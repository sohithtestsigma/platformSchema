package org.sohith.platformosupdation.service.sauceLabs;

import lombok.extern.slf4j.Slf4j;
import org.sohith.platformosupdation.model.PlatformDevicesMobileWeb;
import org.sohith.platformosupdation.model.sauceLabs.SlPlatformDevicesMobileWeb;
import org.sohith.platformosupdation.repo.PlatformDevicesMobileWebRepository;
import org.sohith.platformosupdation.repo.sauceLabs.SlPlatformDevicesMobileWebRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SLPlatformDeviceMobileWebService {

  @Value("${sauce.labs.mobileweb.api.url}")
  private String apiUrl;

  @Value("${sauce.labs.api.username}")
  private String apiUsername;

  @Value("${sauce.labs.api.accessKey}")
  private String apiAccessKey;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private PlatformDevicesMobileWebRepository platformDevicesRepo;

  @Autowired
  private SlPlatformDevicesMobileWebRepository slPlatformDevicesMobileWebRepo;

  @Transactional
  public void syncDevicesFromSauceLabs() {
    HttpHeaders headers = createAuthHeaders();
    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<List> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, List.class);
    List<Map<String, Object>> devices = response.getBody();

    if (devices != null) {
      devices
          .forEach(device -> {
            String os = (String) device.get("api_name");
            String osVersion = (String) device.get("short_version");
            String deviceName = (String) device.get("device");

            // Normalize OS name to "ios" if the api_name is apple or ipad, otherwise keep the original os name
            String generalizedOsName = normalizeOSName(os);

            // Set browser based on OS: Safari for iOS devices, Chrome for others
            String browserName = generalizedOsName.equals("ios") ? "safari" : "chrome";

            String generalizedDeviceName = normalizeDeviceName(deviceName);
            String key = generateKey(generalizedOsName, osVersion, generalizedDeviceName, browserName);

            saveToPlatformDevices(generalizedOsName, osVersion, generalizedDeviceName, browserName, key);
            saveToSlPlatformDevices(os, osVersion, deviceName, browserName, key);
          });
    } else {
      log.warn("No device data found in Sauce Labs response.");
    }
  }


  private HttpHeaders createAuthHeaders() {
    String auth = apiUsername + ":" + apiAccessKey;
    String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Basic " + encodedAuth);
    return headers;
  }


  private String normalizeOSName(String os) {
    if ("apple".equalsIgnoreCase(os) || "ipad".equalsIgnoreCase(os)) {
      return "ios";
    }
    return os.toLowerCase();
  }

  private void saveToPlatformDevices(String osName, String osVersion, String deviceName, String browserName, String key) {
    platformDevicesRepo.findByPlatformKey(key).ifPresentOrElse(
        existingDevice -> {
          existingDevice.setIsSlSupported(true);
          platformDevicesRepo.save(existingDevice);
        },
        () -> {
          PlatformDevicesMobileWeb newDevice = new PlatformDevicesMobileWeb();
          newDevice.setPlatformKey(key);
          newDevice.setOsName(osName);
          newDevice.setOsVersion(osVersion);
          newDevice.setDeviceName(deviceName);
          newDevice.setBrowserName(browserName);
          newDevice.setIsSlSupported(true);
          platformDevicesRepo.save(newDevice);
        }
    );
  }

  private void saveToSlPlatformDevices(String osName, String osVersion, String deviceName, String browserName, String key) {
    boolean exists = slPlatformDevicesMobileWebRepo.existsByPlatformKey(key);
    if (exists) { return; }

    SlPlatformDevicesMobileWeb slDevice = new SlPlatformDevicesMobileWeb();
    slDevice.setPlatformKey(key);
    slDevice.setOsName(osName);
    slDevice.setOsVersion(osVersion);
    slDevice.setDeviceName(deviceName);
    slDevice.setBrowserName(browserName);
    slPlatformDevicesMobileWebRepo.save(slDevice);
  }

  private String normalizeDeviceName(String deviceName) {
    return deviceName.toLowerCase();
  }

  private String generateKey(String osName, String osVersion, String deviceName, String browserName) {
    return osName + "-" + osVersion + "-" + deviceName + "-" + browserName;
  }
}
