package it.smartcommunitylab.comuneintasca.test.config;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import it.smartcommunitylab.comuneintasca.connector.AppManager;
import it.smartcommunitylab.comuneintasca.connector.processor.DataProcessor;

@Configuration
@ComponentScan(basePackages = {"it.smartcommunitylab.comuneintasca.core", "it.smartcommunitylab.comuneintasca.connector"})
@EnableMongoRepositories(basePackages = "it.smartcommunitylab.comuneintasca.connector")
public class TestConfig {

	@Value("classpath:/connectors.yml")
	private Resource resource;
	@Autowired
	private DataProcessor processor;
	@Autowired
	private AppManager appManager;

	@PostConstruct
	public void initialize() throws IOException {
		appManager.initialize(resource.getInputStream(), processor);
	}

	@Bean(name="mongoTemplate")
	public MongoTemplate getMongoTemplate() throws UnknownHostException, MongoException {
		return new MongoTemplate(new MongoClient(), "comuneintasca-multi-test");
	}

}
