package org.openqa;

import java.util.Map;

import org.json.JSONException;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.internal.TestSession;
import org.openqa.grid.internal.TestSlot;
import org.openqa.grid.internal.utils.HtmlRenderer;
import org.openqa.grid.selenium.proxy.DefaultRemoteProxy;

public class SauceLabRemoteProxy extends DefaultRemoteProxy {

  public static String SAUCE_END_POINT = "http://sauce/wd/hub";
  private volatile boolean markUp = false;
  public static final String SAUCE_ONE = "sauce";
  private boolean isSLOne = false;

  public boolean isTheSauceLabProxy() {
    return isSLOne;
  }

  public SauceLabRemoteProxy(RegistrationRequest req, Registry registry) {
    super(req, registry);
    Object b = req.getConfiguration().get(SAUCE_ONE);
    if (b != null) {
      isSLOne = (Boolean) b;
    }
  }

  @Override
  public TestSession getNewSession(Map<String, Object> requestedCapability) {
    if ((isSLOne && markUp) || !isSLOne) {
      return super.getNewSession(requestedCapability);
    } else {
      return null;
    }
  }

  @Override
  public HtmlRenderer getHtmlRender() {
    return new SauceLabRenderer(this);
  }

  @Override
  public int compareTo(RemoteProxy o) {
    if (!(o instanceof SauceLabRemoteProxy)) {
      throw new RuntimeException("cannot mix saucelab and not saucelab ones");
    } else {
      SauceLabRemoteProxy other = (SauceLabRemoteProxy) o;

      if (this.isSLOne) {
        System.out.println("return -1, sslone");
        return 1;
      } else if (other.isSLOne) {
        return -1;
      } else {
        int i = super.compareTo(o);
        System.out.println("return normal " + i);
        return i;
      }
    }
  }

  public boolean contains(SauceLabCapabilities capabilities) throws JSONException {
    for (TestSlot slot : getTestSlots()) {
      SauceLabCapabilities slc = new SauceLabCapabilities(slot.getCapabilities());
      if (slc.equals(capabilities)) {
        return true;
      }
    }
    return false;
  }

  public synchronized boolean isMarkUp() {
    return markUp;
  }

  public synchronized void setMarkUp(boolean markUp) {
    this.markUp = markUp;
  }


}
