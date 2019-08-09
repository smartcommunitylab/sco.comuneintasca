package it.smartcommunitylab.comuneintasca.connector.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;

import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.ConfigData;
import it.smartcommunitylab.comuneintasca.connector.App;
import it.smartcommunitylab.comuneintasca.connector.AppManager;
import it.smartcommunitylab.comuneintasca.connector.ConnectorStorage;
import it.smartcommunitylab.comuneintasca.connector.SourceEntry;
import it.smartcommunitylab.comuneintasca.connector.flows.ConfigFlow;
import it.smartcommunitylab.comuneintasca.connector.flows.CulturaFlow;
import it.smartcommunitylab.comuneintasca.connector.flows.EventsFlow;
import it.smartcommunitylab.comuneintasca.connector.flows.HotelsFlow;
import it.smartcommunitylab.comuneintasca.connector.flows.ItinerariesFlow;
import it.smartcommunitylab.comuneintasca.connector.flows.MainEventsFlow;
import it.smartcommunitylab.comuneintasca.connector.flows.RestaurantsFlow;
import it.smartcommunitylab.comuneintasca.connector.flows.TerritoryServicesFlow;
import it.smartcommunitylab.comuneintasca.connector.flows.TextsFlow;
import it.smartcommunitylab.comuneintasca.connector.processor.ConfigProcessor.ObjectFilters;
import it.smartcommunitylab.comuneintasca.connector.processor.DataExtractor.Extractor;
import it.smartcommunitylab.comuneintasca.core.model.AppObject;
import it.smartcommunitylab.comuneintasca.core.model.BaseCITObject;
import it.smartcommunitylab.comuneintasca.core.model.ContentObject;
import it.smartcommunitylab.comuneintasca.core.model.DynamicConfigObject;
import it.smartcommunitylab.comuneintasca.core.model.EventObject;
import it.smartcommunitylab.comuneintasca.core.model.HotelObject;
import it.smartcommunitylab.comuneintasca.core.model.ItineraryObject;
import it.smartcommunitylab.comuneintasca.core.model.MainEventObject;
import it.smartcommunitylab.comuneintasca.core.model.POIObject;
import it.smartcommunitylab.comuneintasca.core.model.RestaurantObject;
import it.smartcommunitylab.comuneintasca.core.model.TerritoryServiceObject;
import it.smartcommunitylab.comuneintasca.core.model.TypeConstants;
import it.smartcommunitylab.comuneintasca.core.service.DataService;
import it.smartcommunitylab.comuneintasca.storage.exception.DataException;

/**
 * Process data updates
 * @author raman
 *
 */
@Component("dataProcessor")
public class DataProcessor  {

	@Autowired
	private AppManager appManager;
	@Autowired
	private DataService dataService;

	@Autowired
	private DataExtractor dataExtractor;
	
	@Autowired
	private ConnectorStorage connectorStorage;

	@Autowired
	private ConfigProcessor configProcessor;
	
	private static Logger logger = LoggerFactory.getLogger(DataProcessor.class);

	public void onServiceEvents(String appId, String serviceId, String className, List<ByteString> data) {
		logger.debug("Processing update for {}/{}",serviceId, className);
		App app = appManager.getApp(appId, serviceId, className);
		if (app == null) return;
		logger.debug("-- found app {}",app.getId());
		SourceEntry entry = app.findEntry(serviceId, className);
		logger.debug("-- found source {}", entry);
		try {
				if (ConfigFlow.class.getName().equals(className)) {
					updateConfig(data, app, entry);
				}
				if (EventsFlow.class.getName().equals(className)) {
					updateEvents(data, app, entry);
				}
				if (RestaurantsFlow.class.getName().equals(className)) {
					updateRestaurants(data, app, entry);
				}
				if (HotelsFlow.class.getName().equals(className)) {
					updateHotels(data, app, entry);
				}
				if (CulturaFlow.class.getName().equals(className)) {
					updateCultura(data, app, entry);
				}
				if (TerritoryServicesFlow.class.getName().equals(className)) {
					updateTerritoryServices(data, app, entry);
				}
				if (MainEventsFlow.class.getName().equals(className)) {
					updateMainEvents(data, app, entry);
				}
				if (TextsFlow.class.getName().equals(className)) {
					updateTesti(data, app, entry);
				}
				if (ItinerariesFlow.class.getName().equals(className)) {
					updateItinerari(data, app, entry);
				}
		} catch (Exception e) {
			logger.error("Error updating " + className);
			e.printStackTrace();
		}
	}

