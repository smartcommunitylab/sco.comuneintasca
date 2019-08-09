package it.smartcommunitylab.comuneintasca.connector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

	private String id;
	private List<SourceEntry> sources;

	private Map<TypeClassifier, SourceEntry> entryMap = null;

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
		return sources;
	}

	public void setSources(List<SourceEntry> sources) {
		this.sources = sources;
	}
}
