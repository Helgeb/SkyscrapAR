package application;

import application.draw.CityDrawer;
import application.draw.DrawController;
import application.draw.geometry.CoordinateHandler;
import processing.core.PConstants;

public class UserInputHandler {
	
	private boolean mouseNavigationEnabled = false;
	private boolean mouseMovementEnabled = false;

	private float MOUSE_SPEED = 50;
	private float TRANS_SPEED = 3;
	
	private int lastMouseX;
	private int lastMouseY;
	
	private CoordinateHandler coordinateHandler;
	private CityPicker picker;
	private CityDrawer cityDrawer;
	private DrawController drawController;
	private VersionController versionController;

	public UserInputHandler(DrawController drawController, CoordinateHandler coordinateHandler, 
						CityPicker picker, CityDrawer cityDrawer, VersionController versionController) {
		this.drawController = drawController;
		this.coordinateHandler = coordinateHandler;
		this.picker = picker;
		this.cityDrawer = cityDrawer;
		this.versionController = versionController;
	}

	public void keyPressed(char key, int keyCode, int mouseX, int mouseY) {

		if (key == 'z') {
			coordinateHandler.incZoomFactor(0.1f);
		} else if (key == 'Z') {
			coordinateHandler.incZoomFactor(-0.1f);
		} else if (key == 's') {
			cityDrawer.incHeightScale(-0.1f);
		} else if (key == 'S') {
			cityDrawer.incHeightScale(0.1f);
		} else if (key == 'q') {
			mouseNavigationEnabled = !mouseNavigationEnabled;
			lastMouseX = mouseX;
			lastMouseY = mouseY;
		} else if (key == 'Q') {
			mouseMovementEnabled = !mouseMovementEnabled;
			lastMouseX = mouseX;
			lastMouseY = mouseY;
		} else if (key == PConstants.CODED && keyCode == PConstants.RIGHT) {
			versionController.incCurrentVersion(1);
		} else if (key == PConstants.CODED && keyCode == PConstants.LEFT) {
			versionController.incCurrentVersion(-1);
		} else if (key == PConstants.CODED && keyCode == PConstants.DOWN) {
			versionController.incCurrentVersion(-10);
		} else if (key == PConstants.CODED && keyCode == PConstants.UP) {
			versionController.incCurrentVersion(10);
		} else if (key == 'p') {
			drawController.saveScreenshot("output.png");
		} else if (key == 'x') {
			coordinateHandler.incPosition(TRANS_SPEED,0,0);
		} else if (key == 'X') {
			coordinateHandler.incPosition(-TRANS_SPEED,0,0);
		} else if (key == 'y') {
			coordinateHandler.incPosition(0,TRANS_SPEED,0);
		} else if (key == 'Y') {
			coordinateHandler.incPosition(0,-TRANS_SPEED,0);
		} else if (key == 'c') {
			coordinateHandler.incPosition(0,0,TRANS_SPEED);
		} else if (key == 'C') {
			coordinateHandler.incPosition(0,0,-TRANS_SPEED);
		} else if (key == 'r') {
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
