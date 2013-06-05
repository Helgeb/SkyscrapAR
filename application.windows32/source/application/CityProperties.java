package application;

import java.util.Properties;

import processing.core.PApplet;
import processing.xml.XMLElement;
import treemap.Treemap;

public class CityProperties {
	
	public int TREEMAP_WIDTH = 100;
	public int TREEMAP_HEIGHT = TREEMAP_WIDTH;
	public double PACKAGE_HEIGHT = 1.0f;
	public double PACKAGE_BASE_RATIO = 0.90f;
	public double CLASS_BASE_RATIO = 0.70f;

	public double DEFAULT_CLASS_MIN_HEIGHT = 10.0f;
	public double CLASS_MIN_HEIGHT = DEFAULT_CLASS_MIN_HEIGHT;
	public double CLASS_MAX_HEIGHT = (TREEMAP_WIDTH + TREEMAP_HEIGHT) * 0.6f;

	public float heightScale = 1.0f;
	
	private int windowWitdh;
	private int windowHeight;
	String fileName = "data.xml";
	String fileNameExcluded = "excludedelements.txt";


	public void loadProperties(PApplet applet) {
		try {
			Properties props = new Properties();
			props.load(applet.openStream("conf.properties"));
			windowWitdh = PApplet.parseInt(props.getProperty("env.viewport.width", "640"));
			windowHeight = PApplet.parseInt(props.getProperty("env.viewport.height", "480"));
			fileName = props.getProperty("env.input.data", "data.xml");
			fileNameExcluded = props.getProperty("env.input.excludedelements","excludedelements.txt");
		} catch (Exception e) {
			PApplet.println("couldn't read config file...");
		}
	}

	
	public void setSize(PApplet applet) {
		applet.size(windowWitdh, windowHeight, PApplet.OPENGL);
	}

	public String[] loadExcludedElements(PApplet applet) {
		return applet.loadStrings(fileNameExcluded);
	}

	public XMLElement loadXMLCity(PApplet applet) {
		return new XMLElement(applet, fileName);
	}

	public void setMapLayout(Treemap map) {
		map.updateLayout(-TREEMAP_WIDTH / 2, -TREEMAP_HEIGHT / 2, TREEMAP_WIDTH, TREEMAP_HEIGHT);
	}
}
