package it.smartcommunitylab.comuneintasca.core.model;

import it.smartcommunitylab.comuneintasca.storage.data.BasicObject;

@SuppressWarnings("serial")
public class AppObject extends BasicObject {

	private String appId;
	private Long lastModified = null;
	private String localId;

	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public Long getLastModified() {
		return lastModified;
	}
	public void setLastModified(Long lastModified) {
		this.lastModified = lastModified;
	}
	public String getLocalId() {
		return localId;
	}
	public void setLocalId(String localId) {
		this.localId = localId;
	}
	
	public boolean different(AppObject obj) {
		if (obj == null) return true;
		if (lastModified == null) return obj.getLastModified() != null;
		
		return !lastModified.equals(obj.getLastModified());
	}
}
