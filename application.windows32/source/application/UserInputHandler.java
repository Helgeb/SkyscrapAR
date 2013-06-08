package application;

import cityItems.CityItem;
import picking.Picker;
import processing.core.PConstants;
import geometry.CoordinateHandler;

public class UserInputHandler {
	
	private boolean mouseNavigationEnabled = false;
	private boolean mouseMovementEnabled = false;

	private float MOUSE_SPEED = 50;
	private float TRANS_SPEED = 3;
	
	private int lastMouseX;
	private int lastMouseY;
	
	private CoordinateHandler coordinateHandler;
	private SkyscrapAR skyscrapAR;
	private Picker picker;
	private DrawController drawController;

	public UserInputHandler(SkyscrapAR skyscrapAR,	CoordinateHandler coordinateHandler, Picker picker, DrawController drawController) {
		this.skyscrapAR = skyscrapAR;
		this.coordinateHandler = coordinateHandler;
		this.picker = picker;
		this.drawController = drawController;
	}

	public void keyPressed(char key, int keyCode) {

		if (key == 'z') {
			coordinateHandler.incZoomFactor(0.1f);
		} else if (key == 'Z') {
			coordinateHandler.incZoomFactor(-0.1f);
		} else if (key == 's') {
			skyscrapAR.heightScale -= 0.1f;
		} else if (key == 'S') {
			skyscrapAR.heightScale += 0.1f;
		} else if (key == 'q') {
			mouseNavigationEnabled = !mouseNavigationEnabled;
			lastMouseX = skyscrapAR.mouseX;
			lastMouseY = skyscrapAR.mouseY;
		} else if (key == 'Q') {
			mouseMovementEnabled = !mouseMovementEnabled;
			lastMouseX = skyscrapAR.mouseX;
			lastMouseY = skyscrapAR.mouseY;
		} else if (key == PConstants.CODED && keyCode == PConstants.RIGHT) {
			drawController.incCurrentVersion(1);
		} else if (key == PConstants.CODED && keyCode == PConstants.LEFT) {
			drawController.incCurrentVersion(- 1);
		} else if (key == PConstants.CODED && keyCode == PConstants.DOWN) {
			drawController.incCurrentVersion(- 10);
		} else if (key == PConstants.CODED && keyCode == PConstants.UP) {
			drawController.incCurrentVersion(+ 10);
		} else if (key == 'p') {
			skyscrapAR.save("output.png");
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

	public void mouseMoved(int x, int y) {

		if (mouseNavigationEnabled) {
			coordinateHandler.incRotation(-((lastMouseY - (float) skyscrapAR.mouseY) / MOUSE_SPEED),0,
										   ((lastMouseX - (float) skyscrapAR.mouseX) / MOUSE_SPEED));
			lastMouseY = skyscrapAR.mouseY;
			lastMouseX = skyscrapAR.mouseX;
		}
		drawController.titleString = "";
		int id = picker.get(x, y);
		if (id > -1) {
		if (id > -1 && id < skyscrapAR.g_treemapItems.size()) {
			CityItem item = skyscrapAR.g_treemapItems.get(id);
			drawController.titleString = item.printTitleString();
		}}
	}

	public void mouseClicked(int x, int y) {
		int id = picker.get(x, y);
		if (id > -1 && id < skyscrapAR.g_treemapItems.size()) {
			CityItem item = skyscrapAR.g_treemapItems.get(id);
			item.toggleSelect(id);
		}

	}

}
