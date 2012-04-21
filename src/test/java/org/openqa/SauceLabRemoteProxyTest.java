package org.openqa;

import java.net.URL;
import java.util.Arrays;

import org.openqa.grid.common.GridRole;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.utils.GridHubConfiguration;
import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.utils.TestHelper;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups={"slow"})
public class SauceLabRemoteProxyTest {
  private Hub hub;



  @BeforeClass(alwaysRun = false)
  public void prepare() throws Exception {

    hub = TestHelper.getHub();

    SelfRegisteringRemote remote = TestHelper.getRemoteWithoutCapabilities(hub.getUrl(), GridRole.NODE);
    remote.addBrowser(DesiredCapabilities.firefox(), 2);
    remote.startRemoteServer();
    remote.getConfiguration().put(RegistrationRequest.TIME_OUT, -1);
    remote.getConfiguration()
        .put(RegistrationRequest.PROXY_CLASS, "org.openqa.SauceLabRemoteProxy");
    remote.sendRegistrationRequest();
    
    registerSauceLabProxy();

  }

  private void registerSauceLabProxy() throws Exception {
    SelfRegisteringRemote remote = TestHelper.getRemoteWithoutCapabilities(hub.getUrl(), GridRole.NODE);
    remote.addBrowser(DesiredCapabilities.firefox(), 2);
    

    remote.getConfiguration().put(RegistrationRequest.TIME_OUT, -1);
    remote.getConfiguration()
        .put(RegistrationRequest.PROXY_CLASS, "org.openqa.SauceLabRemoteProxy");
    remote.getConfiguration().put(SauceLabRemoteProxy.SAUCE_ONE, true);
    System.out.println(remote.getConfiguration());
    remote.sendRegistrationRequest();

  }

  @Test(invocationCount=3,threadPoolSize=3)
  public void firefoxOnWebDriver() throws Exception {
    WebDriver driver = null;
    try {
      DesiredCapabilities ff = DesiredCapabilities.firefox();
      driver = new RemoteWebDriver(new URL(hub.getUrl() + "/wd/hub"), ff);
      driver.get(hub.getUrl() + "/grid/console");
      Thread.sleep(5000);
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

 
}
