package application.userinput;

import java.util.Vector;

import application.CityPicker;
import application.VersionController;
import application.draw.CityDrawer;
import application.draw.DrawController;
import application.draw.geometry.CoordinateHandler;
import processing.core.PConstants;

public class UserInputHandler {
	
	private boolean mouseNavigationEnabled = false;

	private float MOUSE_SPEED = 50;
	private float TRANS_SPEED = 3;
	
	private int lastMouseX;
	private int lastMouseY;
	
	private CoordinateHandler coordinateHandler;
	private CityPicker picker;
	private CityDrawer cityDrawer;
	private DrawController drawController;
	private VersionController versionController;
	private Vector<UserCommandReaction> userCommandReations;

	public UserInputHandler(DrawController drawController, CoordinateHandler coordinateHandler, 
						CityPicker picker, CityDrawer cityDrawer, VersionController versionController) {
		this.drawController = drawController;
		this.coordinateHandler = coordinateHandler;
		this.picker = picker;
		this.cityDrawer = cityDrawer;
		this.versionController = versionController;
		userCommandReations = new Vector<UserCommandReaction>();
		userCommandReations.add(new ReactZoom(new UserCommand('z'), coordinateHandler, 0.1f));
		userCommandReations.add(new ReactZoom(new UserCommand('Z'), coordinateHandler, -0.1f));
		userCommandReations.add(new ReactTranslate(new UserCommand('x'), coordinateHandler, TRANS_SPEED,0,0));
		userCommandReations.add(new ReactTranslate(new UserCommand('X'), coordinateHandler, -TRANS_SPEED,0,0));
		userCommandReations.add(new ReactTranslate(new UserCommand('y'), coordinateHandler,0, TRANS_SPEED,0));
		userCommandReations.add(new ReactTranslate(new UserCommand('Y'), coordinateHandler,0, -TRANS_SPEED,0));
		userCommandReations.add(new ReactTranslate(new UserCommand('c'), coordinateHandler,0,0, TRANS_SPEED));
		userCommandReations.add(new ReactTranslate(new UserCommand('C'), coordinateHandler,0,0, -TRANS_SPEED));
	}

	public void keyPressed(UserCommand userCommand) {
		for (UserCommandReaction reaction: userCommandReations) 
			reaction.reactOnMatchingCommand(userCommand);
		
		if (userCommand.equals(new UserCommand('s'))) {
			cityDrawer.incHeightScale(-0.1f);
		} else if (userCommand.equals(new UserCommand('S'))) {
			cityDrawer.incHeightScale(0.1f);
		} else if (userCommand.equals(new UserCommand('q'))) {
			mouseNavigationEnabled = !mouseNavigationEnabled;
			lastMouseX = userCommand.mouseX;
			lastMouseY = userCommand.mouseY;
		} else if (userCommand.equals(new UserCommand((char)PConstants.CODED, PConstants.RIGHT))) {
			versionController.incCurrentVersion(1);
		} else if (userCommand.equals(new UserCommand((char)PConstants.CODED, PConstants.LEFT))) {
			versionController.incCurrentVersion(-1);
		} else if (userCommand.equals(new UserCommand((char)PConstants.CODED, PConstants.DOWN))) {
			versionController.incCurrentVersion(-10);
		} else if (userCommand.equals(new UserCommand((char)PConstants.CODED, PConstants.UP))) {
			versionController.incCurrentVersion(10);
		} else if (userCommand.equals(new UserCommand('p'))) {
			drawController.saveScreenshot("output.png");
		} else if (userCommand.equals(new UserCommand('r'))) {
			drawController.toggleRecording();
		}
	}

	public void mouseMoved(int mouseX, int mouseY) {
		if (mouseNavigationEnabled) {
			coordinateHandler.incRotation(-((lastMouseY - (float) mouseY) / MOUSE_SPEED),0,
										   ((lastMouseX - (float) mouseX) / MOUSE_SPEED));
			lastMouseY = mouseY;
			lastMouseX = mouseX;
		}
		picker.showTextByMousePosition(mouseX, mouseY);
	}

	public void mouseClicked(int mouseX, int mouseY) {
		picker.selectByMousePosition(mouseX, mouseY);
	}

}
