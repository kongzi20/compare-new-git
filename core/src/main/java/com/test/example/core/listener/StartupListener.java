package com.test.example.core.listener;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.DynamicServerConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class StartupListener  implements ServletContextListener {

	static Logger logger = LoggerFactory.getLogger(StartupListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.debug("Initializing context...");
		ServletContext context = event.getServletContext();
		setupContext(context);
		logger.debug("initialization complete");
	}

	/**
	 * 加载常量等信息 放入context中.
	 * 
	 * @param context
	 *            The servlet context
	 */
	private static void setupContext(ServletContext context) {

		WebApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(context);

		DynamicServerConfig app = (DynamicServerConfig) ctx
				.getBean("propertyPlaceholderConfigurer");
		try {
			Properties props = app.mergeProperties();
			if (props != null) {
				for (Iterator<Object> iterator = props.keySet().iterator(); iterator
						.hasNext();) {
					String name = (String) iterator.next();
					context.setAttribute(
							name,
							props.getProperty(name) == null ? "" : props
									.getProperty(name));
				}
			}
		} catch (IOException e) {
			logger.error("startup properties error :", e);
		}

	}

}
