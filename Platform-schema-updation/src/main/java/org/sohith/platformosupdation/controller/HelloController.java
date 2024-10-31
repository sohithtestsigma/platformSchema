package org.sohith.platformosupdation.controller;

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
public class HelloController {
  @Autowired
  private  SLPlatformDeviceMobileService slPlatformDeviceMobileService;
  @Autowired
  private  BSPlatformDeviceMobileService bsPlatformDeviceMobileService;
  @Autowired
  private  LTPlatformDeviceMobileService ltpPlatformDeviceMobileService;
  @Autowired
  private LTPlatformDeviceMobileWebService ltpPlatformDeviceMobileWebService;
  @Autowired
  private BSPlatformDeviceMobileWebService bsPlatformDeviceMobileWebService;
  @Autowired
  private SLPlatformDeviceMobileWebService slPlatformDeviceMobileWebService;
  @Autowired
  private SLPlatformBrowserService slPlatformBrowserService;
  @Autowired
  private LTPlatformBrowserService ltPlatformBrowserService;
  @Autowired
  private BSPlatformBrowserService bsPlatformBrowserService;



  @GetMapping("/hello")
  public String sayHello() {

//    bsPlatformDeviceMobileService.syncDevicesFromSauceLabs();
//    ltpPlatformDeviceMobileService.syncDevicesFromSauceLabs();
//    slPlatformDeviceMobileService.syncDevicesFromSauceLabs();


//    bsPlatformDeviceMobileWebService.syncDevicesFromBrowserStack();
//     ltpPlatformDeviceMobileWebService.syncDevicesFromLambdaTest();
//    --- Doubt sauce labs  browser ? os wrong ...
    slPlatformDeviceMobileWebService.syncDevicesFromSauceLabs();


//    bsPlatformBrowserService.syncDevicesFromBrowserStack();
//    ltPlatformBrowserService.syncDevicesFromLambdaTest();
//    slPlatformBrowserService.syncDevicesFromSauceLabs();



    return "Hello";


  }
}
