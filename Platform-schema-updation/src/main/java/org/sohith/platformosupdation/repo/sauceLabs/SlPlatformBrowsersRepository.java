package org.sohith.platformosupdation.repo.sauceLabs;


import org.sohith.platformosupdation.model.sauceLabs.SlPlatformBrowsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlPlatformBrowsersRepository extends JpaRepository<SlPlatformBrowsers, Integer> {
  boolean existsByPlatformKey(String platformKey);
}
