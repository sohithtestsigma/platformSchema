package org.sohith.platformosupdation.repo;

import org.sohith.platformosupdation.model.PlatformDeviceMobile;
import org.sohith.platformosupdation.model.PlatformDevicesMobileWeb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlatformDevicesMobileWebRepository extends JpaRepository<PlatformDevicesMobileWeb, Integer> {
  Optional<PlatformDevicesMobileWeb> findByPlatformKey(String platformKey);


  @Query("SELECT pb FROM PlatformDevicesMobileWeb pb WHERE pb.isLtSupported = true AND pb.isSlSupported = true AND pb.isBsSupported = true")
  List<PlatformDevicesMobileWeb> findAllSupportedData();
}

