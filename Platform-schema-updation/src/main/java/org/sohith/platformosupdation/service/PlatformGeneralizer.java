package org.sohith.platformosupdation.service;


import org.sohith.platformosupdation.constants.PlatformConstants;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PlatformGeneralizer {
  private static final Map<String, String> specialOsVersions = new HashMap<>();
  static {
    // Initialize the map with known special OS versions
    specialOsVersions.put("xp", "5.1");
    specialOsVersions.put("snow leopard", "10.6");
    specialOsVersions.put("lion os", "10.7");
    specialOsVersions.put("mountain lion", "10.8");
    specialOsVersions.put("mavericks", "10.9");
    specialOsVersions.put("yosemite", "10.10");
    specialOsVersions.put("el capitan", "10.11");
    specialOsVersions.put("sierra", "10.12");
    specialOsVersions.put("high sierra", "10.13");
    specialOsVersions.put("mojave", "10.14");
    specialOsVersions.put("catalina", "10.15");
    specialOsVersions.put("big sur", "11");
    specialOsVersions.put("monterey", "12");
    specialOsVersions.put("ventura", "13");
    specialOsVersions.put("sonoma", "14");
    specialOsVersions.put("sequoia", "15");
  }

  public  String generalizeOsName(String os) {
    os = os.trim().toLowerCase();
    if (os.contains("win") || os.contains("microsoft")) {
      return PlatformConstants.WINDOWS_OS;
    } else if (os.contains("mac") || os.contains("os x")) {
      return PlatformConstants.MAC_OS;
    } else if (os.contains("linux")) {
      return PlatformConstants.LINUX_OS;
    } else if (os.contains("android")) {
      return PlatformConstants.ANDROID_OS;
    } else if (os.contains("ios") || os.contains("apple") || os.contains("iphone") || os.contains("ipad")) {
      return PlatformConstants.IOS_OS;
    }
    return "Unknown OS";
  }

  public  String generalizeBrowserName(String browser) {
    browser = browser.trim().toLowerCase();
    if (browser.contains("chrome") || browser.contains("chromium") || browser.contains("google") || browser.contains("samsung") || browser.contains("android")) {
      return PlatformConstants.CHROME_BROWSER;
    } else if (browser.contains("firefox") || browser.contains("mozilla")) {
      return PlatformConstants.FIREFOX_BROWSER;
    } else if (browser.contains("edge") || browser.contains("microsoft")) {
      return PlatformConstants.EDGE_BROWSER;
    } else if (browser.contains("opera")) {
      return PlatformConstants.OPERA_BROWSER;
    } else if (browser.contains("safari") || browser.contains("iphone") || browser.contains("ipad")) {
      return PlatformConstants.SAFARI_BROWSER;
    } else if (browser.contains("ie") || browser.contains("internet explorer")) {
      return PlatformConstants.INTERNET_EXPLORER;
    } else if (browser.contains("electron")) {
      return PlatformConstants.ELECTRON_BROWSER;
    }
    return "Unknown Browser";
  }

  public String generalizeVersion(String version) {
    version = version.trim().toLowerCase();

    // Check if the version matches a special OS version
    if (specialOsVersions.containsKey(version)) {
      return specialOsVersions.get(version);
    }

    // Match whole numbers like "16" or versions like "19.3" and "12.0"
    if (version.matches("\\d+")) {
      return version;
    } else if (version.matches("\\d+\\.\\d+")) {
      // Remove trailing ".0" if present (e.g., "12.0" -> "12")
      return version.endsWith(".0") ? version.split("\\.")[0] : version;
    } else if (version.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
      // For four-part versions, return only the first part
      return version.split("\\.")[0];
    } else if (version.matches(".*\\d+.*")) {
      // Extract numbers only for versions with mixed characters
      return version.replaceAll("[^\\d.]", "").split("\\.")[0];
    } else {
      // Convert alphabetic-only versions to lowercase
      return version.toLowerCase();
    }
  }

  public  String generatePlatformKey(String os, String osVersion, String browser, String browserVersion) {
    return String.format("%s-%s-%s-%s", os, osVersion, browser, browserVersion);
  }

  public  String generatePlatformKey(String os, String version, String device) {
    return String.format("%s-%s-%s", os, version, device);
  }

  public  String generalizeDeviceModelName(String modelName) {
    return modelName.trim();
  }
}