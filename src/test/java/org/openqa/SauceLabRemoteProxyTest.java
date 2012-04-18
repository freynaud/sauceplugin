package org.openqa;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.grid.common.GridRole;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.utils.GridHubConfiguration;
import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SauceLabRemoteProxyTest {
  private Hub hub;



  @BeforeClass(alwaysRun = false)
  public void prepare() throws Exception {

    hub = getHub();

    SelfRegisteringRemote remote = getRemoteWithoutCapabilities(hub.getUrl(), GridRole.NODE);
    remote.addBrowser(DesiredCapabilities.firefox(), 5);
    remote.startRemoteServer();
    remote.getConfiguration().put(RegistrationRequest.TIME_OUT, -1);
    remote.getConfiguration()
        .put(RegistrationRequest.PROXY_CLASS, "org.openqa.SauceLabRemoteProxy");
    remote.sendRegistrationRequest();
    registerSauceLabProxy();

  }

  private void registerSauceLabProxy() throws Exception {
    SelfRegisteringRemote remote = getRemoteWithoutCapabilities(hub.getUrl(), GridRole.NODE);
    remote.addBrowser(DesiredCapabilities.firefox(), 1);
    remote.startRemoteServer();

    remote.getConfiguration().put(RegistrationRequest.TIME_OUT, -1);
    remote.getConfiguration()
        .put(RegistrationRequest.PROXY_CLASS, "org.openqa.SauceLabRemoteProxy");
    remote.getConfiguration().put(SauceLabRemoteProxy.SAUCE_ONE, true);
    remote.sendRegistrationRequest();

  }

  @Test
  public void firefoxOnWebDriver() throws MalformedURLException {
    WebDriver driver = null;
    try {
      DesiredCapabilities ff = DesiredCapabilities.firefox();
      driver = new RemoteWebDriver(new URL(hub.getUrl() + "/wd/hub"), ff);
      driver.get(hub.getUrl() + "/grid/console");
      Assert.assertEquals(driver.getTitle(), "Grid overview");
    } finally {
      if (driver != null) {
        driver.quit();
      }
    }
  }



  @AfterClass(alwaysRun = false)
  public void stop() throws Exception {
    hub.stop();
  }

  private Hub getHub() throws Exception {
    GridHubConfiguration config = new GridHubConfiguration();
    config.setPort(PortProber.findFreePort());
    Hub hub = new Hub(config);
    hub.start();
    return hub;
  }

  public static SelfRegisteringRemote getRemoteWithoutCapabilities(URL hub, GridRole role) {
    RegistrationRequest req = RegistrationRequest.build("-role", "node");

    req.getConfiguration().put(RegistrationRequest.PORT, PortProber.findFreePort());

    // some values have to be computed again after changing internals.
    String url =
        "http://" + req.getConfiguration().get(RegistrationRequest.HOST) + ":"
            + req.getConfiguration().get(RegistrationRequest.PORT);
    req.getConfiguration().put(RegistrationRequest.REMOTE_HOST, url);

    req.getConfiguration().put(RegistrationRequest.HUB_HOST, hub.getHost());
    req.getConfiguration().put(RegistrationRequest.HUB_PORT, hub.getPort());

    SelfRegisteringRemote remote = new SelfRegisteringRemote(req);
    remote.deleteAllBrowsers();
    return remote;
  }
}
