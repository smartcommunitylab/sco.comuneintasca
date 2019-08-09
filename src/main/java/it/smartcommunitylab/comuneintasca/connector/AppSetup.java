package it.smartcommunitylab.comuneintasca.connector;

import java.util.List;

public class AppSetup {

	private List<App> apps;

	public List<App> getApps() {
		return apps;
	}

	public void setApps(List<App> apps) {
		this.apps = apps;
	}

	@Override
	public String toString() {
		return "AppSetup [apps=" + apps + "]";
	}
}
