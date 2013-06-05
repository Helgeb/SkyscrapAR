package color;

import processing.core.PApplet;

public class CityColorHandler {
	
	private PApplet applet;
	
	public CityColorHandler(PApplet applet) {
		this.applet = applet;
	}
	
	private void fillColor(int color) {
		(new CityColor(color, applet)).fillColor();		
	}

	public void fillFloor() {
		fillColor(0xff000000);
	}

	public void fillText() {
		fillColor(0xff000000);
	}

	public void fillClassFloor() {
		fillColor(0xff009900);
	}

	public void fillClass(boolean isSelected, String type) {
		if (isSelected)
			fillColor(0xffFFFF99);
		else if (type.equals("CLAS"))
			fillColor(0xff6666CC);
		else
			fillColor(0xff009999);
	}

	public void fillPackageColor(float fracLevel) {
		CityColor packageMinColor = new CityColor(0xff000000, applet);
		CityColor packageMaxColor = new CityColor(0xffFFFFFF, applet);
		(packageMinColor.add(packageMaxColor, fracLevel)).fillColor();
	}
}