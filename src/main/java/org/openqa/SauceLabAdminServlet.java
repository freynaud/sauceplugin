package org.openqa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.common.SeleniumProtocol;
import org.openqa.grid.internal.BaseRemoteProxy;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.TestSlot;
import org.openqa.grid.web.servlet.RegistryBasedServlet;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.services.SauceLabRestAPIException;
import org.openqa.services.SauceLabService;
import org.openqa.utils.SauceLabServiceHardcodedResponses;

public class SauceLabAdminServlet extends RegistryBasedServlet {

  private static final String UPDATE_BROWSERS = "updateSupportedBrowsers";
  private SauceLabService service = new SauceLabServiceHardcodedResponses();
  private final List<SauceLabCapabilities> all;

  public SauceLabAdminServlet() throws SauceLabRestAPIException {
    this(null);
  }


  public SauceLabAdminServlet(Registry registry) throws SauceLabRestAPIException {
    super(registry);
    all = service.getBrowsers();
  }


  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    String id = req.getParameter("id");
    SauceLabRemoteProxy p = getProxy(id);
    if (req.getPathInfo().endsWith(UPDATE_BROWSERS)) {
      updateBrowsers(req, resp, p);
      resp.sendRedirect("/grid/console");
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {

    String id = req.getParameter("id");
    SauceLabRemoteProxy p = getProxy(id);

    if (req.getPathInfo().endsWith("/admin")) {
      String page = renderAdminPage(id);
      resp.getWriter().print(page);
      resp.getWriter().close();
      return;
    }

    
    String state = req.getParameter("state");
    if ("up".equals(state)) {
      p.setMarkUp(true);
    } else {
      p.setMarkUp(false);
    }
    resp.sendRedirect("/grid/console");
  }

  private void updateBrowsers(HttpServletRequest req, HttpServletResponse resp,
      SauceLabRemoteProxy proxy) {
    String[] supported = req.getParameterValues("supportedCapabilities");
    List<SauceLabCapabilities> caps = new ArrayList<SauceLabCapabilities>();
    for (String index : supported) {
      int i = Integer.parseInt(index);
      caps.add(all.get(i));
    }
    getRegistry().removeIfPresent(proxy);
    
    
    RegistrationRequest sauceRequest = proxy.getOriginalRegistrationRequest();
    // re-create the test slots with the new capabilities.
    sauceRequest.getCapabilities().clear();
    
    for (SauceLabCapabilities cap : caps) {
      DesiredCapabilities c = new DesiredCapabilities(cap.asMap());
      c.setCapability(RegistrationRequest.MAX_INSTANCES, proxy.getMaxNumberOfConcurrentTestSessions());
      sauceRequest.getCapabilities().add(c);
    }
    
    SauceLabRemoteProxy newProxy = new SauceLabRemoteProxy(sauceRequest, getRegistry());
    getRegistry().add(newProxy);


  }


  private List<TestSlot> createSlots(SauceLabRemoteProxy proxy, SauceLabCapabilities cap) {
    List<TestSlot> slots = new ArrayList<TestSlot>();
    for (int i = 0; i < proxy.getMaxNumberOfConcurrentTestSessions(); i++) {
      slots.add(new TestSlot(proxy, SeleniumProtocol.WebDriver,
          SauceLabRemoteProxy.SAUCE_END_POINT, cap.asMap()));
    }
    return slots;
  }


  private SauceLabRemoteProxy getProxy(String id) {
    return (SauceLabRemoteProxy) getRegistry().getProxyById(id);
  }

  private String renderAdminPage(String id) {

    StringBuilder b = new StringBuilder();

    try {
      b.append("<html>");

      b.append("<form action='/grid/admin/SauceLabAdminServlet/" + UPDATE_BROWSERS
          + "' method='POST'>");
      b.append("<ul>");
      for (int i = 0; i < all.size(); i++) {
        SauceLabCapabilities cap = all.get(i);
        b.append("<li><input type='checkbox' name='supportedCapabilities' value='" + i + "'>");
        b.append(cap);
        b.append("</input></li>");
      }
      b.append("</ul>");
      b.append("<input type='hidden' name='id' value='" + id + "' />");
      b.append("<input type='submit' value='save' />");

      b.append("</form>");

      b.append("</html>");
    } catch (Exception e) {
      b.append("Error : " + e.getMessage());
    }

    return b.toString();

  }

}
