package it.smartcommunitylab.comuneintasca.test.util;

import it.smartcommunitylab.comuneintasca.core.model.EventObject;
import it.smartcommunitylab.comuneintasca.core.service.DataService;
import it.smartcommunitylab.comuneintasca.test.config.TestConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TestDataService {

	@Autowired
	private DataService dataService;
	
	@Test
	public void getAppData() {
		dataService.getPublishedObjects(EventObject.class, "TrentoInTasca");
	}
}
