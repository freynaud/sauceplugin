package org.openqa.utils;

import java.net.URL;
import java.util.Arrays;

import org.openqa.grid.common.GridRole;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.utils.GridHubConfiguration;
import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.net.PortProber;

public class TestHelper {

  public static Hub getHub(int port) throws Exception {
    GridHubConfiguration config = new GridHubConfiguration();
    // because auto discovery takes ages.
    config.setHost("localhost");
    config.setPort(port);
    config.setServlets(Arrays.asList(new String[] {"org.openqa.SauceLabAdminServlet"}));
    Hub hub = new Hub(config);
    hub.start();
    return hub;
  }


  public static Hub getHub() throws Exception {
    return getHub(PortProber.findFreePort());
  }

  public static SelfRegisteringRemote getRemoteWithoutCapabilities(URL hub, GridRole role) {
    // hardcode host as auto discovery takes ages.
    RegistrationRequest req = RegistrationRequest.build("-role", "node" , "-host", "localhost");

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
