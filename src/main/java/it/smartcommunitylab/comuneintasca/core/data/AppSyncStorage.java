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
package it.smartcommunitylab.comuneintasca.core.data;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.comuneintasca.core.model.AppObject;
import it.smartcommunitylab.comuneintasca.storage.data.SyncData;
import it.smartcommunitylab.comuneintasca.storage.exception.DataException;

@Component
public class AppSyncStorage  {


	public static final String PUBLISH_COLLECTION = "publishObjects";

	public static final String DRAFT_COLLECTION = "draftObjects";

	protected Log logger = LogFactory.getLog(this.getClass());

	protected AppSyncSubStorage draftStorage;
	protected AppSyncSubStorage publishStorage;
	
	@Autowired
	private CouchDBStorage extraStorage;
	
	@Autowired
	public AppSyncStorage(MongoOperations mongoTemplate) throws MalformedURLException {
		draftStorage = new AppSyncSubStorage(mongoTemplate, DRAFT_COLLECTION);
		publishStorage = new AppSyncSubStorage(mongoTemplate, PUBLISH_COLLECTION);
	}

	public SyncData getSyncAppData(long since, String appId, Map<String, Object> include, Map<String, Object> exclude) throws DataException {
		return publishStorage.getSyncAppData(since, appId, include, exclude);
	}

	public SyncData getSyncAppDraftData(long since, String appId,
			Map<String, Object> include, Map<String, Object> exclude) throws DataException {
		return draftStorage.getSyncAppData(since, appId, include, exclude);
	}

	public <T extends AppObject> List<T> searchObjects(String appId,
			Class<T> cls, Circle circle, String text, String lang,
			Long from, Long to, 
			Map<String, Object> criteria, SortedMap<String, Integer> sort,
			int limit, int skip) throws DataException {
		return publishStorage.searchObjects(appId, cls, circle, text, lang, from, to, criteria, sort, limit, skip);
	}



	public <T extends AppObject> List<T> searchDraftObjects(String appId,
			Class<T> cls, Circle circle, String text, String lang,
			Long from, Long to, 
			Map<String, Object> criteria, SortedMap<String, Integer> sort,
			int limit, int skip) throws DataException {
		return draftStorage.searchObjects(appId, cls, circle, text, lang, from, to, criteria, sort, limit, skip);
	}

	public List<AppObject> getAllAppObjects(String appId) throws DataException {
		return publishStorage.getAllAppObjects(appId);
	}

	public AppObject getObjectById(String id, String appId) throws DataException {
		return publishStorage.getObjectById(id, appId);
	}

	public <T extends AppObject> T getObjectById(String id, Class<T> cls, String appId) throws DataException {
		return publishStorage.getObjectById(id, cls, appId);
	}

	public <T extends AppObject> T getObjectDraftById(String id, Class<T> cls, String appId) throws DataException {
		return draftStorage.getObjectById(id, cls, appId);
	}

	public List<AppObject> getAllAppDtaftObjects(String appId) throws DataException {
		return draftStorage.getAllAppObjects(appId);
	}

	public AppObject getObjectDraftById(String id, String appId) throws DataException {
		return draftStorage.getObjectById(id, appId);
	}

	public <T extends AppObject> T storeDraftObject(T obj, String appId) throws DataException {
		return draftStorage.storeObject(obj, appId);
	}

	public <T extends AppObject> void deleteDraftObject(T obj, String appId) throws DataException {
		draftStorage.deleteObject(obj, appId);
	}

	public <T extends AppObject> T storeObject(T obj, String appId) throws DataException {
		T result = publishStorage.storeObject(obj, appId);
		extraStorage.storeObject(result);
		return result;
	}

	public <T extends AppObject> void deleteObject(T obj, String appId) throws DataException {
		extraStorage.deleteObject(obj);
		publishStorage.deleteObject(obj, appId);
	}
}
