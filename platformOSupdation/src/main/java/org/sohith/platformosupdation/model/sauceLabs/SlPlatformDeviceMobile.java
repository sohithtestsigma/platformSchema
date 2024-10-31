package org.sohith.platformosupdation.model.sauceLabs;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "sl_platform_devices_mobile")
public class SlPlatformDeviceMobile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String platformKey;

  private String osName;
  private String osVersion;
  private String deviceName;
  private String deviceId;

}
