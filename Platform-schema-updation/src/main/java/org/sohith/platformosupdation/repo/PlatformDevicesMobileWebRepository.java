package org.sohith.platformosupdation.repo;

import org.sohith.platformosupdation.model.PlatformDevicesMobileWeb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PlatformDevicesMobileWebRepository extends JpaRepository<PlatformDevicesMobileWeb, Integer> {
  Optional<PlatformDevicesMobileWeb> findByPlatformKey(String platformKey);

  @Modifying
  @Query("UPDATE PlatformDevicesMobileWeb pdmw SET pdmw.helixSupported = true WHERE pdmw.isLtSupported = true AND pdmw.isSlSupported = true AND pdmw.isBsSupported = true")
  int updateHelixSupportForEligibleRecords();

}

