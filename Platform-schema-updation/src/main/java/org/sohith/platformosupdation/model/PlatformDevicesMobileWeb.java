package org.sohith.platformosupdation.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "platform_devices_mobile_web")
public class PlatformDevicesMobileWeb {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "platform_key", unique = true, nullable = false)
  private String platformKey;

  @Column(name = "os_name")
  private String osName;

  @Column(name = "os_version")
  private String osVersion;

  @Column(name = "device_name")
  private String deviceName;

  @Column(name = "browser_name")
  private String browserName;


  @Column(name = "is_bs_supported")
  private Boolean isBsSupported;

  @Column(name = "is_lt_supported")
  private Boolean isLtSupported;

  @Column(name = "is_sl_supported")
  private Boolean isSlSupported;

}
