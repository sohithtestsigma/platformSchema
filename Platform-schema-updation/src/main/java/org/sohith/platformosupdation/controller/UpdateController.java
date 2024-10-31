package org.sohith.platformosupdation.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UpdateController {
  private final SLPlatformDeviceMobileService slPlatformDeviceMobileService;
  private final  BSPlatformDeviceMobileService bsPlatformDeviceMobileService;
  private final LTPlatformDeviceMobileService ltpPlatformDeviceMobileService;
  private final LTPlatformDeviceMobileWebService ltpPlatformDeviceMobileWebService;
  private final BSPlatformDeviceMobileWebService bsPlatformDeviceMobileWebService;
  private final SLPlatformDeviceMobileWebService slPlatformDeviceMobileWebService;
  private final SLPlatformBrowserService slPlatformBrowserService;
  private final LTPlatformBrowserService ltPlatformBrowserService;
  private final BSPlatformBrowserService bsPlatformBrowserService;
  private final HelixSupportUpdateService helixSupportUpdateService;



  @GetMapping("/update")
  public String sayHello() {

    // for Mobile Devices
    bsPlatformDeviceMobileService.syncDevicesFromSauceLabs();
    ltpPlatformDeviceMobileService.syncDevicesFromSauceLabs();
    slPlatformDeviceMobileService.syncDevicesFromSauceLabs();

    // for Mobile Web
    bsPlatformDeviceMobileWebService.syncDevicesFromBrowserStack();
    ltpPlatformDeviceMobileWebService.syncDevicesFromLambdaTest();
    slPlatformDeviceMobileWebService.syncDevicesFromSauceLabs();

    // for Web
    bsPlatformBrowserService.syncDevicesFromBrowserStack();
    ltPlatformBrowserService.syncDevicesFromLambdaTest();
    slPlatformBrowserService.syncDevicesFromSauceLabs();

    helixSupportUpdateService.updateHelixSupportForAllPlatforms();


    return "updated successfully";

  }
}
