package org.openqa.services;

import java.util.List;

import org.openqa.SauceLabCapabilities;

public interface SauceLabService {

  public boolean isSauceLabUp() throws SauceLabRestAPIException;
  public List<SauceLabCapabilities> getBrowsers() throws SauceLabRestAPIException;
    
}
