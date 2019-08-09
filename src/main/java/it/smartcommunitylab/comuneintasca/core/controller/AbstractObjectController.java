/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
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
package it.smartcommunitylab.comuneintasca.core.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;

import it.smartcommunitylab.comuneintasca.core.data.AppSyncStorage;
import it.smartcommunitylab.comuneintasca.core.model.BaseCITObject;
import it.smartcommunitylab.comuneintasca.core.model.ObjectFilter;

public class AbstractObjectController {

	@Autowired
	protected AppSyncStorage storage;

	protected Log logger = LogFactory.getLog(this.getClass());

	public <T extends BaseCITObject> List<T> getAllObject(String appId, ObjectFilter filterObj, Class<T> cls) throws Exception {
		try {
			Map<String, Object> criteria = null;
			if (filterObj != null) {
				criteria = filterObj.getCriteria() != null ? filterObj.getCriteria() : new HashMap<String, Object>();
				if (filterObj.getCategories() != null && !filterObj.getCategories().isEmpty()) {
					criteria.put("category",
							Collections.singletonMap("$in",
									filterObj.getCategories()));
				}
			} else {
				filterObj = new ObjectFilter();
				criteria = Collections.emptyMap();
			}

			if (filterObj.getSkip() == null) {
				filterObj.setSkip(0);
			}
			if (filterObj.getLimit() == null || filterObj.getLimit() < 0) {
				filterObj.setLimit(100);
			} 
			Circle circle = null;
			if (filterObj.getCenter() != null && filterObj.getRadius() != null) {
				circle = new Circle(filterObj.getCenter()[0],
						filterObj.getCenter()[1], filterObj.getRadius());
			}

			List<T> objects = null;

			objects = storage.searchObjects(appId, (Class<T>) cls,
					circle,
					filterObj.getText(), filterObj.getLang(), 
					filterObj.getFromTime(),
					filterObj.getToTime(),
					criteria, filterObj.getSort(),
					filterObj.getLimit(), filterObj.getSkip());

			if (objects != null) {
				return objects;
			}
		} catch (Exception e) {
			logger.error("failed to find objects: " + e.getMessage());
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

}
