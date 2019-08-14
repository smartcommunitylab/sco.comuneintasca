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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.smartcommunitylab.comuneintasca.core.model.AppObject;
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
	
	
	public <T extends AppObject> void storeObject(T ob) {
		if (ob instanceof DynamicConfigObject) {
			DynamicConfigObject conf = (DynamicConfigObject) ob;
			List<MenuItem> highlights = conf.getHighlights();
			List<HashMap> old = db.findDocs("{\"selector\": { \"elementType\": \"gallery-item\"}}", HashMap.class);
			old.forEach(o -> db.remove(o));
			highlights.forEach(h -> {
				h.setElementType("gallery-item");
				db.save(h);
			});
			
		}
		List<HashMap> docs = db.findDocs("{\"selector\": { \"id\": \""+ob.getId()+"\", \"appId\":\""+ob.getAppId()+"\"}}", HashMap.class);
		if (docs != null && docs.size() > 0) {
			String _id = (String) docs.get(0).get("_id");
			String _rev = (String) docs.get(0).get("_rev");
			HashMap toSave = new ObjectMapper().convertValue(ob, HashMap.class);
			toSave.put("_id", _id);
			toSave.put("_rev", _rev);
			db.update(toSave);
		} else  {
			db.save(ob);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AppObject> void deleteObject(T ob) {
		try {
			List<HashMap> docs = db.findDocs("{\"selector\": { \"id\": \""+ob.getId()+"\", \"appId\":\""+ob.getAppId()+"\"}}", HashMap.class);
			if (docs != null && docs.size() > 0) {
				db.remove(docs.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
