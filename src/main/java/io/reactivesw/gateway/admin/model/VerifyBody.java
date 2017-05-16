package io.reactivesw.gateway.admin.model;

import java.io.Serializable;

/**
 * Verify body.
 */
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

  /**
   * Constructor.
   *
   * @param token    String
   * @param resource String
   * @param action   String
   */
  public VerifyBody(String token, String resource, String action) {
    this.token = token;
    this.resource = resource;
    this.action = action;
  }

  /**
   * Getter.
   *
   * @return String
   */
  public String getToken() {
    return token;
  }

  /**
   * Setter.
   *
   * @param token String
   */
  public void setToken(String token) {
    this.token = token;
  }

  /**
   * Getter.
   *
   * @return String
   */
  public String getResource() {
    return resource;
  }

  /**
   * Setter.
   *
   * @param resource String
   */
  public void setResource(String resource) {
    this.resource = resource;
  }

  /**
   * Getter.
   *
   * @return String
   */
  public String getAction() {
    return action;
  }

  /**
   * Setter.
   *
   * @param action string
   */
  public void setAction(String action) {
    this.action = action;
  }

  /**
   * To String method.
   *
   * @return String
   */
  @Override
  public String toString() {
    return "VerifyBody{"
        + "token='" + token + '\''
        + ", resource='" + resource + '\''
        + ", action='" + action + '\''
        + '}';
  }
}
