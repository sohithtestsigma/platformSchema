package org.sohith.platformosupdation.model.lambdatest;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lt_platform_browsers")
@Data
public class LtPlatformBrowsers {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "platform_key", nullable = false)
  private String platformKey;

  @Column(name = "os_name")
  private String osName;

  @Column(name = "os_version")
  private String osVersion;

  @Column(name = "browser_name")
  private String browserName;

  @Column(name = "browser_version")
  private String browserVersion;
}
