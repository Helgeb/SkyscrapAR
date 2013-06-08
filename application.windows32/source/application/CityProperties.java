package application;

import java.util.Properties;

import processing.core.PApplet;
import processing.xml.XMLElement;
import treemap.Treemap;

public class CityProperties {
	

	private int windowWitdh;
	private int windowHeight;
	private String fileName = "data.xml";
	private String fileNameExcluded = "excludedelements.txt";


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


}
