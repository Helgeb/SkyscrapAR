package application;
import java.util.LinkedList;

import model.CityItem;
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

	public int TREEMAP_WIDTH = 100;
	public int TREEMAP_HEIGHT = TREEMAP_WIDTH;
	public double PACKAGE_HEIGHT = 1.0f;
	public double PACKAGE_BASE_RATIO = 0.90f;
	public double CLASS_BASE_RATIO = 0.70f;

	public double DEFAULT_CLASS_MIN_HEIGHT = 10.0f;
	public double CLASS_MIN_HEIGHT = DEFAULT_CLASS_MIN_HEIGHT;
	public double CLASS_MAX_HEIGHT = (TREEMAP_WIDTH + TREEMAP_HEIGHT) * 0.6f;

	public float heightScale = 1.0f;
	
	public LinkedList<CityItem> g_treemapItems = new LinkedList<CityItem>();
	public double g_maxLoc = 0;
	public int g_total_loc;
	public int g_total_objects;
	public int g_total_packages;
	public int g_total_subroutines;
	public CityColorHandler colorHandler;
	public CoordinateHandler coordinateHandler;
	private UserInputHandler userInputHandler;
	public DrawController drawController;

	private XMLConverterFactory converterFactory;

	public void setup() {
		CityProperties cityProperties = new CityProperties();
		cityProperties.loadProperties(this);


		XMLElement elem = cityProperties.loadXMLCity(this);
		XMLElement elemLog = elem.getChild("LogInfo");
		int maxVersion = elem.getInt("lastVersion");
		CommitLog commitLog = new CommitLog(elemLog);
		colorHandler = new CityColorHandler(this);
		CityDrawer cityDrawer = new CityDrawer(this, colorHandler, maxVersion, commitLog);
		String[] excludedElements = cityProperties.loadExcludedElements(this);
		
		CityPicker cityPicker = new CityPicker(new Picker(this), this, cityDrawer);
		converterFactory = new XMLConverterFactory(this, excludedElements, cityPicker);
		
		XMLElement elemCode = elem.getChild("CodeInfo");
		District cityParent = (District) converterFactory.getPackageItemConverter().convertItem(elemCode, 0, cityDrawer, colorHandler);
		
		
		Treemap map = new Treemap(cityParent, 0, 0, width, height);
		map.setLayout(new PivotBySplitSize());
		map.updateLayout(-TREEMAP_WIDTH / 2, -TREEMAP_HEIGHT / 2, TREEMAP_WIDTH, TREEMAP_HEIGHT);

		coordinateHandler = new CoordinateHandler(this);
		
		drawController = new DrawController(map, this, colorHandler, coordinateHandler, cityDrawer);
		
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
