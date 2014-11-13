package com.pubmatic.workflow;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.ErrorHandler;

import com.pubmatic.action.CreativeAction;


public abstract class BaseWorkFlow implements InitializingBean, BeanNameAware, BeanFactoryAware,WorkFlow{

	private BeanFactory beanFactory;
    private String beanName;
    private List <CreativeAction > actions;
    private ErrorHandler defaultErrorHandler;
    
    
    public void setBeanName(String beanName) {
        this.beanName = beanName;

    }
    
    
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;

    }
    
public void afterPropertiesSet() throws Exception {
        
        if(!(beanFactory instanceof ListableBeanFactory))
            throw new BeanInitializationException("The workflow processor ["+beanName+"] " +
            		"is not managed by a ListableBeanFactory, please re-deploy using some dirivative of ListableBeanFactory such as" +
            		"ClassPathXmlApplicationContext ");

        if (actions == null || actions.isEmpty())
            throw new UnsatisfiedDependencyException(getBeanDesc(), beanName, "activities",
                                                     "No activities were wired for this workflow");
        
        for (Iterator iter = actions.iterator(); iter.hasNext();) {
        	CreativeAction action = (CreativeAction) iter.next();
            if( !supports(action))
                throw new BeanInitializationException("The workflow processor ["+beanName+"] does " +
                		"not support the activity of type"+action.getClass().getName());
        }

    }

protected String getBeanDesc() {
    return (beanFactory instanceof ConfigurableListableBeanFactory) ?
            ((ConfigurableListableBeanFactory) beanFactory).getBeanDefinition(beanName).getResourceDescription()
            : "Workflow Processor: " + beanName;

}

public void setActions(List actions) {
    this.actions = actions;
}

public void setDefaultErrorHandler(ErrorHandler defaultErrorHandler) {
    this.defaultErrorHandler = defaultErrorHandler;
}


public List getActions() {
    return actions;
}
public String getBeanName() {
    return beanName;
}
public ErrorHandler getDefaultErrorHandler() {
    return defaultErrorHandler;
}
public BeanFactory getBeanFactory() {
    return beanFactory;
}
}
