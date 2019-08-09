package it.smartcommunitylab.comuneintasca.core.model.app;

/**
 * Object type statistics. TotalObjects stands for total draft objects.
 * Total published objects is calculated as totalObjects + deletedObjects - newObjects;
 * 
 * 
 * @author raman
 *
 */
public class TypeState {

	private String type;
	private int totalObjects;
	private int newObjects;
	private int deletedObjects;
	private int changedObjects;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTotalObjects() {
		return totalObjects;
	}
	public void setTotalObjects(int totalObjects) {
		this.totalObjects = totalObjects;
	}
	public int getNewObjects() {
		return newObjects;
	}
	public void setNewObjects(int newObjects) {
		this.newObjects = newObjects;
	}
	public int getDeletedObjects() {
		return deletedObjects;
	}
	public void setDeletedObjects(int deletedObjects) {
		this.deletedObjects = deletedObjects;
	}
	public int getChangedObjects() {
		return changedObjects;
	}
	public void setChangedObjects(int changedObjects) {
		this.changedObjects = changedObjects;
	}
}
