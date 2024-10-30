package org.sohith.platformosupdation.repo.sauceLabs;

import org.sohith.platformosupdation.model.sauceLabs.SlPlatformDevicesMobileWeb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlPlatformDevicesMobileWebRepository extends JpaRepository<SlPlatformDevicesMobileWeb, Integer> {
  boolean existsByPlatformKey(String platformKey);
}
