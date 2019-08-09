package it.smartcommunitylab.comuneintasca.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.smartcommunitylab.comuneintasca.connector.ConnectorStorage;
import it.smartcommunitylab.comuneintasca.core.model.AppObject;
import it.smartcommunitylab.comuneintasca.core.model.EventObject;
import it.smartcommunitylab.comuneintasca.storage.exception.DataException;
import it.smartcommunitylab.comuneintasca.test.config.TestConfig;
import it.smartcommunitylab.comuneintasca.test.util.ObjectCreator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TestConnectorStorage {

	@Autowired
	private ConnectorStorage storage;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Before
	public void prepare() {
		mongoTemplate.dropCollection(ConnectorStorage.COLLECTION);
	}
	
	@Test
	public void getAppData() throws DataException {
		EventObject eo = ObjectCreator.createEvent();
		storage.storeObject(eo, ObjectCreator.TEST_APP);
		
		AppObject ao = storage.getObjectById("event1", ObjectCreator.TEST_APP);
		assertNotNull(ao);
		assertTrue(ao.getClass().equals(EventObject.class));
		
		EventObject stored = storage.getObjectById("event1", EventObject.class, ObjectCreator.TEST_APP);
		assertNotNull(stored);
		stored.setCategory("category2");
		
		storage.storeObject(stored, ObjectCreator.TEST_APP);
		List<AppObject> allAppObjects = storage.getAllAppObjects(ObjectCreator.TEST_APP);
		assertTrue(allAppObjects != null && allAppObjects.size() == 1);
		
		stored = storage.getObjectById("event1", EventObject.class, ObjectCreator.TEST_APP);
		assertEquals("category2",stored.getCategory());
		
		List<EventObject> events = storage.getObjectsByType(EventObject.class, ObjectCreator.TEST_APP);
		assertTrue(events != null && events.size() == 1);
		
		events = storage.getObjectsByType(EventObject.class, "classifier", ObjectCreator.TEST_APP);
		assertTrue(events != null && events.size() == 1);
		events = storage.getObjectsByType(EventObject.class, "classifier2", ObjectCreator.TEST_APP);
		assertTrue(events != null && events.size() == 0);
		
		storage.deleteObjectById("event1", EventObject.class, ObjectCreator.TEST_APP);
		allAppObjects = storage.getAllAppObjects(ObjectCreator.TEST_APP);
		assertTrue(allAppObjects != null && allAppObjects.size() == 0);
}
	
	
}
