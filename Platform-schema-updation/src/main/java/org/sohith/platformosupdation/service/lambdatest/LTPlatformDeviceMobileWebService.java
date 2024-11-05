package org.sohith.platformosupdation.service.lambdatest;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sohith.platformosupdation.model.PlatformDevicesMobileWeb;
import org.sohith.platformosupdation.model.lambdatest.LtPlatformDevicesMobileWeb;
import org.sohith.platformosupdation.repo.PlatformDevicesMobileWebRepository;
import org.sohith.platformosupdation.repo.lambdatest.LtPlatformDevicesMobileWebRepository;
import org.sohith.platformosupdation.service.PlatformGeneralizer;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LTPlatformDeviceMobileWebService {

  @Value("${lambdatest.labs.mobileweb.api.url}")
  private String apiUrl;

  private final RestTemplate restTemplate;
  private final PlatformDevicesMobileWebRepository platformDeviceWebRepo;
  private final LtPlatformDevicesMobileWebRepository ltPlatformDeviceMobileWebRepo;
  private final PlatformGeneralizer platformGeneralizer;

  @Transactional
  public void syncDevicesFromLambdaTest() {
    HttpEntity<String> entity = new HttpEntity<>(null);
    ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Map.class);
    Map<String, Object> body = response.getBody();

    log.info("In LTPlatformDeviceMobileWebService...");

    if (body != null) {
      body.forEach((osType, platformData) -> {
        List<Map<String, Object>> brands = (List<Map<String, Object>>) platformData;

        String generalizedOSName = platformGeneralizer.generalizeOsName(osType);

        brands.forEach(brandData -> {
          String brand = (String) brandData.get("brand");
          List<Map<String, Object>> devices = (List<Map<String, Object>>) brandData.get("devices");
          devices.forEach(device -> {
            String deviceName = (String) device.get("deviceName");
            String browserName = (String) device.get("browserId");

            String generalizedDeviceName = platformGeneralizer.generalizeDeviceModelName(brand+" "+deviceName);
            String generalizedBrowserName = platformGeneralizer.generalizeBrowserName(browserName);




            List<Map<String, Object>> osVersions = (List<Map<String, Object>>) device.get("osVersion");

            osVersions.forEach(osVersionData -> {
              String osVersion = (String) osVersionData.get("version");
              String osId = (String) osVersionData.get("id");

              String generalizedOSVersion = platformGeneralizer.generalizeOsVersion(osVersion);

              String key = platformGeneralizer.generatePlatformKey(generalizedOSName, generalizedOSVersion, generalizedDeviceName, generalizedBrowserName);

              saveToPlatformDevices(generalizedOSName, generalizedOSVersion, generalizedDeviceName, generalizedBrowserName, key);
              saveToLtPlatformDevices(osType, osVersion, deviceName, browserName, key, osId);
            });
          });
        });
      });
    } else {
      log.warn("No device data found in response.");
    }
  }


  private void saveToPlatformDevices(String osName, String osVersion, String deviceName, String browserName, String key) {
    platformDeviceWebRepo.findByPlatformKey(key).ifPresentOrElse(
        existingDevice -> {
          existingDevice.setIsLtSupported(true);
          platformDeviceWebRepo.save(existingDevice);
        },
        () -> {
          PlatformDevicesMobileWeb newDevice = new PlatformDevicesMobileWeb();
          newDevice.setPlatformKey(key);
          newDevice.setOsName(osName);
          newDevice.setOsVersion(osVersion);
          newDevice.setDeviceName(deviceName);
          newDevice.setBrowserName(browserName);
          newDevice.setIsLtSupported(true);
          platformDeviceWebRepo.save(newDevice);
        }
    );
  }

  private void saveToLtPlatformDevices(String osName, String osVersion, String deviceName, String browserName, String key, String osId) {
    if (ltPlatformDeviceMobileWebRepo.existsByPlatformKey(key)) return;

    LtPlatformDevicesMobileWeb ltDevice = new LtPlatformDevicesMobileWeb();
    ltDevice.setPlatformKey(key);
    ltDevice.setOsName(osName);
    ltDevice.setOsVersion(osVersion);
    ltDevice.setDeviceName(deviceName);
    ltDevice.setBrowserName(browserName);
    ltDevice.setOsId(osId);
    ltPlatformDeviceMobileWebRepo.save(ltDevice);
  }

}
