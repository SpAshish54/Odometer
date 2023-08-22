package com.learning.hello;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;

import java.util.Random;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import controller.HiLoController;
import controller.OdometerController;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/odometer")
public class OdometerServlet extends HttpServlet{
  
  private static final long serialVersionUID = 1L;
  private OdometerController Odo;
  private JakartaServletWebApplication application;
  private TemplateEngine templateEngine;
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    Odo = new OdometerController(0);
    application = JakartaServletWebApplication.buildApplication(getServletContext());
    final WebApplicationTemplateResolver templateResolver = 
        new WebApplicationTemplateResolver(application);
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setPrefix("/WEB-INF/templates/");
    templateResolver.setSuffix(".html");
    templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);
  }
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //hlc.setReading(0);
    var out = resp.getWriter();
    final IWebExchange webExchange = 
        this.application.buildExchange(req, resp);
    final WebContext ctx = new WebContext(webExchange);
    ctx.setVariable("current_reading", Odo.getCurrentReading());
    ctx.setVariable("previous_reading", Odo.getPreviousReading());
    ctx.setVariable("next_reading", Odo.getPreviousReading());
    templateEngine.process("Odometer", ctx, out);
    
    
    
  }
  
  @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String action = req.getParameter("action");

    String inputNumber = req.getParameter("inputNumber");

    OdometerController odometer = getOdometerFromSession(req.getSession());

    if (odometer == null) {

        odometer = new OdometerController(0); // Initialize with a default value

        req.getSession().setAttribute("odometer", odometer);

    }
    if (inputNumber != null && !inputNumber.isEmpty()) {

        int newReading = Integer.parseInt(inputNumber);

        odometer.setReading(newReading); // Update the odometer reading

    } else if ("right".equals(action)) {

        odometer.getNextReading(); // Move to the next reading

    } else if ("left".equals(action)) {

        odometer.getPreviousReading(); // Move to the previous reading

    }

    resp.sendRedirect(req.getContextPath() + "/odometer");

}

 private OdometerController getOdometerFromSession(HttpSession session) {

    return (OdometerController) session.getAttribute("odometer");

 	}
}
