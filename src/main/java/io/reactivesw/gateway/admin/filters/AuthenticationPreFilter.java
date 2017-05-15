package io.reactivesw.gateway.admin.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.reactivesw.gateway.admin.config.AuthFilterConfig;
import io.reactivesw.gateway.admin.model.VerifyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * Authentication filter for checkout customer's authorization.
 */
@Component
public class AuthenticationPreFilter extends ZuulFilter {


  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(AuthenticationPreFilter.class);

  /**
   * RestTemplate.
   */
  private transient RestTemplate restTemplate = new RestTemplate();

  /**
   * Authentication service uri.
   */
  @Value("${authentication.service.uri:http://admin-auth/}")
  private transient String authUri;

  /**
   * Auth filter config.
   */
  @Autowired
  private AuthFilterConfig config;

  /**
   * Filter type.
   *
   * @return string
   */
  @Override
  public String filterType() {
    // use "pre", so we can check the auth before router to back end services.
    return "pre";
  }

  /**
   * Filter order.
   *
   * @return int
   */
  @Override
  public int filterOrder() {
    return 6;
  }

  /**
   * Check if we need to run this filter for this request.
   *
   * @return boolean
   */
  @Override
  public boolean shouldFilter() {
    RequestContext ctx = getCurrentContext();
    String host = ctx.getRouteHost().getHost();
    HttpServletRequest request = ctx.getRequest();
    String method = request.getMethod();
    LOG.debug("Check for host: {}, method: {}.", host, method);
    boolean shouldFilter = true;
    if (config.getHosts().contains(host) | method.equals("OPTIONS")) {
      LOG.debug("Ignore host: {}.", host);
      shouldFilter = true;
    }
    return shouldFilter;
  }

  /**
   * Run function.
   *
   * @return always return null
   */
  @Override
  public Object run() {
    RequestContext ctx = getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    String token = request.getHeader("authorization");
    VerifyResult verifyResult = checkAuthentication(token, ctx.getRouteHost().getHost(),
        request.getMethod());
    if (verifyResult != null && verifyResult.isLogin() && verifyResult.isHashPermission()) {
      // if true, then set the customerId to header
      ctx.addZuulRequestHeader("adminId", verifyResult.getAdminId());
      LOG.info("Exit. check auth success.");
    } else {
      // stop routing and return auth failed.
      ctx.unset();
      if (!verifyResult.isLogin()) {
        ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
      } else if (verifyResult.isHashPermission()) {
        ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
      }
      LOG.info("Exit. check auth failed.");
    }
    return null;
  }


  /**
   * Check the auth status
   *
   * @param tokenString String
   * @return the customer id
   */
  public VerifyResult checkAuthentication(String tokenString, String path, String action) {
    LOG.debug("Enter. token: {}", tokenString);

    try {
      String token = tokenString.substring(7);
      String uri = authUri + "status?token=" + token;
      LOG.debug("AuthUri: {}", uri);

      VerifyResult result = restTemplate.getForObject(uri, VerifyResult.class);

      LOG.debug("Exit. verifyResult: {}", result);
      return result;
    } catch (RestClientException | NullPointerException ex) {
      LOG.debug("Get customerId from authentication service failed.", ex);
      return null;
    }
  }
}
