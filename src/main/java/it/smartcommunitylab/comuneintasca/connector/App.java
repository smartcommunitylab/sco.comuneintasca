package it.smartcommunitylab.comuneintasca.connector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.smartcommunitylab.comuneintasca.core.model.MenuItem;

public class App {

	private String id;
	private List<SourceEntry> sources;

	private String highlightsId, objectUrl, imagePath;
	
	private Map<TypeClassifier, SourceEntry> entryMap = null;

	private List<MenuItem> highlights;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SourceEntry findEntry(String serviceId, String methodName) {
		for (SourceEntry entry : sources) {
			if (entry.getServiceId().equals(serviceId)			 &&
				entry.getMethodName().equals(methodName)) return entry;
		}
		return null;
	}

	public SourceEntry findEntryByType(String type, String classifier) {
		if (entryMap == null) {
			entryMap = new HashMap<TypeClassifier, SourceEntry>(); 
			for (SourceEntry s : sources) {
				entryMap.put(new TypeClassifier(s.getType(), s.getClassifier()), s);
			}

		}
		return entryMap.get(new TypeClassifier(type, classifier));
	}
	
	public List<SourceEntry> getSources() {
		if (sources != null) {
			sources.forEach(s -> {
				if (s.getImagePath() == null) s.setImagePath(imagePath);
			});
		}
		return sources;
	}

	public void setSources(List<SourceEntry> sources) {
		this.sources = sources;
	}

	/**
	 * @return the highlightsId
	 */
	public String getHighlightsId() {
		return highlightsId;
	}

	/**
	 * @param highlightsId the highlightsId to set
	 */
	public void setHighlightsId(String highlightsId) {
		this.highlightsId = highlightsId;
	}

	/**
	 * @return the objectUrl
	 */
	public String getObjectUrl() {
		return objectUrl;
	}

	/**
	 * @param objectUrl the objectUrl to set
	 */
	public void setObjectUrl(String objectUrl) {
		this.objectUrl = objectUrl;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * @return the highlights
	 */
	public List<MenuItem> getHighlights() {
		return highlights;
	}

	/**
	 * @param highlights the highlights to set
	 */
	public void setHighlights(List<MenuItem> highlights) {
		this.highlights = highlights;
	}
	
}
