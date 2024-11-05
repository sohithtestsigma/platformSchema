package org.sohith.platformosupdation.service.browserstack;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sohith.platformosupdation.model.PlatformBrowsers;
import org.sohith.platformosupdation.model.browserstack.BsPlatformBrowsers;
import org.sohith.platformosupdation.repo.PlatformBrowsersRepository;
import org.sohith.platformosupdation.repo.browserstack.BsPlatformBrowsersRepository;
import org.sohith.platformosupdation.service.PlatformGeneralizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BSPlatformBrowserService {

  @Value("${browserstack.labs.web.api.url}")
  private String apiUrl;

  @Value("${browserstack.labs.api.username}")
  private String username;

  @Value("${browserstack.labs.api.accessKey}")
  private String accessKey;

  
  private final RestTemplate restTemplate;
  private final PlatformBrowsersRepository platformBrowsersRepository;
  private final  BsPlatformBrowsersRepository bsPlatformBrowsersRepository;
  private final PlatformGeneralizer platformGeneralizer;

  @Transactional
  public void syncDevicesFromBrowserStack() {
    HttpHeaders headers = createAuthHeaders();
    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<List> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, List.class);
    List<Map<String, Object>> devices = response.getBody();

    log.info("In BSPlatformBrowserService...");

    if (devices != null) {
      devices.stream()
          .filter(device -> device.get("device") == null)
          .forEach(device -> {
            String os = (String) device.get("os");
            String osVersion = (String) device.get("os_version");
            String browserName = (String) device.get("browser");
            String browserVersion = (String) device.get("browser_version");



            String generalizedOsName = platformGeneralizer.generalizeOsName(os);
            String generalizedOSVersion = platformGeneralizer.generalizeOsVersion(osVersion);
            String generalizedBrowserVersion = platformGeneralizer.generalizeBrowserVersion(browserVersion);
            String generalizedBrowserName = platformGeneralizer.generalizeBrowserName(browserName);
            String key = platformGeneralizer.generatePlatformKey(generalizedOsName, generalizedOSVersion, generalizedBrowserName, generalizedBrowserVersion);

            if (platformGeneralizer.isLatestOsVersion(generalizedOSVersion) && platformGeneralizer.isLatestBrowserVersion(generalizedBrowserVersion)){
              saveToPlatformDevices(generalizedOsName, generalizedOSVersion, generalizedBrowserVersion, generalizedBrowserName, key);
              saveToBsPlatformDevices(os, osVersion, browserVersion, browserName, key);
            }


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

  private void saveToPlatformDevices(String osName, String osVersion, String browserVersion, String browserName, String key) {
    Optional<PlatformBrowsers> existingDeviceOpt = platformBrowsersRepository.findByPlatformKey(key);

    existingDeviceOpt.ifPresentOrElse(existingDevice -> {
      existingDevice.setIsBsSupported(true);
      platformBrowsersRepository.save(existingDevice);
      log.info("Updated existing device: {}", existingDevice);
    }, () -> {
      PlatformBrowsers newDevice = new PlatformBrowsers();
      newDevice.setPlatformKey(key);
      newDevice.setOsName(osName);
      newDevice.setOsVersion(osVersion);
      newDevice.setBrowserName(browserName);
      newDevice.setBrowserVersion(browserVersion);
      newDevice.setIsBsSupported(true);
      platformBrowsersRepository.save(newDevice);
    });
  }

  private void saveToBsPlatformDevices(String osName, String osVersion, String browserVersion, String browserName, String key) {
    if (bsPlatformBrowsersRepository.existsByPlatformKey(key)) {
      log.info("Device already exists in BS platform devices: {}", key);
      return;
    }

    BsPlatformBrowsers bsDevice = new BsPlatformBrowsers();
    bsDevice.setPlatformKey(key);
    bsDevice.setOsName(osName);
    bsDevice.setOsVersion(osVersion);
    bsDevice.setBrowserVersion(browserVersion);
    bsDevice.setBrowserName(browserName);
    bsPlatformBrowsersRepository.save(bsDevice);
  }

}
