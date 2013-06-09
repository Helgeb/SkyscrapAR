package application;

import model.CityItemCollection;
import model.District;
import picking.Picker;
import processing.core.PApplet;
import processing.xml.XMLElement;
import treemap.PivotBySplitSize;
import treemap.Treemap;
import xmlConversion.XMLConverterFactory;
import application.draw.CityDrawer;
import application.draw.DrawController;
import application.draw.color.CityColorHandler;
import application.draw.geometry.CoordinateHandler;


@SuppressWarnings("serial")
public class SkyscrapAR extends PApplet {

	private int TREEMAP_WIDTH = 100;
	private int TREEMAP_HEIGHT = TREEMAP_WIDTH;

	private UserInputHandler userInputHandler;
	private DrawController drawController;

	private XMLConverterFactory converterFactory;

	public void setup() {

		CityItemCollection itemCollection = new CityItemCollection();
		CityProperties cityProperties = new CityProperties();
		cityProperties.loadProperties(this);


		XMLElement elem = cityProperties.loadXMLCity(this);
		XMLElement elemLog = elem.getChild("LogInfo");
		int maxVersion = elem.getInt("lastVersion");
		CommitLog commitLog = new CommitLog(elemLog);
		CityColorHandler colorHandler = new CityColorHandler(this);
		CityPicker cityPicker = new CityPicker(new Picker(this), this, itemCollection);
		CityDrawer cityDrawer = new CityDrawer(this, colorHandler, maxVersion, commitLog, cityPicker, itemCollection);
		String[] excludedElements = cityProperties.loadExcludedElements(this);
		
		converterFactory = new XMLConverterFactory(this, excludedElements);
		
		XMLElement elemCode = elem.getChild("CodeInfo");
		District cityParent = (District) converterFactory.getPackageItemConverter().convertItem(elemCode, 0, cityDrawer, itemCollection);
		
		
		Treemap map = new Treemap(cityParent, 0, 0, width, height);
		map.setLayout(new PivotBySplitSize());
		map.updateLayout(-TREEMAP_WIDTH / 2, -TREEMAP_HEIGHT / 2, TREEMAP_WIDTH, TREEMAP_HEIGHT);

		CoordinateHandler coordinateHandler = new CoordinateHandler(this);
		
		drawController = new DrawController(map, this, colorHandler, coordinateHandler, cityDrawer, cityPicker);
		
		userInputHandler = new UserInputHandler(this, coordinateHandler, cityPicker, cityDrawer);
		cityProperties.setSize(this);
		textFont(createFont("FFScala", 16));
		textMode(SCREEN);
	}

	public void draw() {
		drawController.draw();
	}

	public void mouseMoved() {
		userInputHandler.mouseMoved(mouseX, mouseY);
	}

	public void keyPressed() {
		userInputHandler.keyPressed(key, keyCode);
	}
	
	public void mouseClicked() {
		userInputHandler.mouseClicked(mouseX, mouseY);
	}
	
	static public void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#F0F0F0", "application.SkyscrapAR" });
	}

}
