package org.sohith.platformosupdation.repo.sauceLabs;

import org.sohith.platformosupdation.model.sauceLabs.SlPlatformDeviceMobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlPlatformDeviceMobileRepository extends JpaRepository<SlPlatformDeviceMobile, Integer> {
  boolean existsByPlatformKey(String platformKey);
}
