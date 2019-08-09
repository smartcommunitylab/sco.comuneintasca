package it.smartcommunitylab.comuneintasca.connector.processor;

import it.smartcommunitylab.comuneintasca.core.model.EventObject;
import it.smartcommunitylab.comuneintasca.core.model.TypeConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

public class EventMappingDescriptor extends MappingDescriptor {

	public EventMappingDescriptor() {
		super("event", TypeConstants.TYPE_EVENT, EventObject.class, "tipo_evento", "tipo_eventi_manifestazioni");
	}

	@Override
	public String extractClassification(List<Map<String, String>> classifications) throws BadDataException {
		List<String> list = new ArrayList<String>();
		for (Map<String, String> classifics : classifications) {
			for (String key : classifics.keySet()) {
				if (!getQueryKeys().contains(key)) {
					throw new BadDataException("Unknown query key: "+key +" for type "+getRemoteType());
				}
				String val = classifics.get(key);
				// HARD-CODED HANDLING OF MANIFESTAZIONI
				if ("tipo_eventi_manifestazioni".equals(key)) {
					if (val.equalsIgnoreCase("Manifestazione")) return "_complex";
					else continue;
				}
				if (val != null) {
					list.add(val);
				}
			}
		}
		if (list.isEmpty()) return "";
		return StringUtils.collectionToDelimitedString(list, ";");
	}

	
	
}
