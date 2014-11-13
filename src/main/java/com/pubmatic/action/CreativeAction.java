package com.pubmatic.action;

import java.util.List;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.util.ErrorHandler;

import com.pubmatic.adda.domain.CreativeInfo;

public interface CreativeAction {
	
	 public void preProcess() throws Exception;
	 public CreativeInfo execute(CreativeInfo context) throws Exception;
	 //public List <CreativeInfo> execute(List <CreativeInfo> creativeList) throws Exception;	 
	 public void postProcess() throws Exception;
	

}
