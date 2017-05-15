package io.reactivesw.gateway.admin.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Verify body.
 */
@Data
public class VerifyBody implements Serializable {

  /**
   * Auto generated.
   */
  private static final long serialVersionUID = -735270777148367053L;

  /**
   * Token.
   */
  private String token;

  /**
   * Resource path.
   */
  private String resource;

  /**
   * Request Action.
   */
  private String action;
}
