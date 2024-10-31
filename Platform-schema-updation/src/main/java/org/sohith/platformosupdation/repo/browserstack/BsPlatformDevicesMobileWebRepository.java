package org.sohith.platformosupdation.repo.browserstack;

import org.sohith.platformosupdation.model.browserstack.BsPlatformDevicesMobileWeb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BsPlatformDevicesMobileWebRepository extends JpaRepository<BsPlatformDevicesMobileWeb, Integer> {
  boolean existsByPlatformKey(String platformKey);
}
