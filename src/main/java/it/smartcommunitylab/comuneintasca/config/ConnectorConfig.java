package it.smartcommunitylab.comuneintasca.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "it.smartcommunitylab.comuneintasca.connector")
public class ConnectorConfig {
}
