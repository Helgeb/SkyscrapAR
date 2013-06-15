package application.draw;

import model.City;
import application.CommitLog;
import application.SkyscrapAR;
import application.VersionController;

public class TextDrawer {
	
	private CommitLog commitLog;
	private SkyscrapAR skyscrapAR;
	private VersionController versionController;
	private City city;
	
	public TextDrawer(SkyscrapAR skyscrapAR, CommitLog commitLog, City city, VersionController versionController) {
		this.skyscrapAR = skyscrapAR;
		this.commitLog = commitLog;
		this.city = city;
		this.versionController = versionController;
	}	
	
	public void drawTitleText() {
		skyscrapAR.text(getVersionText() + "\n" + city.getInfoText(), 10, skyscrapAR.height - 50);
	}
	
	private String getVersionText() {
		return versionController.getVersionText(commitLog);
	}
}
