package org.sohith.platformosupdation.scheduler;

import lombok.RequiredArgsConstructor;
import org.sohith.platformosupdation.service.HelixSupportUpdateService;
import org.sohith.platformosupdation.service.browserstack.BSPlatformBrowserService;
import org.sohith.platformosupdation.service.browserstack.BSPlatformDeviceMobileService;
import org.sohith.platformosupdation.service.browserstack.BSPlatformDeviceMobileWebService;
import org.sohith.platformosupdation.service.lambdatest.LTPlatformBrowserService;
import org.sohith.platformosupdation.service.lambdatest.LTPlatformDeviceMobileService;
import org.sohith.platformosupdation.service.lambdatest.LTPlatformDeviceMobileWebService;
import org.sohith.platformosupdation.service.sauceLabs.SLPlatformBrowserService;
import org.sohith.platformosupdation.service.sauceLabs.SLPlatformDeviceMobileService;
import org.sohith.platformosupdation.service.sauceLabs.SLPlatformDeviceMobileWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataUpdateScheduler {

  private final SLPlatformDeviceMobileService slPlatformDeviceMobileService;
  private final  BSPlatformDeviceMobileService bsPlatformDeviceMobileService;
  private final  LTPlatformDeviceMobileService ltpPlatformDeviceMobileService;
  private final  LTPlatformDeviceMobileWebService ltpPlatformDeviceMobileWebService;
  private final  BSPlatformDeviceMobileWebService bsPlatformDeviceMobileWebService;
  private final  SLPlatformDeviceMobileWebService slPlatformDeviceMobileWebService;
  private final  SLPlatformBrowserService slPlatformBrowserService;
  private final  LTPlatformBrowserService ltPlatformBrowserService;
  private final  BSPlatformBrowserService bsPlatformBrowserService;

  // Schedule to run at midnight every day
  @Scheduled(cron = "0 0 0 * * *")
  public void updateDatabaseDaily() {
    // For Mobile Devices
    bsPlatformDeviceMobileService.syncDevicesFromSauceLabs();
    ltpPlatformDeviceMobileService.syncDevicesFromSauceLabs();
    slPlatformDeviceMobileService.syncDevicesFromSauceLabs();

    // For Mobile Web
    bsPlatformDeviceMobileWebService.syncDevicesFromBrowserStack();
    ltpPlatformDeviceMobileWebService.syncDevicesFromLambdaTest();
    slPlatformDeviceMobileWebService.syncDevicesFromSauceLabs();

    // For Web
    bsPlatformBrowserService.syncDevicesFromBrowserStack();
    ltPlatformBrowserService.syncDevicesFromLambdaTest();
    slPlatformBrowserService.syncDevicesFromSauceLabs();

  }
}