	private void updateConfig(List<ByteString> data, App app, SourceEntry entry) throws Exception {
		ConfigData cd = ConfigData.parseFrom(data.get(0));
		String d = cd.getData().replace("\\\"", "");

		ObjectMapper mapper = new ObjectMapper();

		DynamicConfigObject config = mapper.readValue(d, DynamicConfigObject.class);

		logger.info("Updating config");
		try {
			configProcessor.buildConfig(config, app);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return;
		}

		List<DynamicConfigObject> oldList = dataService.getDraftObjects(DynamicConfigObject.class, app.getId());
		DynamicConfigObject old = null;
		if (oldList != null && !oldList.isEmpty()) {
			logger.info("Updating existing config");
			old = oldList.get(0);
			if (old.getLastModified() < cd.getDateModified() || !old.dataEquals(config)) {
				config.setId(old.getId());
				config.setAppId(app.getId());
				config.setLastModified(cd.getDateModified());
				connectorStorage.storeObject(config, app.getId());
				
				// update application
				dataService.upsertObject(config, app.getId());
				logger.info("Stored updated config.");
				if (entry.isAutoPublish()) {
					dataService.publishObject(config, app.getId());
					logger.info("Published updated config.");
				}
				updateAll(config, app);
			}
		} else {
			logger.info("New config");
			config.setAppId(app.getId());
			config.setLastModified(cd.getDateModified());
			connectorStorage.storeObject(config, app.getId());

			// update application
			dataService.upsertObject(config, app.getId());
			logger.info("Stored new config.");
			if (entry.isAutoPublish()) {
				dataService.publishObject(config, app.getId());
				logger.info("Published new config.");
			}
			updateAll(config, app);
		}

	}

	@SuppressWarnings("rawtypes")
	private void updateAll(DynamicConfigObject config, App app) throws DataException {
		Map<String,ObjectFilters> map = configProcessor.constructFilters(config);
		Map<Class, ObjectFilters> classMap = new HashMap<Class, ObjectFilters>();
		List<AppObject> allObjects = connectorStorage.getAllAppObjects(app.getId());
		for (AppObject o : allObjects) {
			if (o instanceof BaseCITObject) {
				if (!classMap.containsKey(o.getClass())) {
					ObjectFilters filters = null;
					MappingDescriptor md = configProcessor.findDescriptor(o.getClass());
					if (md != null) {
						filters = map.get(md.getLocalType());
					}
					classMap.put(o.getClass(), filters);
				}
				Boolean applies = applies((BaseCITObject)o, classMap.get(o.getClass()));
				AppObject draftObject = dataService.getDraftObject(o.getId(), app.getId());
				if (draftObject != null && !applies) {
					dataService.deleteObject(draftObject, app.getId());
					dataService.unpublishObject(draftObject, app.getId());
				} else if (applies) {
					dataService.upsertObject(o, app.getId());
					SourceEntry entry = app.findEntryByType(o.getClass().getName(), ((BaseCITObject) o).getClassifier());
					if (entry.isAutoPublish()) {
						dataService.publishObject(o, app.getId()); 
					}
				}
			}
		}
	}	

	private boolean applies(BaseCITObject o, ObjectFilters filters) {
		if (filters == null) return false;
		return filters.applies(o);
	}

