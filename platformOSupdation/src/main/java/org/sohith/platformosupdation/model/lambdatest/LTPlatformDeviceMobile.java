package org.sohith.platformosupdation.model.lambdatest;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "lt_platform_devices_mobile")
public class LTPlatformDeviceMobile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String platformKey;

  private String osName;
  private String osVersion;
  private String deviceName;

}
