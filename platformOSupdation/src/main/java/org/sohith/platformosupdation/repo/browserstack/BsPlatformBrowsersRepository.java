package org.sohith.platformosupdation.repo.browserstack;

import org.sohith.platformosupdation.model.browserstack.BsPlatformBrowsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BsPlatformBrowsersRepository extends JpaRepository<BsPlatformBrowsers, Integer> {
  boolean existsByPlatformKey(String platformKey);
}
