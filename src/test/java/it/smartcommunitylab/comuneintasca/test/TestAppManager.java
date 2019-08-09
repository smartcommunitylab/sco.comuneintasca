package it.smartcommunitylab.comuneintasca.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.smartcommunitylab.comuneintasca.connector.App;
import it.smartcommunitylab.comuneintasca.connector.AppManager;
import it.smartcommunitylab.comuneintasca.storage.exception.DataException;
import it.smartcommunitylab.comuneintasca.test.config.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TestAppManager {

	@Autowired
	private AppManager manager;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Test
	public void getAppData() throws DataException {
		App app = manager.getApp("test", "smartcampus.service.opendata", "it.smartcommunitylab.comuneintasca.connector.flows.ConfigFlow");
		assertNotNull(app);

		app = manager.getApp("test", "smartcampus.service.opendata", "Test3");
		assertNull(app);
	}
	
	
}
