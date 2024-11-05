package org.sohith.platformosupdation.repo;

import org.sohith.platformosupdation.model.PlatformBrowsers;
import org.sohith.platformosupdation.model.PlatformDeviceMobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlatformDeviceMobileRepository extends JpaRepository<PlatformDeviceMobile, Integer> {
  Optional<PlatformDeviceMobile> findByplatformKey(String key);

  @Query("SELECT pb FROM PlatformDeviceMobile pb WHERE pb.isLtSupported = true AND pb.isSlSupported = true AND pb.isBsSupported = true")
  List<PlatformDeviceMobile> findAllSupportedData();
}
