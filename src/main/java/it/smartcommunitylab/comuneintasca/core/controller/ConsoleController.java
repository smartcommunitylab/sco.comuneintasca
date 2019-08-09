package it.smartcommunitylab.comuneintasca.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.smartcommunitylab.comuneintasca.core.model.AppObject;
import it.smartcommunitylab.comuneintasca.core.model.TypeConstants;
import it.smartcommunitylab.comuneintasca.core.model.app.AppSettings;
import it.smartcommunitylab.comuneintasca.core.security.AppSetup;
import it.smartcommunitylab.comuneintasca.core.security.CustomAuthenticationProvider.AppDetails;
import it.smartcommunitylab.comuneintasca.core.service.DataService;
import it.smartcommunitylab.comuneintasca.storage.exception.DataException;

@Controller
public class ConsoleController {

	@Autowired
	private DataService dataService; 
	@Autowired
	private AppSetup appSetup;
	
	@RequestMapping("/")
	public String home() {
		return "index";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/console/data")
	public @ResponseBody AppSettings getApp() throws DataException {
		String app = getAppId();
		AppSettings settings = appSetup.findAppById(app);
		settings.setTypeStates(dataService.computeTypeStates(app));
		return settings;
	}

	private String getAppId() {
		AppDetails details = (AppDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String app = details.getUsername();
		return app;
	}
	
	
	@RequestMapping(value="/console/publish", method=RequestMethod.PUT)
	public @ResponseBody AppSettings publishApp() throws DataException {
		dataService.publishApp(getAppId());
		return getApp();
	}
	@RequestMapping(value="/console/publish/{type}", method=RequestMethod.PUT)
	public @ResponseBody AppSettings publishAppType(@PathVariable String type) throws DataException {
		dataService.publishType(mapTypeToClass(type), getAppId(), null);
		return getApp();
	}
	
	@SuppressWarnings("unchecked")
	private <T extends AppObject> Class<T> mapTypeToClass(String type) {
		return (Class<T>) TypeConstants.getTypeMapping(type);
	}

}
