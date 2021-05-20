/*******************************************************************************
 * Copyright 2015 Fondazione Bruno Kessler
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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import it.smartcommunitylab.comuneintasca.connector.ConnectionUtils;
import it.smartcommunitylab.comuneintasca.connector.processor.MappingDescriptor;
import it.smartcommunitylab.comuneintasca.core.model.MenuItem;
import it.smartcommunitylab.comuneintasca.storage.util.Util;

/**
 * @author raman
 *
 */
public class HighlightsScript extends OpenContentScript {

	private static final Logger logger = LoggerFactory.getLogger(HighlightsScript.class);
		
	/**
	 * Reading Google Sheet with header row and 4 columns: order, URL, class, object ID
	 * @param id
	 * @param key
	 * @param objectUrl
	 * @return
	 */
	public List<MenuItem> extractContent(String id, String key, String objectUrl, String imagePath, Map<String, MappingDescriptor> descriptors) throws Exception {
		List<List<Object>> highlights = Util.extractContentFromGS(id, "A:E", key);
		List<MenuItem> items = new LinkedList<>();
		highlights = highlights.subList(1, highlights.size()).stream().filter(row -> row.size() > 0 && row.get(0) != null && StringUtils.hasText(row.get(0).toString())).collect(Collectors.toList());
		highlights.sort((a,b) -> a.get(0).toString().compareTo(b.get(0).toString()));
		for (List<Object> line : highlights) {
			MenuItem item = new MenuItem();
			String objectId = line.get(4).toString();
			String title = line.get(2).toString();
			String url = line.get(1).toString();
			item.setExternalLink(url);
			String itJson = ConnectionUtils.call(objectUrl + "/object/" + objectId, String.class);
			String enJson = null;
			String deJson = null;
			try {
				enJson = ConnectionUtils.call(objectUrl + "/object/" + objectId+ "?Translation=eng-GB", String.class);
			} catch (Exception e) {
				logger.warn("No translation EN: " + objectId);;
			}
			try {
				deJson = ConnectionUtils.call(objectUrl + "/object/" + objectId+ "?Translation=gert-DE", String.class);
			} catch (Exception e) {
				logger.warn("No translation DE: " + objectId);;
			}
			Map<String, Map<String, Object>> i18n = buildMap(langs, itJson, enJson, deJson);
			item.setId(objectId);
			item.setType(descriptors.get(((String)line.get(3)).toLowerCase()).getLocalType());
//			item.setName(getI18NStringValueMap(i18n, FIELDS, "titolo", VALUE));
//			if (!item.getName().containsKey("it")) {
//				item.setName(getI18NStringValueMap(i18n, FIELDS, "title", VALUE));
//			}
//			if (!item.getName().containsKey("it")) {
//				item.setName(getI18NStringValueMap(i18n, FIELDS, "nome", VALUE));
//			}
//			if (!item.getName().containsKey("it")) {
//				item.setName(getI18NStringValueMap(i18n, FIELDS, "name", VALUE));
//			}
			item.setName(new HashMap<>());
			item.getName().put("it", title);
			item.getName().put("en", title);
			item.getName().put("de", title);
			
			item.setObjectIds(Collections.singletonList(((String)getRecValue(getMap(i18n,DEFAULT_LANGUAGE), "metadata", "objectRemoteId"))));
			
			Object image = getRecValue(getMap(i18n,DEFAULT_LANGUAGE), FIELDS, "image", VALUE);
			if (image != null && image instanceof String) {
				image = getImageURL(image.toString(), imagePath); 
				Map<String, String> images = new HashMap<>();
				images.put("it", (String)image);
				images.put("en", (String)image);
				images.put("de", (String)image);
				item.setImage(images);
			}
			items.add(item);
		}

		return items;
	}
	
	protected Map<String, String> getI18NStringValueMap(Map<String, Map<String, Object>> map, String... keys) {
		Map<String, String> res = new HashMap<>();
		Object value;
		if (map.containsKey("it")) {
			value = getRecValue(map.get("it"), keys);
			if (value != null) {
				res.put("it", asString(value));
			}
		}
		if (map.containsKey("en")) {
			value = getRecValue(map.get("en"), keys);
			if (value != null) {
				res.put("en", asString(value));
			}
		}
		if (map.containsKey("de")) {
			value = getRecValue(map.get("de"), keys);
			if (value != null) {
				res.put("de", asString(value));
			}
		}
		return res;
	}
	
	protected String getImageURL(String image, String imagePath) {
		if (image == null || image.isEmpty()) return null;
		String img = image;
		if (img.indexOf('|')>0) {
			img = img.substring(0,img.indexOf('|'));
		} 
		if (!img.startsWith("http")) {
			return (imagePath == null ? "" : imagePath) + img;
		}
		if (img.startsWith("http:")) {
			img = img.replace("http:", "https:");
		}
		return img;
	}

}


