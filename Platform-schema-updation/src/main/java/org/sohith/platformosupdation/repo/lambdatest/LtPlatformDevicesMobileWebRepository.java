package org.sohith.platformosupdation.repo.lambdatest;

import org.sohith.platformosupdation.model.lambdatest.LtPlatformDevicesMobileWeb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LtPlatformDevicesMobileWebRepository extends JpaRepository<LtPlatformDevicesMobileWeb, Integer> {
  boolean existsByPlatformKey(String platformKey);
}

