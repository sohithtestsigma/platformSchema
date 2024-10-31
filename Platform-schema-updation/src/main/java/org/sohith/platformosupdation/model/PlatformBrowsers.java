package org.sohith.platformosupdation.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "platform_browsers")
@Data
public class PlatformBrowsers {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "platform_key", nullable = false, unique = true)
  private String platformKey;

  @Column(name = "os_name")
  private String osName;

  @Column(name = "os_version")
  private String osVersion;

  @Column(name = "browser_name")
  private String browserName;

  @Column(name = "browser_version")
  private String browserVersion;

  @Column(name = "is_bs_supported")
  private Boolean isBsSupported;

  @Column(name = "is_lt_supported")
  private Boolean isLtSupported;

  @Column(name = "is_sl_supported")
  private Boolean isSlSupported;

  @Column(name = "helix_supported")
  private Boolean helixSupported;
}
