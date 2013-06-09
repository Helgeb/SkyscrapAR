package application;

import model.Building;
import model.CityItem;
import model.CityItemCollection;
import picking.Picker;

public class CityPicker {

	private Picker picker;
	private SkyscrapAR skyscrapAR;
	private String pickedObjectString = "";
	private CityItemCollection cityItemCollection;

	public CityPicker(Picker picker, SkyscrapAR skyscrapAR, CityItemCollection cityItemCollection) {
		this.picker = picker;
		this.skyscrapAR = skyscrapAR;
		this.cityItemCollection = cityItemCollection;
	}

	public void start(int index) {
		picker.start(index);
	}

	public void showTextByMousePosition(int x, int y) {
		CityItem item = pickItem(x, y);
		if (item != null)
			pickedObjectString = item.printTitleString();
	}

	public void selectByMousePosition(int x, int y) {
		CityItem item = pickItem(x, y);
		if (item != null && item instanceof Building)
			((Building)item).toggleSelect();
	}

	private CityItem pickItem(int x, int y) {
		int id = picker.get(x, y);
		return cityItemCollection.getItemById(id);
	}

	
	
	public void drawPickedText() {
		skyscrapAR.text(pickedObjectString, 10, 32);		
	}
}
