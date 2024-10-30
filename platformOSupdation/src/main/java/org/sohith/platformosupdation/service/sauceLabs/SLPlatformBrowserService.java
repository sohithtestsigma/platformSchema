package org.sohith.platformosupdation.service.sauceLabs;

import org.sohith.platformosupdation.model.PlatformBrowsers;
import org.sohith.platformosupdation.model.saucelabs.SlPlatformBrowsers;
import org.sohith.platformosupdation.repo.PlatformBrowsersRepository;
import org.sohith.platformosupdation.repo.sauceLabs.SlPlatformBrowsersRepository;
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
public class SLPlatformBrowserService {

  @Value("${sauce.labs.web.api.url}")
  private String apiUrl;

  @Value("${sauce.labs.api.username}")
  private String username;

  @Value("${sauce.labs.api.accessKey}")
  private String accessKey;

  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private PlatformBrowsersRepository platformBrowsersRepository;
  @Autowired
  private SlPlatformBrowsersRepository slPlatformBrowsersRepository;

  @Transactional
  public void syncDevicesFromSauceLabs() {
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(username, accessKey);
    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<List> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, List.class);
    List<Map<String, Object>> devices = response.getBody();

    if (devices != null) {
      devices.forEach(device -> {
        if (device.containsKey("device")) {
          return;
        }

        String os = (String) device.get("os");
        String osName = os.split(" ")[0];
        String osVersion = os.split(" ").length > 1 ? os.split(" ")[1] : "";

        String browserName = (String) device.get("long_name");
        String browserVersion = (String) device.get("short_version");

        String generalizedOsName = normalizeOSName(osName);
        String generalizedBrowser = normalizeBrowserName(browserName);
        String generalizedOsVersion = normalizeOSVersion(browserVersion);
        String platformKey = generatePlatformKey(generalizedOsName, generalizedOsVersion, generalizedBrowser, browserVersion);

        saveToPlatformBrowsers(generalizedOsName, generalizedOsVersion, generalizedBrowser, browserVersion, platformKey);
        saveToSlPlatformBrowsers(osName, osVersion, browserName, browserVersion, platformKey);
      });
    } else {
      System.out.println("No data received from Sauce Labs API.");
    }
  }

  private String generatePlatformKey(String osName, String osVersion, String browserName, String browserVersion) {
    return osName + "-" + osVersion + "-" + browserName + "-" + browserVersion;
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

  private String normalizeOSName(String os) {
    return os.toLowerCase();
  }

  private String normalizeOSVersion(String version) {
    return version.toLowerCase();
  }

  private String normalizeBrowserName(String browserName) {
    return browserName.toLowerCase();
  }
}
