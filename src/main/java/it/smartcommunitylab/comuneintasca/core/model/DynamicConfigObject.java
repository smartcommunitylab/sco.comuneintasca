/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
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
package it.smartcommunitylab.comuneintasca.core.model;

import java.util.List;

public class DynamicConfigObject extends AppObject {
	private static final long serialVersionUID = 3952436037350859543L;

	private List<MenuItem> highlights;
	private List<MenuItem> navigationItems;
	private List<MenuItem> menu;

	public DynamicConfigObject() {
	}

	public List<MenuItem> getHighlights() {
		return highlights;
	}

	public void setHighlights(List<MenuItem> highlights) {
		this.highlights = highlights;
	}

	public List<MenuItem> getNavigationItems() {
		return navigationItems;
	}

	public void setNavigationItems(List<MenuItem> navigationItems) {
		this.navigationItems = navigationItems;
	}

	public List<MenuItem> getMenu() {
		return menu;
	}

	public void setMenu(List<MenuItem> menu) {
		this.menu = menu;
	}

	
	@Override
	public boolean different(AppObject obj) {
		if (!super.different(obj)) return true;
		if (!(obj instanceof DynamicConfigObject)) return false;
		return !dataEquals((DynamicConfigObject)obj);
	}

	public boolean dataEquals(DynamicConfigObject o) {
		if (highlights.size() != o.getHighlights().size()) return false;
		for (int i = 0; i < highlights.size(); i++) {
			if (!highlights.get(i).sameData(o.getHighlights().get(i))) {
				System.err.println("different highlight "+i);
				return false; 
			}
		}
		if (navigationItems.size() != o.getNavigationItems().size()) return false;
		for (int i = 0; i < navigationItems.size(); i++) {
			if (!navigationItems.get(i).sameData(o.getNavigationItems().get(i))) {
				System.err.println("different navitem "+i);
				return false; 
			}
		}
		if (menu.size() != o.getMenu().size()) return false;
		for (int i = 0; i < menu.size(); i++) {
			if (!menu.get(i).sameData(o.getMenu().get(i))) {
				System.err.println("different menu "+i);
				System.err.println("THIS:"+menu.get(i).toString());
				System.err.println("THAT:"+o.getMenu().get(i).toString());
				return false; 
			}
		}
		return true;
	}
}
