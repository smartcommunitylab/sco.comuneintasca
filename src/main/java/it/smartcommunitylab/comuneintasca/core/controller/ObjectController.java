package it.smartcommunitylab.comuneintasca.core.controller;

import it.smartcommunitylab.comuneintasca.core.model.BaseCITObject;
import it.smartcommunitylab.comuneintasca.core.model.EventObject;
import it.smartcommunitylab.comuneintasca.core.model.HotelObject;
import it.smartcommunitylab.comuneintasca.core.model.ItineraryObject;
import it.smartcommunitylab.comuneintasca.core.model.ObjectFilter;
import it.smartcommunitylab.comuneintasca.core.model.POIObject;
import it.smartcommunitylab.comuneintasca.core.model.RestaurantObject;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ObjectController extends AbstractObjectController {

	@RequestMapping(method = RequestMethod.POST, value = "/events/{appId}")
	public ResponseEntity<?> events(@PathVariable String appId, @RequestBody ObjectFilter filter) throws Exception{
		return searchObjects(filter, EventObject.class, appId);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/places/{appId}")
	public ResponseEntity<?> places(@PathVariable String appId, @RequestBody ObjectFilter filter) throws Exception{
		return searchObjects(filter, POIObject.class, appId);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/itineraries/{appId}")
	public ResponseEntity<?> itineraries(@PathVariable String appId, @RequestBody ObjectFilter filter) throws Exception{
		return searchObjects(filter, ItineraryObject.class, appId);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/hotels/{appId}")
	public ResponseEntity<?> hotels(@PathVariable String appId, @RequestBody ObjectFilter filter) throws Exception{
		return searchObjects(filter, HotelObject.class, appId);
	}
	@RequestMapping(method = RequestMethod.POST, value = "/restaurants/{appId}")
	public ResponseEntity<?> restaurants(@PathVariable String appId, @RequestBody ObjectFilter filter) throws Exception{
		return searchObjects(filter, RestaurantObject.class, appId);
	}

	private <T extends BaseCITObject> ResponseEntity<List<T>> searchObjects(ObjectFilter filter, Class<T> cls, String appId) throws Exception {
		return new ResponseEntity<List<T>>(getAllObject(appId, filter, cls),HttpStatus.OK);
	}
}
