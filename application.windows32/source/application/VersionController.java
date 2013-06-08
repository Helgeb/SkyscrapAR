package application;

import processing.core.PApplet;

public class VersionController {

	private int currentVersion = 1;
	private double tweeningVersion = currentVersion;
	private double startTweeningVersion = tweeningVersion;
	private int maxVersion = -1;
	private PApplet applet;

	private double TWEENING_TIME_INTERVAL = 1000; // milliseconds
	private int startTime = 0;
	
	public VersionController(PApplet applet, int maxVersion){
		this.maxVersion = maxVersion;
		this.applet = applet;
	}
	
	private void tweenVersion() {
		int time = applet.millis();
		double progress = (time - startTime) / TWEENING_TIME_INTERVAL;
		if (progress > 1.0f)
			progress = 1.0f;
		tweeningVersion = progress * (currentVersion) + (1 - progress) * (startTweeningVersion);
	}
	
	public String getVersionText(CommitLog commitLog) {
		return "v" + currentVersion + " of " + maxVersion + ": " + commitLog.getDate(currentVersion);
	}
	public void incCurrentVersion(int increment) {
		int v = currentVersion + increment;
		if (v < 1)
			v = 1;
		if (v > maxVersion)
			v = maxVersion;

		if (currentVersion != v) {
			currentVersion = v;
			startTime = applet.millis();
			startTweeningVersion = tweeningVersion;
		}
	}

	public double getCurrentVersion() {
		tweenVersion();
		return tweeningVersion;
	}	
}
