package org.sohith.platformosupdation.service.lambdatest;

import lombok.extern.slf4j.Slf4j;
import org.sohith.platformosupdation.model.PlatformDeviceMobile;
import org.sohith.platformosupdation.model.lambdatest.LTPlatformDeviceMobile;
import org.sohith.platformosupdation.repo.PlatformDeviceMobileRepository;
import org.sohith.platformosupdation.repo.lambdatest.LTPlatformDeviceMobileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LTPlatformDeviceMobileService {

  @Value("${lambdatest.labs.mobile.api.url}")
  private String apiUrl;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private PlatformDeviceMobileRepository platformDeviceRepo;

  @Autowired
  private LTPlatformDeviceMobileRepository ltPlatformDeviceMobileRepository;

  @Transactional
  public void syncDevicesFromSauceLabs() {
    HttpEntity<String> entity = new HttpEntity<>(null);

    ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Map.class);
    Map<String, Object> body = response.getBody();

    if (body != null && body.containsKey("platforms")) {
      Map<String, Object> platforms = (Map<String, Object>) body.get("platforms");
      List<Map<String, Object>> mobilePlatforms = (List<Map<String, Object>>) platforms.get("Mobile");

      if (mobilePlatforms != null) {
        log.info("Found {} mobile platforms", mobilePlatforms.size());

        // Iterate through each platform (iOS, Android, etc.)
        mobilePlatforms.forEach(platformData -> {
          String os = (String) platformData.get("platform");
          List<Map<String, Object>> devices = (List<Map<String, Object>>) platformData.get("devices");

          // Process each device for the given platform
          if (devices != null) {
            devices.forEach(device -> {
              String deviceName = (String) device.get("device_name");
              String osVersion = (String) device.get("version");

              String generalizedOsName = normalizeOSName(os);
              String generalizedDeviceName = normalizeDeviceName(deviceName);
              String key = generateKey(generalizedOsName, osVersion, generalizedDeviceName);

              saveToPlatformDevices(generalizedOsName, osVersion, generalizedDeviceName, key);
              saveToSlPlatformDevices(os, osVersion, deviceName, key);
            });
          }
        });
      }
    } else {
      log.warn("No platforms data found in response.");
    }
  }


  private void saveToPlatformDevices(String osName, String osVersion, String deviceName, String key) {
    platformDeviceRepo.findByplatformKey(key).ifPresentOrElse(
        existingDevice -> {
          existingDevice.setIsLtSupported(true);
          platformDeviceRepo.save(existingDevice);
        },
        () -> {
          PlatformDeviceMobile newDevice = new PlatformDeviceMobile();
          newDevice.setPlatformKey(key);
          newDevice.setOsName(osName);
          newDevice.setOsVersion(osVersion);
          newDevice.setDeviceName(deviceName);
          newDevice.setIsLtSupported(true);
          platformDeviceRepo.save(newDevice);
        }
    );
  }

  private void saveToSlPlatformDevices(String osName, String osVersion, String deviceName, String key) {
    boolean exists = ltPlatformDeviceMobileRepository.existsByPlatformKey(key);
    if (exists) { return; }

    LTPlatformDeviceMobile ltDevice = new LTPlatformDeviceMobile();
    ltDevice.setPlatformKey(key);
    ltDevice.setOsName(osName);
    ltDevice.setOsVersion(osVersion);
    ltDevice.setDeviceName(deviceName);
    ltPlatformDeviceMobileRepository.save(ltDevice);
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
