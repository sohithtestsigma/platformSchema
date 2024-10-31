package org.sohith.platformosupdation.repo.lambdatest;

import org.sohith.platformosupdation.model.lambdatest.LtPlatformBrowsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LtPlatformBrowsersRepository extends JpaRepository<LtPlatformBrowsers, Integer> {
  boolean existsByPlatformKey(String platformKey);
}
