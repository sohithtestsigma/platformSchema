package org.sohith.platformosupdation.service.sauceLabs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sohith.platformosupdation.model.PlatformBrowsers;
import org.sohith.platformosupdation.model.sauceLabs.SlPlatformBrowsers;
import org.sohith.platformosupdation.repo.PlatformBrowsersRepository;
import org.sohith.platformosupdation.repo.sauceLabs.SlPlatformBrowsersRepository;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SLPlatformBrowserService {

  @Value("${sauce.labs.web.api.url}")
  private String apiUrl;

  @Value("${sauce.labs.api.username}")
  private String username;

  @Value("${sauce.labs.api.accessKey}")
  private String accessKey;

  private final RestTemplate restTemplate;
  private final PlatformBrowsersRepository platformBrowsersRepository;
  private final SlPlatformBrowsersRepository slPlatformBrowsersRepository;
  private final PlatformGeneralizer platformGeneralizer;

  @Transactional
  public void syncDevicesFromSauceLabs() {
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(username, accessKey);
    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<List> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, List.class);
    List<Map<String, Object>> devices = response.getBody();

    log.info("In SLPlatformBrowserService...");

    if (devices != null) {
      devices.forEach(device -> {
        if (device.containsKey("device")) {
          return;
        }


        String os = (String) device.get("os");
        String osName = os.split(" ")[0];
        String osVersion = os.split(" ").length > 1 ? os.split(" ")[1] : "";

        String browserName = (String) device.get("api_name");
        String browserVersion = (String) device.get("short_version");

        String generalizedOsName = platformGeneralizer.generalizeOsName(osName);
        String generalizedBrowser = platformGeneralizer.generalizeBrowserName(browserName);
        String generalizedOsVersion = platformGeneralizer.generalizeVersion(osVersion);
        String generalizedBrowserVersion = platformGeneralizer.generalizeVersion(browserVersion);
        String platformKey = platformGeneralizer.generatePlatformKey(generalizedOsName, generalizedOsVersion, generalizedBrowser, generalizedBrowserVersion);

        saveToPlatformBrowsers(generalizedOsName, generalizedOsVersion, generalizedBrowser, generalizedBrowserVersion, platformKey);
        saveToSlPlatformBrowsers(os, os, browserName, browserVersion, platformKey);
      });
    } else {
      System.out.println("No data received from Sauce Labs API.");
    }
  }



  private void saveToPlatformBrowsers(String osName, String osVersion, String browserName, String browserVersion, String platformKey) {
    Optional<PlatformBrowsers> existingPlatform = platformBrowsersRepository.findByPlatformKey(platformKey);
    if (existingPlatform.isEmpty()) {
      PlatformBrowsers platformBrowsers = new PlatformBrowsers();
      platformBrowsers.setPlatformKey(platformKey);
      platformBrowsers.setOsName(osName);
      platformBrowsers.setOsVersion(osVersion);
      platformBrowsers.setBrowserName(browserName);
      platformBrowsers.setBrowserVersion(browserVersion);
      platformBrowsers.setIsSlSupported(true);
      platformBrowsersRepository.save(platformBrowsers);
    }
    else {
      existingPlatform.get().setPlatformKey(platformKey);
      platformBrowsersRepository.save(existingPlatform.get());
    }
  }

  private void saveToSlPlatformBrowsers(String osName, String osVersion, String browserName, String browserVersion, String platformKey) {
    if (!slPlatformBrowsersRepository.existsByPlatformKey(platformKey)) {
      SlPlatformBrowsers slPlatformBrowsers = new SlPlatformBrowsers();
      slPlatformBrowsers.setPlatformKey(platformKey);
      slPlatformBrowsers.setOsName(osName);
      slPlatformBrowsers.setOsVersion(osVersion);
      slPlatformBrowsers.setBrowserName(browserName);
      slPlatformBrowsers.setBrowserVersion(browserVersion);
      slPlatformBrowsersRepository.save(slPlatformBrowsers);
    }
  }

}
