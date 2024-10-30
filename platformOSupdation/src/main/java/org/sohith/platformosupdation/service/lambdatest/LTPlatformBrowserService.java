package org.sohith.platformosupdation.service.lambdatest;

import org.sohith.platformosupdation.model.PlatformBrowsers;
import org.sohith.platformosupdation.model.lambdatest.LtPlatformBrowsers;
import org.sohith.platformosupdation.repo.PlatformBrowsersRepository;
import org.sohith.platformosupdation.repo.lambdatest.LtPlatformBrowsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Service
public class LTPlatformBrowserService {

  @Value("${lambdatest.labs.web.api.url}")
  private String apiUrl;


  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private PlatformBrowsersRepository platformBrowsersRepository;
  @Autowired
  private LtPlatformBrowsersRepository ltPlatformBrowsersRepository;

  @Transactional
  public void syncDevicesFromLambdaTest() {
    HttpEntity<String> entity = new HttpEntity<>(null);

    ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Map.class);
    Map<String, Object> responseBody = response.getBody();

    if (responseBody != null && responseBody.containsKey("platforms")) {
      Map<String, List<Map<String, Object>>> platformsByCategory = (Map<String, List<Map<String, Object>>>) responseBody.get("platforms");

      if (platformsByCategory.containsKey("Desktop")) {
        List<Map<String, Object>> desktopPlatforms = platformsByCategory.get("Desktop");


        desktopPlatforms.forEach(platformData -> {
          String platform = (String) platformData.get("platform");
          String osName = platform.split(" ")[0];
          String osVersion = platform.substring(platform.indexOf(" ") + 1);

          List<Map<String, String>> browsers = (List<Map<String, String>>) platformData.get("browsers");

          browsers.forEach(browser -> {
            String browserName = browser.get("browser_name");
            String browserVersion = browser.get("version");

            String generalizedOsName = normalizeOSName(osName);
            String generalizedBrowser = normalizeBrowserName(browserName);
            String generalizedOsVersion = normalizeOSVersion(browserVersion);
            String platformKey = generatePlatformKey(osName, osVersion, browserName, browserVersion);

            saveToPlatformBrowsers(generalizedOsName, generalizedOsVersion, generalizedBrowser, browserVersion, platformKey);
            saveToLtPlatformBrowsers(osName, osVersion, browserName, browserVersion, platformKey);
          });
        });
      } else {
        System.out.println("No Desktop platforms found in LambdaTest API.");
      }
    } else {
      System.out.println("No data received from LambdaTest API.");
    }
  }


  private String generatePlatformKey(String osName, String osVersion, String browserName, String browserVersion) {
    return osName + "-" + osVersion + "-" + browserName + "-" + browserVersion;
  }

  private void saveToPlatformBrowsers(String osName, String osVersion, String browserName, String browserVersion, String platformKey) {
    platformBrowsersRepository.findByPlatformKey(platformKey).ifPresentOrElse(
        existingPlatform -> {
          existingPlatform.setIsLtSupported(true);
          platformBrowsersRepository.save(existingPlatform);
        },
        () -> {
          PlatformBrowsers newPlatformBrowser = new PlatformBrowsers();
          newPlatformBrowser.setPlatformKey(platformKey);
          newPlatformBrowser.setOsName(osName);
          newPlatformBrowser.setOsVersion(osVersion);
          newPlatformBrowser.setBrowserName(browserName);
          newPlatformBrowser.setBrowserVersion(browserVersion);
          newPlatformBrowser.setIsLtSupported(true);
          platformBrowsersRepository.save(newPlatformBrowser);
        }
    );
  }


  private void saveToLtPlatformBrowsers(String osName, String osVersion, String browserName, String browserVersion, String platformKey) {
    if (!ltPlatformBrowsersRepository.existsByPlatformKey(platformKey)) {
      LtPlatformBrowsers ltPlatformBrowsers = new LtPlatformBrowsers();
      ltPlatformBrowsers.setPlatformKey(platformKey);
      ltPlatformBrowsers.setOsName(osName);
      ltPlatformBrowsers.setOsVersion(osVersion);
      ltPlatformBrowsers.setBrowserName(browserName);
      ltPlatformBrowsers.setBrowserVersion(browserVersion);
      ltPlatformBrowsersRepository.save(ltPlatformBrowsers);
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
