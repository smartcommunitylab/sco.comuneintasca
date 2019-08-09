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

import it.smartcommunitylab.comuneintasca.connector.AppManager;
import it.smartcommunitylab.comuneintasca.connector.ConnectorStorage;
import it.smartcommunitylab.comuneintasca.connector.Subscriber;
import it.smartcommunitylab.comuneintasca.connector.processor.DataProcessor;
import it.smartcommunitylab.comuneintasca.core.data.AppSyncStorage;
import it.smartcommunitylab.comuneintasca.core.model.AppObject;
import it.smartcommunitylab.comuneintasca.core.model.DynamicConfigObject;
import it.smartcommunitylab.comuneintasca.core.model.ItineraryObject;
import it.smartcommunitylab.comuneintasca.core.service.DataService;
import it.smartcommunitylab.comuneintasca.storage.exception.DataException;
import it.smartcommunitylab.comuneintasca.test.config.TestConfig;
import it.smartcommunitylab.comuneintasca.test.util.ObjectCreator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TestConnector {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private ConnectorStorage connectorStorage;
	@Autowired
	private AppManager appManager;
	@Autowired
	private DataService dataService;
	@Autowired
	private DataProcessor processor;

	@Before
	public void prepare() {
		mongoTemplate.dropCollection(ConnectorStorage.COLLECTION);
		mongoTemplate.dropCollection(AppSyncStorage.DRAFT_COLLECTION);
		mongoTemplate.dropCollection(AppSyncStorage.PUBLISH_COLLECTION);
	}

	@Test
	public void testConfig() throws DataException {
		Subscriber s = new Subscriber();
		s.subscribe(appManager.getApp("test", "smartcampus.service.opendata", "it.smartcommunitylab.comuneintasca.connector.flows.ConfigFlow"), processor);
		s.process();
		
		List<DynamicConfigObject> draftObjects = dataService.getDraftObjects(DynamicConfigObject.class, ObjectCreator.TEST_APP);
		assertNotNull(draftObjects);
		assertEquals(1, draftObjects.size());

		List<DynamicConfigObject> publishObjects = dataService.getPublishedObjects(DynamicConfigObject.class, ObjectCreator.TEST_APP);
		assertNotNull(publishObjects);
		assertEquals(0, publishObjects.size());
	}

	@Test
	public void testLists() throws DataException {
//		testLists("test", "smartcampus.service.opendata", "it.smartcommunitylab.comuneintasca.connector.flows.EventsFlow", EventObject.class);
//		testLists("test", "smartcampus.service.opendata", "it.smartcommunitylab.comuneintasca.connector.flows.RestaurantsFlow", RestaurantObject.class);
//		testLists("test", "smartcampus.service.opendata", "it.smartcommunitylab.comuneintasca.connector.flows.HotelsFlow", HotelObject.class);
//		testLists("test", "smartcampus.service.opendata", "it.smartcommunitylab.comuneintasca.connector.flows.CulturaFlow", POIObject.class);
		testLists("test", "smartcampus.service.opendata", "it.smartcommunitylab.comuneintasca.connector.flows.ItinerariesFlow", ItineraryObject.class);
//		testLists("test", "smartcampus.service.opendata", "it.smartcommunitylab.comuneintasca.connector.flows.TextsFlow", ContentObject.class);
	}

	public void testLists(String app, String serviceId, String clsName, Class<? extends AppObject> cls) throws DataException {
		Subscriber s = new Subscriber();
		s.subscribe(appManager.getApp(app, serviceId, clsName), processor);
		s.process();
		
		List<?> draftObjects = dataService.getDraftObjects(cls, ObjectCreator.TEST_APP);
		assertNotNull(draftObjects);
		assertEquals(1, draftObjects.size());

		List<?> publishObjects = dataService.getPublishedObjects(cls, ObjectCreator.TEST_APP);
		assertNotNull(publishObjects);
		assertEquals(1, publishObjects.size());
	}

}
