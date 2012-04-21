package org.openqa;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrowsersCache {

  private Map<String, SauceLabCapabilities> map = new HashMap<String, SauceLabCapabilities>();

  public BrowsersCache(List<SauceLabCapabilities> caps) {
    for (SauceLabCapabilities cap : caps) {
      map.put(cap.getMD5(), cap);
    }
  }


  public SauceLabCapabilities get(String md5) {
    SauceLabCapabilities res = map.get(md5);
    if (res == null) {
      throw new RuntimeException("cap missing from cache");
    } else {
      return res;
    }
  }
  
  public Collection<SauceLabCapabilities> getAllBrowsers(){
    return map.values();
  }

}
