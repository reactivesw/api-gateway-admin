package io.reactivesw.gateway.admin.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Verify result.
 */
@Data
public class VerifyResult implements Serializable {
  /**
   * Auto generated.
   */
  private static final long serialVersionUID = -7573340224750295318L;

  /**
   * Has the admin login.
   */
  private boolean isLogin;

  /**
   * Has the right permissions.
   */
  private boolean hashPermission;

  /**
   * Admin id.
   */
  private String adminId;
}
