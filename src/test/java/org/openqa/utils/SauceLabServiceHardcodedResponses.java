package org.openqa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.openqa.services.SauceLabServiceImpl;

public class SauceLabServiceHardcodedResponses extends SauceLabServiceImpl {

  private String statusRepsonse;
  private String browsersResponse;


  public SauceLabServiceHardcodedResponses() {
    statusRepsonse = "{\"error\": \"Sorry, Sauce Labs status not yet implemented over REST API\"}";
    browsersResponse = loadFromFile("informationBrowsers.json");


  }

  private String loadFromFile(String resource) {
    InputStream is = this.getClass().getResourceAsStream(resource);
    StringWriter writer = new StringWriter();
    try {
      IOUtils.copy(is, writer);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return writer.toString();
  }



  @Override
  protected String executeCommand(String url) throws JSONException, IOException {
    if (STATUS.equals(url)) {
      return statusRepsonse;
    } else if (BROWSERS.equals(url)) {
      return browsersResponse;
    } else {
      throw new RuntimeException("NI");
    }
  }



}
