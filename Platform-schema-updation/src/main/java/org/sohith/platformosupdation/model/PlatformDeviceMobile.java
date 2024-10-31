package org.sohith.platformosupdation.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "platform_devices_mobile")
public class PlatformDeviceMobile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true, nullable = false)
  private String platformKey;

  private String osName;
  private String osVersion;
  private String deviceName;
  private Boolean isBsSupported;
  private Boolean isLtSupported;
  private Boolean isSlSupported;
  private Boolean helixSupported;

}
