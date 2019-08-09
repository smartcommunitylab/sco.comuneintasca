package it.smartcommunitylab.comuneintasca.connector;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.comuneintasca.core.data.AppSyncSubStorage;
import it.smartcommunitylab.comuneintasca.core.model.AppObject;
import it.smartcommunitylab.comuneintasca.storage.exception.DataException;

@Component
public class ConnectorStorage {
	public static final String COLLECTION = "connectorStorage";

//	private static final String CLASS_FIELD = "_class";

	@Autowired
	private MongoTemplate mongoTemplate;
	
	private AppSyncSubStorage storage = null;
	
	private AppSyncSubStorage getStorage() {
		if (storage == null) {
			storage = new AppSyncSubStorage(mongoTemplate, COLLECTION); 
		}
		return storage;
	}

	public <T extends AppObject> void storeObject(T obj, String appId) throws DataException {
		getStorage().storeObject(obj, appId);
//		obj.setLocalId(obj.getId());
//		AppObject old = getObjectById(obj.getId(), appId);
//		if (old != null) {
//			obj.setId(old.getId());
//		} else {
//			obj.setId(null);
//		}
//		obj.setAppId(appId);
//		mongoTemplate.save(obj, COLLECTION);
	}

	public List<AppObject> getAllAppObjects(String appId) throws DataException {
		return getStorage().getAllAppObjects(appId);
//		return mongoTemplate.find(Query.query(Criteria.where("appId").is(appId)), AppObject.class, COLLECTION);
	}

	public AppObject getObjectById(String id, String appId) throws DataException {
		return getStorage().getObjectById(id, appId);
//		return findObject(id, appId, AppObject.class);
	}

//	private <T extends AppObject> T findObject(String id, String appId, Class<T> cls) {
//		List<T> list = mongoTemplate.find(Query.query(Criteria.where("appId").is(appId).and("localId").is(id)), cls, COLLECTION);
//		if (list != null && list.size() > 0) return list.get(0);
//		return null;
//	}
	
	public <T extends AppObject> T getObjectById(String id, Class<T> cls, String appId) throws DataException {
		return getStorage().getObjectById(id, cls, appId);
//		return findObject(id, appId, cls);
	}

	public <T extends AppObject> List<T> getObjectsByType(Class<T> cls, String appId) throws DataException {
		return getStorage().searchObjects(appId, cls, null, null, null, null, null, null, null, 0, -1);
//		return mongoTemplate.find(Query.query(Criteria.where("appId").is(appId).and(CLASS_FIELD).is(cls.getName())), cls, COLLECTION);
	}
	public <T extends AppObject> List<T> getObjectsByType(Class<T> cls, String classifier, String appId) throws DataException {
		return getStorage().searchObjects(appId, cls, null, null, null, null, null, Collections.<String,Object>singletonMap("classifier", classifier), null, 0, -1);
//		return mongoTemplate.find(Query.query(Criteria.where("appId").is(appId).and(CLASS_FIELD).is(cls.getName()).and("classifier").is(classifier)), cls, COLLECTION);
	}

	public <T extends AppObject> void deleteObjectById(String id, Class<T> cls, String appId) throws DataException {
		getStorage().deleteObject(getObjectById(id, appId), appId);
//		T old = findObject(id, appId, cls);
//		if (old != null) {
//			mongoTemplate.remove(old, COLLECTION);
//		}
	}

}
