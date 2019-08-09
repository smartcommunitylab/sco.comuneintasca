package it.smartcommunitylab.comuneintasca.connector;

public class SourceEntry {
	private String type;
	private String classifier;
	private String url;
	private String subscriptionId;
	private String serviceId;
	private String methodName;
	private boolean autoPublish;
	private String imagePath;
	
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
	
	
}