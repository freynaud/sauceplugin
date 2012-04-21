package org.openqa;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ModelTests {


  private String stringCapabilities = "{\"selenium_name\" : \"iexplore\","
      + "  \"short_version\" : \"9\"," + "\"long_name\" : \"Internet Explorer\","
      + "\"long_version\" : \"9.0.8112.16421.\"," + " \"preferred_version\" : \"9\","
      + " \"os\" : \"Windows 2008\"}";

  private String stringCapabilitiesInvalid = "{\"selenium_name2\" : \"iexplore\","
      + "  \"short_version\" : \"9\"," + "\"long_name\" : \"Internet Explorer\","
      + "\"long_version\" : \"9.0.8112.16421.\"," + " \"preferred_version\" : \"9\","
      + " \"os\" : \"Windows 2008\"}";


  private JSONObject jsonCapabilities;


  @BeforeClass
  public void setup() throws JSONException {
    jsonCapabilities = new JSONObject(stringCapabilities);
  }


  @Test
  public void sauceLabCapability() throws JSONException {
    SauceLabCapabilities slc = new SauceLabCapabilities(stringCapabilities);
    Assert.assertEquals(slc.getLongName(), "Internet Explorer");
    Assert.assertEquals(slc.getLongVersion(), "9.0.8112.16421.");
  }

  @Test
  public void sauceLabCapabilityJson() throws JSONException {
    SauceLabCapabilities slc = new SauceLabCapabilities(jsonCapabilities);
    Assert.assertEquals(slc.getLongName(), "Internet Explorer");
    Assert.assertEquals(slc.getLongVersion(), "9.0.8112.16421.");
  }

  @Test(expectedExceptions = {JSONException.class})
  public void sauceLabCapabilityInvalid() throws JSONException {
    new SauceLabCapabilities(stringCapabilitiesInvalid);
  }

}
