package application;

import java.text.NumberFormat;

import color.CityColorHandler;

import processing.core.PMatrix3D;
import treemap.Treemap;

public class DrawController {

	private SkyscrapAR skyscrapAR;
	private boolean recordingActive = false;

	private double TWEENING_TIME_INTERVAL = 1000; // milliseconds
	private float heightScale = 1.0f;

	int startTime = 0;
	public int g_currentVersion = 1;
	public int g_firstVersion = 1;
	public double g_tweeningVersion = g_currentVersion;
	double startTweeningVersion = g_tweeningVersion;
	public int previousVisitedVersion = g_firstVersion;
	private int maxVersion = -1;
	public String titleString = "";
	
	private PMatrix3D lastMatrix = new PMatrix3D(0.03271547f, -0.9987524f, 0.037727464f, 7.3349524f, 0.9948697f, 0.028926386f, -0.09694087f,
												 6.203373f, 0.0957286f, 0.040705375f, 0.99457484f, -279.99384f, 0.0f, 0.0f, 0.0f, 1.0f);
	private CityColorHandler colorHandler;
	private int treemapHeight;
	private int treemapWidth;
	private CommitLog commitLog;
	private Treemap map;
	
	public DrawController(SkyscrapAR skyscrapAR, CityColorHandler colorHandler, int treemapHeight, int treemapWidth, int maxVersion,
			CommitLog commitLog, Treemap map) {
		this.skyscrapAR = skyscrapAR;
		this.colorHandler = colorHandler;
		this.treemapHeight = treemapHeight;
		this.treemapWidth = treemapWidth;
		this.maxVersion = maxVersion;
		this.commitLog = commitLog;
		this.map = map;
	}

	public void draw() {
		tweenVersion();
		skyscrapAR.background(255);
		drawOnLastMarker();
		drawText();
		if (recordingActive)
			skyscrapAR.saveFrame("/output/seq-####.tga");
	}

	public void drawXmlTreemap3D() {
		skyscrapAR.lights();
		skyscrapAR.noStroke();
		skyscrapAR.pushMatrix();
		skyscrapAR.translate(0, 0, -6.0f);
		colorHandler.fillFloor();
		skyscrapAR.noStroke();
		skyscrapAR.strokeWeight(0);
		skyscrapAR.box((float) treemapWidth, (float) treemapHeight, 12.0f);
		skyscrapAR.popMatrix();
		skyscrapAR.stroke(0x33000000);
		map.draw();
		skyscrapAR.noLights();
	}

	public void drawModel() {
		skyscrapAR.coordinateHandler.applyTransformation();
		drawXmlTreemap3D();
	}

	public void tweenVersion() {
		int time = skyscrapAR.millis();
		double progress = (time - startTime) / TWEENING_TIME_INTERVAL;
		if (progress > 1.0f)
			progress = 1.0f;
		g_tweeningVersion = progress * (g_currentVersion) + (1 - progress) * (startTweeningVersion);
	}
	public void drawOnLastMarker() {
		skyscrapAR.pushMatrix();
		skyscrapAR.resetMatrix();
		skyscrapAR.applyMatrix(lastMatrix.m00, lastMatrix.m01, lastMatrix.m02,
				lastMatrix.m03, lastMatrix.m10, lastMatrix.m11, lastMatrix.m12,
				lastMatrix.m13, lastMatrix.m20, lastMatrix.m21, lastMatrix.m22,
				lastMatrix.m23, lastMatrix.m30, lastMatrix.m31, lastMatrix.m32,
				lastMatrix.m33);
		drawModel();
		skyscrapAR.popMatrix();
	}
	
	private String getInfoText() {
		return "Packages: "
				+ NumberFormat.getInstance().format(skyscrapAR.g_total_packages)
				+ " Objects: "
				+ NumberFormat.getInstance().format(skyscrapAR.g_total_objects)
				+ " Subroutines: "
				+ NumberFormat.getInstance().format(skyscrapAR.g_total_subroutines)
				+ " Total LOC: "
				+ NumberFormat.getInstance().format(skyscrapAR.g_total_loc);
	}
	
	public void drawText() {
		skyscrapAR.colorHandler.fillText();
		skyscrapAR.text(titleString, 10, 32);
		String mode = "scale=" + heightScale;
		skyscrapAR.text(mode + "\n" + getVersionText() + "\n" + getInfoText(), 10,	skyscrapAR.height - 50);
	}
	
	public String getVersionText() {
		return "v" + g_currentVersion + " of " + maxVersion + ": " + commitLog.getDate(g_currentVersion);
	}
	
	public void incCurrentVersion(int increment) {
		int v = g_currentVersion + increment;
		if (v < 1)
			v = 1;
		if (v > maxVersion)
			v = maxVersion;

		if (g_currentVersion != v) {
			previousVisitedVersion = g_currentVersion;
			g_currentVersion = v;
			startTime = skyscrapAR.millis();
			startTweeningVersion = g_tweeningVersion;
		}
	}
	
	public void toggleRecording() {
		recordingActive = !recordingActive;		
	}
	
}
