package it.smartcommunitylab.comuneintasca.connector;

public class TypeClassifier {

	private String type;
	private String classifier;
	
	public TypeClassifier(String type, String classifier) {
		super();
		this.type = type;
		this.classifier = classifier;
	}
	public String getType() {
		return type;
	}
	public String getClassifier() {
		return classifier;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((classifier == null) ? 0 : classifier.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypeClassifier other = (TypeClassifier) obj;
		if (classifier == null) {
			if (other.classifier != null)
				return false;
		} else if (!classifier.equals(other.classifier))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	
}
