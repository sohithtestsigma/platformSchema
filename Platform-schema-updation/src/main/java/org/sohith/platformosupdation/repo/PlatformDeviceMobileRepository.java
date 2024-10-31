package org.sohith.platformosupdation.repo;

import org.sohith.platformosupdation.model.PlatformDeviceMobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatformDeviceMobileRepository extends JpaRepository<PlatformDeviceMobile, Integer> {
  Optional<PlatformDeviceMobile> findByplatformKey(String key);

  @Modifying
  @Query("UPDATE PlatformDeviceMobile pdm SET pdm.helixSupported = true WHERE pdm.isLtSupported = true AND pdm.isSlSupported = true AND pdm.isBsSupported = true")
  int updateHelixSupportForEligibleRecords();

}
