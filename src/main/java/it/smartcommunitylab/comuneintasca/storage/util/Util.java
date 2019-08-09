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
package it.smartcommunitylab.comuneintasca.storage.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.smartcommunitylab.comuneintasca.storage.SyncObjectBean;
import it.smartcommunitylab.comuneintasca.storage.data.BasicObject;
import it.smartcommunitylab.comuneintasca.storage.data.SyncData;
import it.smartcommunitylab.comuneintasca.storage.data.SyncDataRequest;

/**
 * @author raman
 *
 */
public class Util {

    private static ObjectMapper fullMapper = new ObjectMapper();
    static {
        fullMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        fullMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        fullMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        fullMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    }

    @SuppressWarnings("unchecked")
	public static SyncDataRequest convertRequest(Map<String,Object> requestMap, long since) throws ClassNotFoundException {
		SyncData data = new SyncData();
		try {
			data.setVersion(Long.parseLong(requestMap.get("version").toString()));
		} catch (Exception e1) {
			data.setVersion(0);
		}
		Map<String,Object> readMap = (Map<String,Object>)requestMap.get("updated");
		Map<String, List<BasicObject>> updatedMap = new HashMap<String, List<BasicObject>>();

		for (String key : readMap.keySet()) {
			Class<? extends BasicObject> cls = (Class<? extends BasicObject>) Thread.currentThread().getContextClassLoader().loadClass(key);
			List<Object> list = (List<Object>)readMap.get(key);
			List<BasicObject> result = new ArrayList<BasicObject>();
			if (list != null) {
				for (Object o : list) result.add(fullMapper.convertValue(o, cls));
			}
			updatedMap.put(key, result);
		}
		data.setUpdated(updatedMap);
		
		Map<String,List<String>> deleted = (Map<String,List<String>>)requestMap.get("deleted");
		data.setDeleted(deleted);
		
		data.setExclude((Map<String, Object>) requestMap.get("exclude"));
		data.setInclude((Map<String, Object>) requestMap.get("include"));
		
    	return new SyncDataRequest(data, since);
    } 
    
    public static <T> T convert(Object object, Class<T> cls) {
    	return fullMapper.convertValue(object, cls);
    }
    
    public static <T extends SyncObjectBean> T convertToObjectBean(BasicObject o, Class<T> cls) throws InstantiationException, IllegalAccessException {
    	T result = cls.newInstance();
    	@SuppressWarnings("unchecked")
		Map<String,Object> map = convert(o, Map.class);
    	result.setContent(map);
    	result.setDeleted(false);
    	result.setId(o.getId());
    	result.setType(o.getClass().getCanonicalName());
    	result.setUpdateTime(o.getUpdateTime() < 0 ? System.currentTimeMillis() : o.getUpdateTime());
    	result.setVersion(o.getVersion());
    	result.setUser(o.getUser());
    	return result;
    }
    
	public static <T extends BasicObject> T convertBeanToBasicObject(SyncObjectBean bean, Class<T> cls) {
    	T res = convert(bean.getContent(), cls);
    	if (res.getUpdateTime() <= 0) res.setUpdateTime(bean.getUpdateTime()); 
    	return res;
    }
}