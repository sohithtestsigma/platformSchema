package org.sohith.platformosupdation.service;

import lombok.extern.slf4j.Slf4j;
import org.sohith.platformosupdation.repo.PlatformBrowsersRepository;
import org.sohith.platformosupdation.repo.PlatformDeviceMobileRepository;
import org.sohith.platformosupdation.repo.PlatformDevicesMobileWebRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class HelixSupportUpdateService {

  @Autowired
  private PlatformBrowsersRepository platformBrowsersRepository;

  @Autowired
  private PlatformDeviceMobileRepository platformDevicesMobileRepository;

  @Autowired
  private PlatformDevicesMobileWebRepository platformDevicesMobileWebRepository;


}
