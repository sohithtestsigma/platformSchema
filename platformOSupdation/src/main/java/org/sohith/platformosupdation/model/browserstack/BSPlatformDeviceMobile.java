package org.sohith.platformosupdation.model.browserstack;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "bs_platform_devices_mobile")
public class BSPlatformDeviceMobile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String platformKey;

  private String osName;
  private String osVersion;
  private String deviceName;

}
