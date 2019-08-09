package it.smartcommunitylab.comuneintasca.connector.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import it.smartcommunitylab.comuneintasca.core.model.BaseCITObject;

public class MappingDescriptor {
	private String remoteType;
	private String localType;
	private Class<? extends BaseCITObject> localClass;
	private Set<String> queryKeys = new HashSet<String>();

	public MappingDescriptor(String remoteType, String localType, Class<? extends BaseCITObject> localClass, String ... queryKeys) {
		super();
		this.remoteType = remoteType;
		this.localType = localType;
		this.localClass = localClass;
		if (queryKeys != null && queryKeys.length > 0) {
			this.queryKeys.addAll(Arrays.asList(queryKeys));
		}
	}
	
	public String getRemoteType() {
		return remoteType;
	}
	public void setRemoteType(String remoteType) {
		this.remoteType = remoteType;
	}
	public String getLocalType() {
		return localType;
	}
	public void setLocalType(String localType) {
		this.localType = localType;
	}
	public Class<? extends BaseCITObject> getLocalClass() {
		return localClass;
	}
	public void setLocalClass(Class<? extends BaseCITObject> localClass) {
		this.localClass = localClass;
	}
	public Set<String> getQueryKeys() {
		return queryKeys;
	}
	public void setQueryKeys(Set<String> queryKeys) {
		this.queryKeys = queryKeys;
	}

	public String extractClassification(List<Map<String, String>> classifications) throws BadDataException {
		List<String> list = new ArrayList<String>();
		for (Map<String, String> classifics : classifications) {
			for (String key : classifics.keySet()) {
				if (!queryKeys.contains(key)) {
					throw new BadDataException("Unknown query key: "+key +" for type "+remoteType);
				}
				String val = classifics.get(key);
				if (val != null) {
					list.add(val);
				}
			}
		}
		if (list.isEmpty()) return "";
		return StringUtils.collectionToDelimitedString(list, ";");
	}
}
