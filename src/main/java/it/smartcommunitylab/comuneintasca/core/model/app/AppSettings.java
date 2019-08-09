package it.smartcommunitylab.comuneintasca.core.model.app;

import java.util.List;

public class AppSettings {

    private String id;
    private String password;

    private List<TypeState> typeStates;
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public List<TypeState> getTypeStates() {
		return typeStates;
	}

	public void setTypeStates(List<TypeState> typeStates) {
		this.typeStates = typeStates;
	}
}
