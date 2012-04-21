package org.openqa;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class SauceLabCapabilities {


  public static final  String NAME = "selenium_name";
  public static final   String SHORT_VERSION = "short_version";
  public static final   String LONG_NAME= "long_name";
  public static final   String LONG_VERSION= "long_version";
  public static final   String PREFERRED_VERSION= "preferred_version";
  public static final   String OS= "os";
  private Map<String, Object> map = new HashMap<String, Object>();

  public SauceLabCapabilities(String rawJson) throws JSONException {
    this(new JSONObject(rawJson));
  }


  public SauceLabCapabilities(JSONObject raw) throws JSONException {
   copy(NAME, raw);
   copy(SHORT_VERSION, raw);
   copy(LONG_VERSION, raw);
   copy(LONG_NAME, raw);
   copy(PREFERRED_VERSION, raw);
   copy(OS, raw);
  }
  
  public String getName() {
    return get(NAME);
  }

  public String getShortVersion() {
    return get(SHORT_VERSION);
  }

  public String getLongName() {
    return get(LONG_NAME);
  }

  public String getLongVersion() {
    return get(LONG_VERSION);
  }

  public String getPreferredVersion() {
    return get(PREFERRED_VERSION);
  }

  public String getOs() {
    return get(OS);
  }

  @Override
  public String toString() {
    return getName() + " "+getShortVersion()+" on "+getOs();
  }


  public Map<String, Object> asMap() {
    return map;
  }
  
  private String get(String key){
    return (String)map.get(key);
  }
  
  private void copy(String key,JSONObject raw) throws JSONException{
    set(key, raw.getString(key));
  }
  private void set(String key,String value){
    map.put(key, value);
  }
}
