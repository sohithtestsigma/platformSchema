package org.sohith.platformosupdation.repo;

import org.sohith.platformosupdation.model.PlatformDevicesMobileWeb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlatformDevicesMobileWebRepository extends JpaRepository<PlatformDevicesMobileWeb, Integer> {
  Optional<PlatformDevicesMobileWeb> findByPlatformKey(String platformKey);
}

