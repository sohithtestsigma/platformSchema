package org.sohith.platformosupdation.model.browserstack;

import jakarta.persistence.*;
import lombok.Data;
import org.sohith.platformosupdation.model.PlatformDevicesMobileWeb;

@Entity
@Data
@Table(name = "bs_platform_devices_mobile_web")
public class BsPlatformDevicesMobileWeb {

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


  @ManyToOne
  @JoinColumn(name = "platform_key", referencedColumnName = "platform_key", insertable = false, updatable = false)
  private PlatformDevicesMobileWeb platformDevice;
}
