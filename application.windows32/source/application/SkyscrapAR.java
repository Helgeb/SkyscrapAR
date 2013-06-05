package application;
import processing.core.*;
import processing.xml.*;

import treemap.*;
import xmlConversion.XMLConverterFactory;
import picking.*;
import geometry.CoordinateHandler;
import java.util.Properties;
import java.io.*;
import java.text.*;
import java.util.*;

import color.CityColorHandler;

import cityItems.CityItem;
import cityItems.PackageItem;

@SuppressWarnings("serial")
public class SkyscrapAR extends PApplet {

	public int maxLevel = -1;
	public String titleString = "";
	public LinkedList<CityItem> g_treemapItems = new LinkedList<CityItem>();
	public double g_maxLoc = 0;
	public int g_total_loc;
	public int g_total_objects;
	public int g_total_packages;
	public int g_total_subroutines;
	public CityColorHandler colorHandler;
	public CoordinateHandler coordinateHandler;
	private UserInputHandler userInputHandler;
	private DrawController drawController;
	private int maxVersion;

	public Treemap map;
	CommitLog commitLog;

	private XMLConverterFactory converterFactory;

	public void loadTreemap(CityProperties cityProperties) {
		XMLElement elem = cityProperties.loadXMLCity(this);
		XMLElement elemCode = elem.getChild("CodeInfo");
		XMLElement elemLog = elem.getChild("LogInfo");
		PackageItem cityParent = (PackageItem) converterFactory.getPackageItemConverter().convertItem(elemCode, 0);
		maxVersion = elem.getInt("lastVersion");
		commitLog = new CommitLog(elemLog);
		map = new Treemap(cityParent, 0, 0, width, height);
		map.setLayout(new PivotBySplitSize());
		cityProperties.setMapLayout(map);
	}

	public void setup() {
		CityProperties cityProperties = new CityProperties();
		cityProperties.loadProperties(this);


		String[] excludedElements = cityProperties.loadExcludedElements(this);
		
		Picker picker = new Picker(this);
		converterFactory = new XMLConverterFactory(this, excludedElements, picker);
		colorHandler = new CityColorHandler(this);
		coordinateHandler = new CoordinateHandler(this);
		drawController = new DrawController(this, colorHandler,cityProperties.TREEMAP_HEIGHT, cityProperties.TREEMAP_WIDTH, maxVersion);
		userInputHandler = new UserInputHandler(this, coordinateHandler, picker, drawController);
		
		loadTreemap(cityProperties);
		
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
