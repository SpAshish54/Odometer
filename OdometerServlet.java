package com.learning.hello;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;


import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import controller.OdometerController;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/odometer")
public class OdometerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private OdometerController Odo;
	private JakartaServletWebApplication application;
	private TemplateEngine templateEngine;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		Odo = new OdometerController(5);
		application = JakartaServletWebApplication.buildApplication(getServletContext());
		final WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// hlc.setReading(0);
		final IWebExchange webExchange = this.application.buildExchange(req, resp);
		final WebContext ctx = new WebContext(webExchange);
		var out = resp.getWriter();

		ctx.setVariable("reading", Odo.getCurrentReading());
		templateEngine.process("Odometer", ctx, out);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		if (action == null) {
			Odo.reset();
		} else if (action.equals("next") ) {
			Odo.getNextReading();
		} else if (action.equals("prev")) {
			Odo.getPreviousReading();
		}


		doGet(req, resp);

	}
}
