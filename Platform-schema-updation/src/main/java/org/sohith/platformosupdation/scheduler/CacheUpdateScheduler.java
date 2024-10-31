package org.sohith.platformosupdation.scheduler;

import lombok.RequiredArgsConstructor;
import org.sohith.platformosupdation.service.HelixDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CacheUpdateScheduler {

  private final HelixDataService platformDataService;

  // Runs every day at 2 AM to refresh the cache
  @Scheduled(cron = "0 0 2 * * ?")
  public void refreshCacheDaily() {
    platformDataService.refreshPlatformDataCache();
  }
}
