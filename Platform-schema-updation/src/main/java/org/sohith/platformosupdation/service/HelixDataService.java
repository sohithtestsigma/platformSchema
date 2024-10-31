package org.sohith.platformosupdation.service;

import lombok.RequiredArgsConstructor;
import org.sohith.platformosupdation.model.PlatformBrowsers;
import org.sohith.platformosupdation.repo.PlatformBrowsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HelixDataService {

  private final PlatformBrowsersRepository platformRepository;

  @Cacheable(value = "platformDataCache")
  public List<PlatformBrowsers> getCachedPlatformData() {
    return platformRepository.findAllSupportedData();
  }


  @CachePut(value = "platformDataCache")
  public List<PlatformBrowsers> refreshPlatformDataCache() {
    List<PlatformBrowsers> updatedData = platformRepository.findAllSupportedData();
    return updatedData;
  }

}
