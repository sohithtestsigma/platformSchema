package org.sohith.platformosupdation.repo.lambdatest;

import org.sohith.platformosupdation.model.lambdatest.LTPlatformDeviceMobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LTPlatformDeviceMobileRepository extends JpaRepository<LTPlatformDeviceMobile, Integer> {
  boolean existsByPlatformKey(String platformKey);
}
