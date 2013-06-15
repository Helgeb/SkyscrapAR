package model;

public class BuildingVersion {

	private int height;
	private int groundSize;
	
	public BuildingVersion(int height, int groundSize) {
		this.height = height;
		this.groundSize = groundSize;
	}

	public int getValue(String attr) {
		if (attr.equals("avloc"))
			return height;
		else
			return groundSize;
		
	}
}
