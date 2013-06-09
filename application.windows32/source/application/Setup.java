package application;

import model.*;
import picking.Picker;
import processing.xml.XMLElement;
import treemap.*;
import xmlConversion.XMLConverterFactory;
import application.draw.*;
import application.draw.color.CityColorHandler;
import application.draw.geometry.CoordinateHandler;

public class Setup {

	private int TREEMAP_WIDTH = 100;
	private int TREEMAP_HEIGHT = TREEMAP_WIDTH;
	private UserInputHandler userInputHandler;
	private DrawController drawController;

	public UserInputHandler getUserInputHandler() {
		return userInputHandler;
	}

	public DrawController getDrawController() {
		return drawController;
	}

	public void doCitySetup(SkyscrapAR skyscrapAR, int width, int height) {
		CityProperties cityProperties = loadCityProperties(skyscrapAR);
		CityItemCollection itemCollection = new CityItemCollection();
		XMLElement elem = cityProperties.loadXMLCity(skyscrapAR);
		XMLElement elemLog = elem.getChild("LogInfo");
		int maxVersion = elem.getInt("lastVersion");
		CommitLog commitLog = new CommitLog(elemLog);
		CityColorHandler colorHandler = new CityColorHandler(skyscrapAR);
		CityPicker cityPicker = new CityPicker(new Picker(skyscrapAR), skyscrapAR, itemCollection);
		VersionController versionController = new VersionController(skyscrapAR, maxVersion);
		CityDrawer cityDrawer = new CityDrawer(skyscrapAR, colorHandler,cityPicker, itemCollection, versionController);
		String[] excludedElements = cityProperties.loadExcludedElements(skyscrapAR);
		XMLConverterFactory converterFactory = new XMLConverterFactory(skyscrapAR);
		XMLElement elemCode = elem.getChild("CodeInfo");
		District cityParent = (District) converterFactory.getPackageItemConverter(excludedElements).convertItem(elemCode, 0, cityDrawer, itemCollection);
		Treemap map = new Treemap(cityParent, 0, 0, width, height);
		map.setLayout(new PivotBySplitSize());
		map.updateLayout(-TREEMAP_WIDTH / 2, -TREEMAP_HEIGHT / 2, TREEMAP_WIDTH, TREEMAP_HEIGHT);
		CoordinateHandler coordinateHandler = new CoordinateHandler(skyscrapAR);
		TextDrawer textDrawer = new TextDrawer(skyscrapAR, commitLog, itemCollection, versionController);
		drawController = new DrawController(map, skyscrapAR, colorHandler, coordinateHandler, 
														cityPicker, textDrawer);
		userInputHandler = new UserInputHandler(drawController, coordinateHandler, cityPicker, cityDrawer, versionController);
		cityProperties.setSize(skyscrapAR);
	}

	private CityProperties loadCityProperties(SkyscrapAR skyscrapAR) {
		CityProperties cityProperties = new CityProperties();
		cityProperties.loadProperties(skyscrapAR);
		return cityProperties;
	}
	
}
