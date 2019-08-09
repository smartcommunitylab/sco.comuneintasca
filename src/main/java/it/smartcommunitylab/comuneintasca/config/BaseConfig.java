package it.smartcommunitylab.comuneintasca.config;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;

import it.smartcommunitylab.comuneintasca.connector.AppManager;
import it.smartcommunitylab.comuneintasca.connector.processor.DataProcessor;

@Configuration
@EnableScheduling
public class BaseConfig {

	@Autowired
	private DataProcessor processor;
	@Autowired
	private AppManager appManager;
	@Value("classpath:/connectors.yml")
	private Resource resource;
	
	@PostConstruct
	public void initialize() throws IOException {
		appManager.initialize(resource.getInputStream(), processor);
	}


}
