package application.draw;

import model.CityItemCollection;
import application.CommitLog;
import application.SkyscrapAR;
import application.VersionController;

public class TextDrawer {
	
	private CommitLog commitLog;
	private SkyscrapAR skyscrapAR;
	private VersionController versionController;
	private CityItemCollection itemCollection;
	
	public TextDrawer(SkyscrapAR skyscrapAR, CommitLog commitLog, CityItemCollection itemCollection,
			VersionController versionController) {
		this.skyscrapAR = skyscrapAR;
		this.commitLog = commitLog;
		this.itemCollection = itemCollection;
		this.versionController = versionController;
	}	
	
	public void drawTitleText() {
		skyscrapAR.text(getVersionText() + "\n" + itemCollection.getInfoText(), 
							10,	skyscrapAR.height - 50);
	}
	
	private String getVersionText() {
		return versionController.getVersionText(commitLog);
	}
}
