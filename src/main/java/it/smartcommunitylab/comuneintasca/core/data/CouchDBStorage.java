/*******************************************************************************
 * Copyright 2015 Fondazione Bruno Kessler
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

import java.util.List;

import javax.annotation.PostConstruct;

import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.comuneintasca.core.model.DynamicConfigObject;
import it.smartcommunitylab.comuneintasca.core.model.MenuItem;

/**
 * @author raman
 *
 */
@Component
public class CouchDBStorage {

	@Value("${couchdb.host:localhost}")
	private String dbHost;
	@Value("${couchdb.port:5984}")
	private int dbPort;
	@Value("${couchdb.protocol:http}")
	private String dbProtocol;
	@Value("${couchdb.db:comuneinstasca}")
	private String dbName;
	@Value(value="${couchdb.username:#{null}}")
	private String dbUser;
	@Value("${couchdb.password:#{null}}")
	private String dbPassword;
	
	private CouchDbClient db;
	
	@PostConstruct
	public void initDB() {
		db = new CouchDbClient(dbName, true, dbProtocol, dbHost, dbPort, dbUser, dbPassword);
		
	}
	
	
	public <T> void storeObject(T ob) {
		if (ob instanceof DynamicConfigObject) {
			DynamicConfigObject conf = (DynamicConfigObject) ob;
			List<MenuItem> highlights = conf.getHighlights();
			List<MenuItem> old = db.findDocs("{\"selector\": { \"elementType\": \"gallery-item\"}}", MenuItem.class);
			old.forEach(o -> db.remove(o));
			highlights.forEach(h -> {
				h.setElementType("gallery-item");
				db.save(h);
			});
			
		}
		db.save(ob);
	}
	
	public <T> void deleteObject(T ob) {
		db.remove(ob);
	}

}
