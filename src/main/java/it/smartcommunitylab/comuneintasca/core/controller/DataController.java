package it.smartcommunitylab.comuneintasca.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.smartcommunitylab.comuneintasca.core.model.AppObject;
import it.smartcommunitylab.comuneintasca.core.model.TypeConstants;
import it.smartcommunitylab.comuneintasca.core.service.DataService;
import it.smartcommunitylab.comuneintasca.storage.exception.DataException;

@Controller
@RequestMapping("/dataapi")
@PreAuthorize("hasAuthority(#app)")
public class DataController {

	@Autowired
	private DataService dataService;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@RequestMapping(value="/{app}/ping",method=RequestMethod.GET)
	public @ResponseBody String ping(@PathVariable String app) {
		return "pong";
	}
	
	@RequestMapping(value="/{app}/{type}/batch",method=RequestMethod.POST)
	public <T extends AppObject> void upsertType(@PathVariable String app, @PathVariable String type, @RequestParam(required=false) String classifier, @RequestBody List<Map<String,Object>> data) throws DataException {
		Class<T> cls = mapTypeToClass(type);
		dataService.upsertType(convertData(data, cls), cls, app, classifier);
	}

	@RequestMapping(value="/{app}/{type}",method=RequestMethod.POST)
	public <T extends AppObject> void upsertObject(@PathVariable String app, @PathVariable String type, @RequestBody Map<String,Object> data) throws DataException {
		Class<T> cls = mapTypeToClass(type);
		dataService.upsertObject(convertObject(data, cls), app);
	}

	@RequestMapping(value="/{app}/{type}/{id}",method=RequestMethod.DELETE)
	public <T extends AppObject> void deleteObject(@PathVariable String app, @PathVariable String type, @PathVariable String id) throws DataException {
		dataService.deleteObject(dataService.getDraftObject(id, app), app);
	}

	@RequestMapping(value="/{app}/{type}/{id}/publish",method=RequestMethod.PUT)
	public <T extends AppObject> void publishObject(@PathVariable String app, @PathVariable String type, @PathVariable String id) throws DataException {
		dataService.publishObject(dataService.getDraftObject(id, app), app);
	}
	@RequestMapping(value="/{app}/{type}/{id}/unpublish",method=RequestMethod.PUT)
	public <T extends AppObject> void unpublishObject(@PathVariable String app, @PathVariable String type, @PathVariable String id) throws DataException {
		dataService.unpublishObject(dataService.getDraftObject(id, app), app);
	}

	@RequestMapping(value="/{app}/{type}/publish",method=RequestMethod.PUT)
	public <T extends AppObject> void publishType(@PathVariable String app, @PathVariable String type, @RequestParam(required=false) String classifier) throws DataException {
		Class<T> cls = mapTypeToClass(type);
		dataService.publishType(cls, app, classifier);
	}

	@RequestMapping(value="/{app}/publish",method=RequestMethod.PUT)
	public <T extends AppObject> void publishApp(@PathVariable String app) throws DataException {
		dataService.publishApp(app);
	}
	
	private <T extends AppObject> List<T> convertData(List<Map<String, Object>> data, Class<T> cls) {
		List<T> result = new ArrayList<T>();
		for (Map<String,Object> o : data) {
			result.add(convertObject(o, cls));
		}
		return result;
	}

	private <T extends AppObject> T convertObject(Map<String, Object> data, Class<T> cls) {
		return mapper.convertValue(data, cls);
	}

	@SuppressWarnings("unchecked")
	private <T extends AppObject> Class<T> mapTypeToClass(String type) {
		return (Class<T>) TypeConstants.getTypeMapping(type);
	}
}
