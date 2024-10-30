package org.sohith.platformosupdation.service.browserstack;

import lombok.extern.slf4j.Slf4j;
import org.sohith.platformosupdation.model.PlatformDeviceMobile;
import org.sohith.platformosupdation.model.browserstack.BSPlatformDeviceMobile;
import org.sohith.platformosupdation.repo.PlatformDeviceMobileRepository;
import org.sohith.platformosupdation.repo.browserstack.BSPlatformDeviceMobileRepository;
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
public class BSPlatformDeviceMobileService {
  @Value("${browserstack.labs.mobile.api.url}")
  private String apiUrl;

  @Value("${browserstack.labs.api.username}")
  private String apiUsername;

  @Value("${browserstack.labs.api.accessKey}")
  private String apiAccessKey;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private PlatformDeviceMobileRepository platformDeviceRepo;

  @Autowired
  private BSPlatformDeviceMobileRepository bsPlatformDeviceRepo;

  @Transactional
  public void syncDevicesFromSauceLabs() {
    HttpHeaders headers = createAuthHeaders();
    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<List> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, List.class);
    List<Map<String, Object>> devices = response.getBody();

    log.info("Found {} devices", devices != null ? devices.size() : 0);


    if (devices != null) {
      devices.forEach(device -> {
        String os = (String) device.get("os");
        String osVersion = (String) device.get("os_version");
        String deviceName = (String) device.get("device");

        String generalizedOsName = normalizeOSName(os);
        String generalizedDeviceName = normalizeDeviceName(deviceName);
        String key = generateKey(generalizedOsName, osVersion, generalizedDeviceName);

        saveToPlatformDevices(generalizedOsName, osVersion, generalizedDeviceName, key);
        saveToSlPlatformDevices(os, osVersion, deviceName, key);
      });
    }
  }

  private HttpHeaders createAuthHeaders() {
    String auth = apiUsername + ":" + apiAccessKey;
    String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Basic " + encodedAuth);
    return headers;
  }

  private void saveToPlatformDevices(String osName, String osVersion, String deviceName, String key) {
    platformDeviceRepo.findByplatformKey(key).ifPresentOrElse(
        existingDevice -> {
          existingDevice.setIsBsSupported(true);
          platformDeviceRepo.save(existingDevice);
        },
        () -> {
          PlatformDeviceMobile newDevice = new PlatformDeviceMobile();
          newDevice.setPlatformKey(key);
          newDevice.setOsName(osName);
          newDevice.setOsVersion(osVersion);
          newDevice.setDeviceName(deviceName);
          newDevice.setIsBsSupported(true);
          platformDeviceRepo.save(newDevice);
        }
    );
  }

  private void saveToSlPlatformDevices(String osName, String osVersion, String deviceName, String key) {
    boolean exists = bsPlatformDeviceRepo.existsByPlatformKey(key);
    if (exists) {return;}
    BSPlatformDeviceMobile bsDevice = new BSPlatformDeviceMobile();
    bsDevice.setPlatformKey( key);
    bsDevice.setOsName(osName);
    bsDevice.setOsVersion(osVersion);
    bsDevice.setDeviceName(deviceName);
    bsPlatformDeviceRepo.save(bsDevice);
  }

  private String normalizeOSName(String os) {
    return os.toLowerCase();
  }

  private String normalizeDeviceName(String deviceName) {
    return deviceName.toLowerCase();
  }

  private String generateKey(String osName, String osVersion, String deviceName) {
    return osName + "-" + osVersion + "-" + deviceName;
  }
}
