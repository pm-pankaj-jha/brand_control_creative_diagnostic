package com.pubmatic.adda.domain;

import java.sql.Date;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonIgnoreProperties(ignoreUnknown = true) 
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL) 
public class CreativeInfo {
	
	private String id;
	private String url;
	private String geo;
	private String image;
	private String thumbnail;
	private String dom;
	private String netlog;
	private String userAgent;
	private String proxy;
	@JsonProperty("har")
	private Map har;
	private String ocr;
	private String isStreamingMedia;
	private String isAutoAudio;
	private String mediaDuration;
	private String clickThroughURL;
	private String primaryClassificationType;
	private String secondaryClassificationType;
	private String timeToServe;
	private String noOfRedirects;
	private Date timeOfRequest;
	private String daisyChain;
	private String message= "Processed Successfully";
	private String action;
	
	
	/**
	 * @return the proxy
	 */
	public String getProxy() {
		return proxy;
	}
	/**
	 * @param proxy the proxy to set
	 */
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
		return userAgent;
	}
	/**
	 * @param userAgent the userAgent to set
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	/**
	 * @return the isAutoAudio
	 */
	public String getIsAutoAudio() {
		return isAutoAudio;
	}
	/**
	 * @return the thumbnail
	 */
	public String getThumbnail() {
		return thumbnail;
	}
	/**
	 * @param thumbnail the thumbnail to set
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	/**
	 * @param isAutoAudio the isAutoAudio to set
	 */
	public void setIsAutoAudio(String isAutoAudio) {
		this.isAutoAudio = isAutoAudio;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CreativeInfo [id=" + id + ", url=" + url + ", geo=" + geo
				+ ", image=" + image + ", thumbnail=" + thumbnail + ", dom="
				+ dom + ", netlog=" + netlog + ", har=" + har + ", ocr=" + ocr
				+ ", isStreamingMedia=" + isStreamingMedia + ", isAutoAudio="
				+ isAutoAudio + ", mediaDuration=" + mediaDuration
				+ ", clickThroughURL=" + clickThroughURL
				+ ", primaryClassificationType=" + primaryClassificationType
				+ ", secondaryClassificationType="
				+ secondaryClassificationType + ", timeToServe=" + timeToServe
				+ ", noOfRedirects=" + noOfRedirects + ", timeOfRequest="
				+ timeOfRequest + ", daisyChain=" + daisyChain + ", message="
				+ message + ", action=" + action + "]";
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return the geo
	 */
	public String getGeo() {
		return geo;
	}
	/**
	 * @param geo the geo to set
	 */
	public void setGeo(String geo) {
		this.geo = geo;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the daisyChain
	 */
	public String getDaisyChain() {
		return daisyChain;
	}
	/**
	 * @param daisyChain the daisyChain to set
	 */
	public void setDaisyChain(String daisyChain) {
		this.daisyChain = daisyChain;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}
	/**
	 * @return the dom
	 */
	public String getDom() {
		return dom;
	}
	/**
	 * @param dom the dom to set
	 */
	public void setDom(String dom) {
		this.dom = dom;
	}
	/**
	 * @return the netlog
	 */
	public String getNetlog() {
		return netlog;
	}
	/**
	 * @param netlog the netlog to set
	 */
	public void setNetlog(String netlog) {
		this.netlog = netlog;
	}
	
	/**
	 * @return the har
	 */
	public Map getHar() {
		return har;
	}
	/**
	 * @param har the har to set
	 */
	public void setHar(Map har) {
		this.har = har;
	}
	/**
	 * @return the ocr
	 */
	public String getOcr() {
		return ocr;
	}
	/**
	 * @param ocr the ocr to set
	 */
	public void setOcr(String ocr) {
		this.ocr = ocr;
	}
	/**
	 * @return the isStreamingMedia
	 */
	public String getIsStreamingMedia() {
		return isStreamingMedia;
	}
	/**
	 * @param isStreamingMedia the isStreamingMedia to set
	 */
	public void setIsStreamingMedia(String isStreamingMedia) {
		this.isStreamingMedia = isStreamingMedia;
	}
	/**
	 * @return the mediaDuration
	 */
	public String getMediaDuration() {
		return mediaDuration;
	}
	/**
	 * @param mediaDuration the mediaDuration to set
	 */
	public void setMediaDuration(String mediaDuration) {
		this.mediaDuration = mediaDuration;
	}
	/**
	 * @return the clickThroughURL
	 */
	public String getClickThroughURL() {
		return clickThroughURL;
	}
	/**
	 * @param clickThroughURL the clickThroughURL to set
	 */
	public void setClickThroughURL(String clickThroughURL) {
		this.clickThroughURL = clickThroughURL;
	}
	/**
	 * @return the primaryClassificationType
	 */
	public String getPrimaryClassificationType() {
		return primaryClassificationType;
	}
	/**
	 * @param primaryClassificationType the primaryClassificationType to set
	 */
	public void setPrimaryClassificationType(String primaryClassificationType) {
		this.primaryClassificationType = primaryClassificationType;
	}
	/**
	 * @return the secondaryClassificationType
	 */
	public String getSecondaryClassificationType() {
		return secondaryClassificationType;
	}
	/**
	 * @param secondaryClassificationType the secondaryClassificationType to set
	 */
	public void setSecondaryClassificationType(String secondaryClassificationType) {
		this.secondaryClassificationType = secondaryClassificationType;
	}
	/**
	 * @return the timeToServe
	 */
	public String getTimeToServe() {
		return timeToServe;
	}
	/**
	 * @param timeToServe the timeToServe to set
	 */
	public void setTimeToServe(String timeToServe) {
		this.timeToServe = timeToServe;
	}
	/**
	 * @return the noOfRedirects
	 */
	public String getNoOfRedirects() {
		return noOfRedirects;
	}
	/**
	 * @param noOfRedirects the noOfRedirects to set
	 */
	public void setNoOfRedirects(String noOfRedirects) {
		this.noOfRedirects = noOfRedirects;
	}
	/**
	 * @return the timeOfRequest
	 */
	public Date getTimeOfRequest() {
		return timeOfRequest;
	}
	/**
	 * @param timeOfRequest the timeOfRequest to set
	 */
	public void setTimeOfRequest(Date timeOfRequest) {
		this.timeOfRequest = timeOfRequest;
	}

	

}
