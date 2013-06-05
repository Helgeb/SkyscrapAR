package cityItems;

public class ClassVersion {

	private int loc;
	private int methods;
	
	public ClassVersion(int loc, int methods) {
		this.loc = loc;
		this.methods = methods;
	}

	public int getValue(String attr) {
		if (attr.equals("avloc"))
			return loc;
		else
			return methods;
		
	}
}
