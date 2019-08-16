/*******************************************************************************
 * Copyright 2012-2014 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package it.smartcommunitylab.comuneintasca.connector.scripts;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Message;

import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.ConfigLink;
import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.I18nDouble;
import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.I18nLong;
import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.I18nString;

public class OpenContentScript {

	protected static final String METADATA = "metadata";
	protected static final String OBJECT_NAME = "objectName";
	protected static final String VALUE = "value";
	protected static final String STRING_VALUE = "string_value";
	protected static final String FIELDS = "fields";
	protected static final String DEFAULT_LANGUAGE = "it";

	protected static ObjectMapper mapper = new ObjectMapper();
	protected static List<String> langs = Arrays.asList(new String[] { "it", "en", "de" });

	static {
		mapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<String> extractLinks(String json) throws Exception {
		List<String> links = new ArrayList<String>();
		Map<String, Object> map = mapper.readValue(json, Map.class);
		List<Map> list = (List<Map>) map.get("nodes");
		if (list != null) {
			for (Map m : list) {
				links.add(m.get("link").toString());
			}
		} else {
			list = (List<Map>) map.get("childrenNodes");
			if (list != null) {
				for (Map m : list) {
					links.add(m.get("link").toString());
				}
			}
		}
		return links;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Message> extractUri(String json) throws Exception {
		List<Message> result = new ArrayList<Message>();
		List<Map> configs = mapper.readValue(json, List.class);
		for (Map m : configs) {
			String name = m.get("name").toString();
			String uri = m.get("uri").toString();
			Long modified = Long.parseLong(m.get("dateModified").toString()) * 1000;
			ConfigLink.Builder builder = ConfigLink.newBuilder();
			builder.setName(name);
			builder.setUri(uri);
			builder.setDateModified(modified);
			result.add(builder.build());
		}
		return result;
	}

	protected Map<String, Map<String, Object>> buildMap(List<String> langs, String... json) throws Exception {
		Map<String, Map<String, Object>> result = new TreeMap<String, Map<String, Object>>();
		for (int i = 0; i < langs.size(); i++) {
			if (StringUtils.isEmpty(json[i])) continue;
			Map<String, Object> map = mapper.readValue(json[i], Map.class);
			result.put(langs.get(i), map);
		}
		return result;
	}

	protected Map<String, Object> getMap(Map<String, Map<String, Object>> map, String lang) {
		if (map.containsKey(lang)) {
			return map.get(lang);
		} else {
			return Collections.EMPTY_MAP;
		}
	}

	protected I18nString concatI18nString(String concat, I18nString... values) {
		I18nString.Builder builder = I18nString.newBuilder(values[0]);
		for (int i = 1; i < values.length; i++) {
			if (values[i].hasIt()) {
				builder.setIt(builder.getIt() + concat + values[i].getIt());
			}
			if (values[i].hasEn()) {
				builder.setEn(builder.getEn() + concat + values[i].getEn());
			}
			if (values[i].hasDe()) {
				builder.setDe(builder.getDe() + concat + values[i].getDe());
			}
		}
		return builder.build();
	}

	protected I18nLong toI18nLong(I18nString value, long coeff) {
		I18nLong.Builder builder = I18nLong.newBuilder();
		try {
			builder.setIt(value.hasIt() ? Long.parseLong(value.getIt()) * coeff : 0);
		} catch (Exception e) {}
		try {
			builder.setEn(value.hasEn() ? Long.parseLong(value.getEn()) * coeff : 0);
		} catch (Exception e) {}
		try {
			builder.setDe(value.hasDe() ? Long.parseLong(value.getDe()) * coeff : 0);
		} catch (Exception e) {}

		return builder.build();
	}

	protected I18nDouble toI18nDouble(I18nString value) {
		I18nDouble.Builder builder = I18nDouble.newBuilder();
		try {
			if (value.hasIt()) {
				builder.setIt(Double.parseDouble(value.getIt()));
			}
		} catch (Exception e) {}
		try {
			if (value.hasEn()) {
				builder.setEn(Double.parseDouble(value.getEn()));
			}
		} catch (Exception e) {}
		try {
			if (value.hasDe()) {
				builder.setDe(Double.parseDouble(value.getDe()));
			}
		} catch (Exception e) {}

		return builder.build();
	}

	protected I18nString getI18NStringValue(Map<String, Map<String, Object>> map, String... keys) {
		I18nString.Builder builder = I18nString.newBuilder();
		Object value;
		if (map.containsKey("it")) {
			value = getRecValue(map.get("it"), keys);
			if (value != null) {
				builder.setIt(asString(value));
			}
		}
		if (map.containsKey("en")) {
			value = getRecValue(map.get("en"), keys);
			if (value != null) {
				builder.setEn(asString(value));
			}
		}
		if (map.containsKey("de")) {
			value = getRecValue(map.get("de"), keys);
			if (value != null) {
				builder.setDe(asString(value));
			}
		}
		return builder.build();
	}
	
	protected String asString(Object o) {
		if (o instanceof List || o instanceof Set) {
			return o.toString().replace("[", "").replace("]", "");
		} else  {
			return o.toString();
		}  
	}

	protected Object getRecValue(Map<String, Object> map, String... keys) {
		Map<String, Object> m = map;
		Object res = null;
		int i = 0;
		for (i = 0; i < keys.length; i++) {
			if (m.get(keys[i]) instanceof Map) {
				m = (Map) m.get(keys[i]);
			} else if (m.get(keys[i]) instanceof List) {
				List l = (List) m.get(keys[i]);
				List<Object> res2 = new ArrayList<Object>();
				Map<String, String> conv = getReferencedObject(l);
				for (Object o : l) {
					Object o2 = ((Map) o).get(keys[keys.length - 1]);
					if (o2 != null) {
						if (conv.containsKey(o2)) {
							res2.add(conv.get(o2));
						} else {
							res2.add(o2);
						}
					}
				}
				if (!res2.isEmpty()) {
					res = res2;
				}
			} else if (res == null) {
				String r = getReferencedObject(m);
				if (r != null) {
					res = r;
				} else {
					res = m.get(keys[i]);
					if (res instanceof Boolean) {
						res = null;
					}					
				}
			}
		}
		return res;
	}
	
	protected Map<String, String> getReferencedObject(List l) {
		Map<String, String> res = new TreeMap<String, String>();
		try {
		for (Object o: l) {
			String objName = (String)((Map)o).get(OBJECT_NAME);
			String link = (String)((Map)o).get("link");
			Map<String, Object> map = mapper.readValue(new URL(link), Map.class);
			String name = (String)getRecValue(map, METADATA, OBJECT_NAME);
			res.put(objName, name);
		}
		} catch (Exception e) {
		}
		return res;		
	}

	protected String getReferencedObject(Map m) {
		String name = null;
		try {
		String link = (String)((Map)m).get("link");
		Map<String, Object> map = mapper.readValue(new URL(link), Map.class);
		name = (String)getRecValue(map, METADATA, OBJECT_NAME);
		} catch (Exception e) {
		}
		return name;
	}
	
	protected Double[] extractGPS(String gps) {
		Double latlon[] = new Double[2];
		String coords[] = gps.replace("#", "").split("\\|");
		try {
			latlon[0] = Double.parseDouble(coords[1]);
		} catch (Exception e) {
			return null;
		}
		try {
			latlon[1] = Double.parseDouble(coords[2]);
		} catch (Exception e) {
			return null;
		}
		return latlon;
	}

}
