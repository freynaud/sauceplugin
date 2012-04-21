package org.openqa.services;

public class SauceLabRestAPIException extends Exception {


  public SauceLabRestAPIException(Throwable t) {
    super(t);
  }

  public SauceLabRestAPIException(String msg, Exception e) {
   super(msg, e);
  }
}
