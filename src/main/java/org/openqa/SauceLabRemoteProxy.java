package org.openqa;

import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.selenium.proxy.DefaultRemoteProxy;

public class SauceLabRemoteProxy extends DefaultRemoteProxy {

  public static final String SAUCE_ONE = "sauce";
  private boolean isSLOne = false;

  public SauceLabRemoteProxy(RegistrationRequest req, Registry registry) {
    super(req, registry);
    Object b = req.getConfiguration().get(SAUCE_ONE);
    if (b != null) {
      isSLOne = (Boolean) b;
    }
  }

  @Override
  public int compareTo(RemoteProxy o) {
    if (!(o instanceof SauceLabRemoteProxy)) {
      throw new RuntimeException("cannot mix saucelab and not saucelab ones");
    } else {
      if (this.isSLOne) {
        return -1;
      } else {
        return super.compareTo(o);
      }
    }
  }
}