	private ObjectFilters getFilters(String key, App app) throws DataException {
		List<DynamicConfigObject> oldList = connectorStorage.getObjectsByType(DynamicConfigObject.class, app.getId());
		if (oldList != null && oldList.size() > 0) {
			DynamicConfigObject old = oldList.get(0);
			Map<String, ObjectFilters> constructFilters = configProcessor.constructFilters(old);
			return constructFilters.get(key);
		}

		return null;
	}
	
	private <T extends AppObject> Set<String> getOldIds(Class<T> cls, String classifier, String appId) throws DataException {
		List<T> oldList = connectorStorage.getObjectsByType(cls, classifier, appId);
		Set<String> oldIds = new HashSet<String>();
		for (T o : oldList) {
			oldIds.add(o.getId());
		}
		return oldIds;
	}


	private <S,T extends BaseCITObject> void updateData(
			List<ByteString> data, 
			App app, 
			SourceEntry entry, 
			String filterType, 
			Class<T> targetCls,
			Extractor<S, T> extractor) throws Exception {
		String classifier = entry.getClassifier();

		Set<String> oldIds = getOldIds(targetCls, classifier, app.getId());
		List<T> toPublish = new ArrayList<T>();
		ObjectFilters filters = getFilters(filterType, app); 
		for (ByteString bs : data) {
			S bt = extractor.readData(bs);
			String id = extractor.getId(bt);
			oldIds.remove(id);
			T obj = null;
			try {
				obj = connectorStorage.getObjectById(id, targetCls, app.getId());
			} catch (Exception e) {}
			if (obj == null || extractor.isNewer(bt, obj)) {
				obj = extractor.extractData(bt, entry);
				connectorStorage.storeObject(obj, app.getId());
			}
			boolean applies = applies(obj, filters);
			if (applies) {
				toPublish.add(obj);
			}
		}
		dataService.upsertType(toPublish, targetCls, app.getId(), classifier);
		if (entry.isAutoPublish()) {
			dataService.publishType(targetCls, app.getId(), classifier);
		}
		
		for (String s : oldIds) {
			logger.debug("Deleting object " + s);
			connectorStorage.deleteObjectById(s, targetCls, app.getId());
		}

	}
	
	private void updateEvents(List<ByteString> data, App app, SourceEntry entry) throws Exception {
		updateData(data, app, entry, TypeConstants.TYPE_EVENT, EventObject.class, dataExtractor.eventExtractor);
	}

	private void updateRestaurants(List<ByteString> data, App app, SourceEntry entry) throws Exception {
		updateData(data, app, entry, TypeConstants.TYPE_RESTAURANT, RestaurantObject.class, dataExtractor.restaurantExtractor);
	}

	private void updateItinerari(List<ByteString> data, App app, SourceEntry entry) throws Exception {
		updateData(data, app, entry, TypeConstants.TYPE_ITINERARY, ItineraryObject.class, dataExtractor.itineraryExtractor);
	}

	private void updateTesti(List<ByteString> data, App app, SourceEntry entry) throws Exception {
		updateData(data, app, entry, TypeConstants.TYPE_CONTENT, ContentObject.class, dataExtractor.contentExtractor);
	}

	private void updateMainEvents(List<ByteString> data, App app, SourceEntry entry) throws Exception {
		updateData(data, app, entry, TypeConstants.TYPE_MAINEVENT, MainEventObject.class, dataExtractor.mainEventExtractor);
	}

	private void updateCultura(List<ByteString> data, App app, SourceEntry entry) throws Exception {
		updateData(data, app, entry, TypeConstants.TYPE_POI, POIObject.class, dataExtractor.poiExtractor);
	}

	private void updateTerritoryServices(List<ByteString> data, App app, SourceEntry entry) throws Exception {
		updateData(data, app, entry, TypeConstants.TYPE_TERRITORY_SERVICE, TerritoryServiceObject.class, dataExtractor.territoryServiceExtractor);
	}

	private void updateHotels(List<ByteString> data, App app, SourceEntry entry) throws Exception {
		updateData(data, app, entry, TypeConstants.TYPE_HOTEL, HotelObject.class, dataExtractor.hotelExtractor);
	}


}
