package org.sohith.platformosupdation.repo;

import org.sohith.platformosupdation.model.PlatformBrowsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlatformBrowsersRepository extends JpaRepository<PlatformBrowsers, Integer> {
  Optional<PlatformBrowsers> findByPlatformKey(String platformKey);
}
