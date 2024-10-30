package org.sohith.platformosupdation.repo;

import org.sohith.platformosupdation.model.PlatformDeviceMobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatformDeviceMobileRepository extends JpaRepository<PlatformDeviceMobile, Integer> {
  Optional<PlatformDeviceMobile> findByplatformKey(String key);
}
