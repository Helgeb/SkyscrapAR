package application;
import processing.core.*;
import processing.xml.*;

import treemap.*;
import xmlConversion.XMLConverterFactory;
import picking.*;
import processing.opengl.*;
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

	String[] excludedElements;
	boolean SAVE_VIDEO = false;

	public int maxLevel = -1;

	int TREEMAP_WIDTH = 100;
	int TREEMAP_HEIGHT = TREEMAP_WIDTH;

	public double PACKAGE_HEIGHT = 1.0f;

	public double PACKAGE_BASE_RATIO = 0.90f;
	public double CLASS_BASE_RATIO = 0.70f;

	public double DEFAULT_CLASS_MIN_HEIGHT = 10.0f;
	public double CLASS_MIN_HEIGHT = DEFAULT_CLASS_MIN_HEIGHT;
	public double CLASS_MAX_HEIGHT = (TREEMAP_WIDTH + TREEMAP_HEIGHT) * 0.6f;

	float MOUSE_SPEED = 50;
	float TRANS_SPEED = 3;

	double TWEENING_TIME_INTERVAL = 1000; // milliseconds

	public CityColorHandler colorHandler = new CityColorHandler(this);
	public CoordinateHandler coordinateHandler = new CoordinateHandler(this);

	boolean mouseNavigationEnabled = false;
	boolean mouseMovementEnabled = false;

	int lastMouseX;
	int lastMouseY;

	public float heightScale = 1.0f;

	PMatrix3D lastMatrix = new PMatrix3D(0.03271547f, -0.9987524f,
			0.037727464f, 7.3349524f, 0.9948697f, 0.028926386f, -0.09694087f,
			6.203373f, 0.0957286f, 0.040705375f, 0.99457484f, -279.99384f,
			0.0f, 0.0f, 0.0f, 1.0f);

	PFont font = createFont("FFScala", 16);
	PImage myframe;

	Treemap map;
	PackageItem cityParent;
	int globalIndex = 0;
	public LinkedList<CityItem> g_treemapItems = new LinkedList<CityItem>();
	public int g_nSelectedItems = 0;
	public int g_currentVersion = 1;
	public int g_firstVersion = 1;
	public double g_tweeningVersion = g_currentVersion;
	public double g_maxLoc = 0;
	int maxVersion = -1;
	CommitLog commitLog;
	String projectName;
	public int previousVisitedVersion = g_firstVersion;
	public int g_total_loc;
	public int g_total_objects;
	public int g_total_packages;
	public int g_total_subroutines;

	XMLConverterFactory converterFactory;
	String titleString = "";
	public Picker picker;

	public void loadTreemap(String fileName) {
		MapLayout algorithm = new PivotBySplitSize();

		XMLElement elem = new XMLElement(this, fileName);
		XMLElement elemCode = elem.getChild("CodeInfo");
		XMLElement elemLog = elem.getChild("LogInfo");
		projectName = elem.getString("name");

		cityParent = (PackageItem) converterFactory.getPackageItemConverter()
				.convertItem(elemCode, 0);
		maxVersion = elem.getInt("lastVersion");
		commitLog = new CommitLog(elemLog);

		map = new Treemap(cityParent, 0, 0, width, height);
		map.setLayout(algorithm);
		map.updateLayout(-TREEMAP_WIDTH / 2, -TREEMAP_HEIGHT / 2,
				TREEMAP_WIDTH, TREEMAP_HEIGHT);
	}

	public void setup() {
		int windowWitdh = 640;
		int windowHeight = 480;
		String fileName = "data.xml";
		String fileNameExcluded = "excludedelements.txt";
		try {
			Properties props = new Properties();
			props.load(openStream("conf.properties"));
			windowWitdh = PApplet.parseInt(props.getProperty(
					"env.viewport.width", "640"));
			windowHeight = PApplet.parseInt(props.getProperty(
					"env.viewport.height", "480"));
			fileName = props.getProperty("env.input.data", "data.xml");
			fileNameExcluded = props.getProperty("env.input.excludedelements",
					"excludedelements.txt");
		} catch (Exception e) {
			println("couldn't read config file...");
		}
		size(windowWitdh, windowHeight, OPENGL);
		myframe = new PImage(width, height, RGB);
		excludedElements = loadStrings(fileNameExcluded);
		converterFactory = new XMLConverterFactory(excludedElements, this);
		loadTreemap(fileName);
		picker = new Picker(this);

		textFont(font);
		textMode(SCREEN);
	}

	public void fileSelected(File selection) {
		if (selection != null)
			loadTreemap(selection.getName());
	}

	public void drawXmlTreemap3D() {

		lights();
		noStroke();

		pushMatrix();
		translate(0, 0, -6.0f);
		colorHandler.fillFloor();
		noStroke();
		strokeWeight(0);
		box((float) TREEMAP_WIDTH, (float) TREEMAP_HEIGHT, 12.0f);
		popMatrix();

		stroke(0x33000000);
		map.draw();
		noLights();
	}

	public void drawModelCube() {
		fill(0, 0, 255);
		textMode(MODEL);
		fill(255);
		box(90, 90, -0.2f);
		textAlign(CENTER);
		fill(0);
		rotateZ(PI);
		text(projectName, 0, 0);
		textAlign(LEFT);
		textMode(SCREEN);
	}

	public void drawModel() {
		coordinateHandler.applyZoom();
		coordinateHandler.applyXRotation();
		coordinateHandler.applyYRotation();
		coordinateHandler.applyZRotation();
		coordinateHandler.applyTranslation();
		drawXmlTreemap3D();
	}

	int startTime = 0;
	double startTweeningVersion = g_tweeningVersion;

	public void tweenVersion() {
		int time = millis();
		double progress = (time - startTime) / TWEENING_TIME_INTERVAL;
		if (progress > 1.0f)
			progress = 1.0f;
		g_tweeningVersion = progress * (g_currentVersion) + (1 - progress)
				* (startTweeningVersion);
	}

	public void setCurrentVersion(int v) {
		if (v < 1)
			v = 1;
		else if (v > maxVersion)
			v = maxVersion;

		if (g_currentVersion != v) {
			previousVisitedVersion = g_currentVersion;

			g_currentVersion = v;
			startTime = millis();
			startTweeningVersion = g_tweeningVersion;
		}
	}

	public void drawOnLastMarker() {
		pushMatrix();
		resetMatrix();
		applyMatrix(lastMatrix.m00, lastMatrix.m01, lastMatrix.m02,
				lastMatrix.m03, lastMatrix.m10, lastMatrix.m11, lastMatrix.m12,
				lastMatrix.m13, lastMatrix.m20, lastMatrix.m21, lastMatrix.m22,
				lastMatrix.m23, lastMatrix.m30, lastMatrix.m31, lastMatrix.m32,
				lastMatrix.m33);
		drawModel();
		popMatrix();
	}

	public void drawText() {
		colorHandler.fillText();
		text(titleString, 10, 32);
		String mode = "scale=" + heightScale;
		text(mode + "\n" + getVersionText() + "\n" + getInfoText(), 10,
				height - 50);
	}

	public String getInfoText() {
		return "Packages: "
				+ NumberFormat.getInstance().format(g_total_packages)
				+ " Objects: "
				+ NumberFormat.getInstance().format(g_total_objects)
				+ " Subroutines: "
				+ NumberFormat.getInstance().format(g_total_subroutines)
				+ " Total LOC: "
				+ NumberFormat.getInstance().format(g_total_loc);
	}

	public String getVersionText() {
		return "v" + g_currentVersion + " of " + maxVersion + ": "
				+ commitLog.getDate(g_currentVersion);
	}

	public void draw() {
		tweenVersion();
		if (mouseNavigationEnabled) {
			coordinateHandler
					.incXRotation(-((lastMouseY - (float) mouseY) / MOUSE_SPEED));
			lastMouseY = mouseY;
			coordinateHandler.incZRotation((lastMouseX - (float) mouseX)
					/ MOUSE_SPEED);
			lastMouseX = mouseX;
		}
		background(255);
		drawOnLastMarker();
		drawText();
		if (SAVE_VIDEO)
			saveFrame("/output/seq-####.tga");
	}

	public void mouseMoved() {
		int x = mouseX;
		int y = mouseY;

		titleString = "";
		int id = picker.get(x, y);
		if (id > -1 && id < g_treemapItems.size()) {
			CityItem item = g_treemapItems.get(id);
			titleString = item.printTitleString();
		}
	}

	public void mouseClicked() {
		int x = mouseX;
		int y = mouseY;

		int id = picker.get(x, y);
		if (id > -1 && id < g_treemapItems.size()) {
			CityItem item = g_treemapItems.get(id);
			item.toggleSelect(id);
		}
	}

	public Set<CityItem> getSelectedItems() {
		Set<CityItem> selected = new HashSet<CityItem>();
		for (CityItem item : g_treemapItems) {
			if (item.isSelected())
				selected.add(item);
		}
		return selected;
	}

	public void keyPressed() {
		if (key == 'z') {
			coordinateHandler.incZoomFactor(0.1f);
		} else if (key == 'Z') {
			coordinateHandler.incZoomFactor(-0.1f);
		} else if (key == 's') {
			heightScale -= 0.1f;
		} else if (key == 'S') {
			heightScale += 0.1f;
		} else if (key == 'q') {
			mouseNavigationEnabled = !mouseNavigationEnabled;
			lastMouseX = mouseX;
			lastMouseY = mouseY;
		} else if (key == 'Q') {
			mouseMovementEnabled = !mouseMovementEnabled;
			lastMouseX = mouseX;
			lastMouseY = mouseY;
		} else if (key == CODED && keyCode == RIGHT) {
			setCurrentVersion(g_currentVersion + 1);
		} else if (key == CODED && keyCode == LEFT) {
			setCurrentVersion(g_currentVersion - 1);
		} else if (key == CODED && keyCode == DOWN) {
			setCurrentVersion(g_currentVersion - 10);
		} else if (key == CODED && keyCode == UP) {
			setCurrentVersion(g_currentVersion + 10);
		} else if (key == 'p') {
			save("output.png");
		} else if (key == 'x') {
			coordinateHandler.incXPosition(TRANS_SPEED);
		} else if (key == 'X') {
			coordinateHandler.incXPosition(-TRANS_SPEED);
		} else if (key == 'y') {
			coordinateHandler.incYPosition(TRANS_SPEED);
		} else if (key == 'Y') {
			coordinateHandler.incYPosition(-TRANS_SPEED);
		} else if (key == 'c') {
			coordinateHandler.incZPosition(TRANS_SPEED);
		} else if (key == 'C') {
			coordinateHandler.incZPosition(-TRANS_SPEED);
		} else if (key == 'r') {
			SAVE_VIDEO = !SAVE_VIDEO;
			if (SAVE_VIDEO)
				println("Recording active");
		}
	}

	static public void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#F0F0F0", "application.SkyscrapAR" });
	}
}
