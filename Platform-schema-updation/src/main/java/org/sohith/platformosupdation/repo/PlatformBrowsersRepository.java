package org.sohith.platformosupdation.repo;

import org.sohith.platformosupdation.model.PlatformBrowsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlatformBrowsersRepository extends JpaRepository<PlatformBrowsers, Integer> {
  Optional<PlatformBrowsers> findByPlatformKey(String platformKey);

  @Modifying
  @Query("UPDATE PlatformBrowsers pb SET pb.helixSupported = true WHERE pb.isLtSupported = true AND pb.isSlSupported = true AND pb.isBsSupported = true")
  int updateHelixSupportForEligibleRecords();

  @Query("SELECT pb FROM PlatformBrowsers pb WHERE pb.helixSupported = true")
  List<PlatformBrowsers> findAllSupportedData();

}
