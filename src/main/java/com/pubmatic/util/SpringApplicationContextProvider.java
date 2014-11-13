package com.pubmatic.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
 
public class SpringApplicationContextProvider implements ApplicationContextAware {
 
	private static ApplicationContext appContext;
 
	// Private constructor prevents instantiation from other classes
        private SpringApplicationContextProvider() {}
 
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		appContext = applicationContext;
 
	}
 
	public static Object getBean(String beanName) {
		return appContext.getBean(beanName);
	}
 
}
