package com.pubmatic.action;

import org.springframework.util.ErrorHandler;

public abstract class BaseCreativeAction implements CreativeAction{
	
    private String name;
    private String runType;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the runType
	 */
	public String getRunType() {
		return runType;
	}
	/**
	 * @param runType the runType to set
	 */
	public void setRunType(String runType) {
		this.runType = runType;
	}



}
