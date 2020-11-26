package it.smartcommunitylab.comuneintasca.connector;

import java.util.LinkedList;
import java.util.List;

import it.smartcommunitylab.comuneintasca.connector.processor.ConfigProcessor.ObjectFilters;

public class SourceEntry {
	private String type;
	private String classifier;
	private String url;
	private String subscriptionId;
	private String serviceId;
	private String methodName;
	private boolean autoPublish;
	private boolean manual;
	private String imagePath;
	private List<String> ids;
	private List<String> classifications;
	
	private ObjectFilters filters;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public String getClassifier() {
		return classifier;
	}
	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public boolean isAutoPublish() {
		return autoPublish;
	}
	public void setAutoPublish(boolean autoPublish) {
		this.autoPublish = autoPublish;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	@Override
	public String toString() {
		return "SourceEntry [serviceId=" + serviceId + ", methodName="
				+ methodName + ", type=" + type + ", classifier=" + classifier
				+ "]";
	}
	public boolean isManual() {
		return manual;
	}
	public void setManual(boolean manual) {
		this.manual = manual;
	}
	/**
	 * @return the ids
	 */
	public List<String> getIds() {
		if (ids == null) ids = new LinkedList<>();
		return ids;
	}
	/**
	 * @param ids the ids to set
	 */
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	/**
	 * @return the classifications
	 */
	public List<String> getClassifications() {
		if (classifications == null) classifications = new LinkedList<>();
		return classifications;
	}
	/**
	 * @param classifications the classifications to set
	 */
	public void setClassifications(List<String> classifications) {
		this.classifications = classifications;
	}
	/**
	 * @return the filters
	 */
	public ObjectFilters getFilters() {
		return filters;
	}
	/**
	 * @param filters the filters to set
	 */
	public void setFilters(ObjectFilters filters) {
		this.filters = filters;
	}

}