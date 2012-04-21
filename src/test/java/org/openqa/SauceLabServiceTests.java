package org.openqa;

import java.util.List;

import org.openqa.services.SauceLabRestAPIException;
import org.openqa.services.SauceLabService;
import org.openqa.services.SauceLabServiceImpl;
import org.openqa.utils.SauceLabServiceHardcodedResponses;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SauceLabServiceTests {

  private SauceLabService realOne = new SauceLabServiceImpl();
  private SauceLabService mockOne = new SauceLabServiceHardcodedResponses();

  // for now, as status doesn't seem implemented.
  @Test(groups = {"manual"}, expectedExceptions = {SauceLabRestAPIException.class})
  public void testStatusAgainstSauceLab() throws SauceLabRestAPIException {
    boolean up = realOne.isSauceLabUp();
    Assert.assertTrue(up);
  }

  @Test(groups = {"manual"})
  public void testBrowsersAgainstSauceLab() throws SauceLabRestAPIException {
    List<SauceLabCapabilities> caps = realOne.getBrowsers();
    Assert.assertTrue(caps.size() > 50);
  }

  // for now, as status doesn't seem implemented.
  @Test(expectedExceptions = {SauceLabRestAPIException.class})
  public void testStatus() throws SauceLabRestAPIException {
    boolean up = mockOne.isSauceLabUp();
    Assert.assertTrue(up);
  }

  @Test
  public void testBrowsers() throws SauceLabRestAPIException {
    List<SauceLabCapabilities> caps = mockOne.getBrowsers();
    Assert.assertEquals(caps.size(), 112);
    SauceLabCapabilities first = caps.get(0);
    Assert.assertEquals(first.getName(), "iehta");
    Assert.assertEquals(first.getLongVersion(), "9.0.8112.16421.");
  }



}
