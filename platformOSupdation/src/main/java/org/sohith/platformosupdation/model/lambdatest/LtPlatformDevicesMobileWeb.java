package org.sohith.platformosupdation.model.lambdatest;

import jakarta.persistence.*;
import lombok.Data;
import org.sohith.platformosupdation.model.PlatformDevicesMobileWeb;

@Entity
@Data
@Table(name = "lt_platform_devices_mobile_web")
public class LtPlatformDevicesMobileWeb {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "platform_key", nullable = false)
  private String platformKey;

  @Column(name = "os_name")
  private String osName;

  @Column(name = "os_version")
  private String osVersion;

  @Column(name = "device_name")
  private String deviceName;

  @Column(name = "browser_name")
  private String browserName;

  @Column(name = "browser_version")
  private String browserVersion;

  @ManyToOne
  @JoinColumn(name = "platform_key", referencedColumnName = "platform_key", insertable = false, updatable = false)
  private PlatformDevicesMobileWeb platformDevice;
}