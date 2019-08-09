package it.smartcommunitylab.comuneintasca.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.smartcommunitylab.comuneintasca.core.data.AppSyncStorage;
import it.smartcommunitylab.comuneintasca.core.model.AppObject;
import it.smartcommunitylab.comuneintasca.core.model.EventObject;
import it.smartcommunitylab.comuneintasca.core.service.DataService;
import it.smartcommunitylab.comuneintasca.storage.exception.DataException;
import it.smartcommunitylab.comuneintasca.test.config.TestConfig;
import it.smartcommunitylab.comuneintasca.test.util.ObjectCreator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})

public class TestDataService {

	@Autowired
	private DataService dataService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Before
	public void prepare() {
		mongoTemplate.dropCollection(AppSyncStorage.DRAFT_COLLECTION);
		mongoTemplate.dropCollection(AppSyncStorage.PUBLISH_COLLECTION);
	}

	@Test
	public void testDraftData() throws DataException {
		EventObject eo = ObjectCreator.createEvent();
		EventObject stored = dataService.upsertObject(eo, ObjectCreator.TEST_APP);
		assertNotNull(stored);
		assertEquals(eo.getId(), stored.getId());
		
		List<EventObject> draftObjects = dataService.getDraftObjects(EventObject.class, ObjectCreator.TEST_APP);
		assertNotNull(draftObjects);
		assertEquals(1, draftObjects.size());

		AppObject draftObject = dataService.getDraftObject("event1", ObjectCreator.TEST_APP);
		assertNotNull(draftObject);
		
		draftObject = dataService.upsertObject(draftObject, ObjectCreator.TEST_APP);
		assertEquals(EventObject.class, draftObject.getClass());
		draftObjects = dataService.getDraftObjects(EventObject.class, ObjectCreator.TEST_APP);
		assertNotNull(draftObjects);
		assertEquals(1, draftObjects.size());
		
		dataService.deleteObject(stored, ObjectCreator.TEST_APP);
		draftObjects = dataService.getDraftObjects(EventObject.class, ObjectCreator.TEST_APP);
		assertNotNull(draftObjects);
		assertEquals(0, draftObjects.size());
	}

	@Test
	public void testPublishObject() throws DataException {
		EventObject eo = ObjectCreator.createEvent();
		EventObject stored = dataService.upsertObject(eo, ObjectCreator.TEST_APP);

		List<EventObject> publishObjects = dataService.getPublishedObjects(EventObject.class, ObjectCreator.TEST_APP);
		assertNotNull(publishObjects);
		assertEquals(0, publishObjects.size());

		dataService.publishObject(stored, ObjectCreator.TEST_APP);
		publishObjects = dataService.getPublishedObjects(EventObject.class, ObjectCreator.TEST_APP);
		assertNotNull(publishObjects);
		assertEquals(1, publishObjects.size());

		dataService.unpublishObject(stored, ObjectCreator.TEST_APP);
		publishObjects = dataService.getPublishedObjects(EventObject.class, ObjectCreator.TEST_APP);
		assertNotNull(publishObjects);
		assertEquals(0, publishObjects.size());

	}

	@Test
	public void testPublishTypeNoClassifier() throws DataException {
		EventObject eo = ObjectCreator.createEvent();
		dataService.upsertObject(eo, ObjectCreator.TEST_APP);

		List<EventObject> publishObjects = dataService.getPublishedObjects(EventObject.class, ObjectCreator.TEST_APP);
		assertNotNull(publishObjects);
		assertEquals(0, publishObjects.size());

		dataService.publishType(EventObject.class, ObjectCreator.TEST_APP, null);
		publishObjects = dataService.getPublishedObjects(EventObject.class, ObjectCreator.TEST_APP);
		assertNotNull(publishObjects);
		assertEquals(1, publishObjects.size());
	}

	@Test
	public void testPublishTypeClassifier() throws DataException {
		EventObject eo = ObjectCreator.createEvent();
		dataService.upsertObject(eo, ObjectCreator.TEST_APP);

		List<EventObject> publishObjects = dataService.getPublishedObjects(EventObject.class, ObjectCreator.TEST_APP);
		assertNotNull(publishObjects);
		assertEquals(0, publishObjects.size());

		dataService.publishType(EventObject.class, ObjectCreator.TEST_APP, "classifier 2");
		publishObjects = dataService.getPublishedObjects(EventObject.class, ObjectCreator.TEST_APP);
		assertNotNull(publishObjects);
		assertEquals(0, publishObjects.size());
		
		dataService.publishType(EventObject.class, ObjectCreator.TEST_APP, "classifier");
		publishObjects = dataService.getPublishedObjects(EventObject.class, ObjectCreator.TEST_APP);
		assertNotNull(publishObjects);
		assertEquals(1, publishObjects.size());

	}

}
