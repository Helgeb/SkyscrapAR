package application;
import processing.xml.XMLElement;

public class CommitLog {
	XMLElement elem;

	public CommitLog(XMLElement elem) {
		this.elem = elem;
	}

	private XMLElement getVersion(int version) {
		XMLElement[] children = this.elem.getChildren();
		return children[fitVersionToBound(version, children.length)];
	}

	private int fitVersionToBound(int version, int childrenLength) {
		if (version > childrenLength)
			version = childrenLength;
		if (version < 1)
			version = 1;
		return version - 1;
	}

	public String getDate(int currentVersion) {
		return getVersion(currentVersion).getString("date");
	}
}
