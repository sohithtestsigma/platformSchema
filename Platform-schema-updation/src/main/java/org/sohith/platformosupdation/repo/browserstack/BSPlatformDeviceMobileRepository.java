package org.sohith.platformosupdation.repo.browserstack;

import org.sohith.platformosupdation.model.browserstack.BSPlatformDeviceMobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BSPlatformDeviceMobileRepository extends JpaRepository<BSPlatformDeviceMobile, Integer> {
  boolean existsByPlatformKey(String platformKey);
}
