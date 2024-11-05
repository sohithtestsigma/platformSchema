package org.sohith.platformosupdation.service.browserstack;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sohith.platformosupdation.model.PlatformDevicesMobileWeb;
import org.sohith.platformosupdation.model.browserstack.BsPlatformDevicesMobileWeb;
import org.sohith.platformosupdation.repo.PlatformDevicesMobileWebRepository;
import org.sohith.platformosupdation.repo.browserstack.BsPlatformDevicesMobileWebRepository;
import org.sohith.platformosupdation.service.PlatformGeneralizer;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BSPlatformDeviceMobileWebService {

  @Value("${browserstack.labs.mobileweb.api.url}")
  private String apiUrl;

  @Value("${browserstack.labs.api.username}")
  private String username;

  @Value("${browserstack.labs.api.accessKey}")
  private String accessKey;

  private final RestTemplate restTemplate;
  private final PlatformDevicesMobileWebRepository platformDevicesRepo;
  private final BsPlatformDevicesMobileWebRepository bsPlatformDevicesMobileWebRepo;
  private final PlatformGeneralizer platformGeneralizer;

  @Transactional
  public void syncDevicesFromBrowserStack() {
    HttpHeaders headers = createAuthHeaders();
    HttpEntity<String> entity = new HttpEntity<>(headers);

    log.info("In BSPlatformDeviceMobileWebService...");

    ResponseEntity<List> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, List.class);
    List<Map<String, Object>> devices = response.getBody();
    if (devices != null) {
      devices.stream()
          .filter(device -> device.get("device") != null)
          .forEach(device -> {
            String os = (String) device.get("os");
            String osVersion = (String) device.get("os_version");
            String browserName = (String) device.get("browser");
            String deviceName = (String) device.get("device");

            String generalizedOsName = platformGeneralizer.generalizeOsName(os);
            String generalizedDeviceName = platformGeneralizer.generalizeDeviceModelName(deviceName);
            String generalizedOsVersion = platformGeneralizer.generalizeOsVersion(osVersion);
            String generalizedBrowserName = platformGeneralizer.generalizeBrowserName(browserName);
            String key = platformGeneralizer.generatePlatformKey(generalizedOsName, generalizedOsVersion, generalizedDeviceName, generalizedBrowserName);

            saveToPlatformDevices(generalizedOsName, generalizedOsVersion, generalizedDeviceName, generalizedBrowserName, key);
            saveToBsPlatformDevices(os, osVersion, deviceName, browserName, key);
          });
    } else {
      log.warn("No device data found in BrowserStack response.");
    }
  }

  private HttpHeaders createAuthHeaders() {
    String auth = username + ":" + accessKey;
    byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
    String authHeader = "Basic " + new String(encodedAuth);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", authHeader);
    return headers;
  }

  private void saveToPlatformDevices(String osName, String osVersion, String deviceName, String browserName, String key) {
    platformDevicesRepo.findByPlatformKey(key).ifPresentOrElse(
        existingDevice -> {
          existingDevice.setIsBsSupported(true);
          platformDevicesRepo.save(existingDevice);
        },
        () -> {
          PlatformDevicesMobileWeb newDevice = new PlatformDevicesMobileWeb();
          newDevice.setPlatformKey(key);
          newDevice.setOsName(osName);
          newDevice.setOsVersion(osVersion);
          newDevice.setDeviceName(deviceName);
          newDevice.setBrowserName(browserName);
          newDevice.setIsBsSupported(true);
          platformDevicesRepo.save(newDevice);
        }
    );
  }

  private void saveToBsPlatformDevices(String osName, String osVersion, String deviceName, String browserName, String key) {
    boolean exists = bsPlatformDevicesMobileWebRepo.existsByPlatformKey(key);
    if (exists) { return; }

    BsPlatformDevicesMobileWeb bsDevice = new BsPlatformDevicesMobileWeb();
    bsDevice.setPlatformKey(key);
    bsDevice.setOsName(osName);
    bsDevice.setOsVersion(osVersion);
    bsDevice.setDeviceName(deviceName);
    bsDevice.setBrowserName(browserName);
    bsPlatformDevicesMobileWebRepo.save(bsDevice);
  }

}

