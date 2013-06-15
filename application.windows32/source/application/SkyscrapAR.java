package application;

import processing.core.PApplet;
import application.draw.DrawController;
import application.userinput.*;

@SuppressWarnings("serial")
public class SkyscrapAR extends PApplet {

	private UserInputHandler userInputHandler;
	private DrawController drawController;

	public void setup() {
		Setup setup = new Setup();
		setup.doCitySetup(this, width, height);
		drawController = setup.getDrawController();
		userInputHandler = setup.getUserInputHandler();
		textFont(createFont("FFScala", 16));
		textMode(SCREEN);
	}

	public void draw() {
		drawController.draw();
	}

	public void mouseMoved() {
		userInputHandler.mouseMoved(mouseX, mouseY);
	}

	public void keyPressed() {
		userInputHandler.keyPressed(new UserCommand(key, keyCode, mouseX, mouseY));
	}
	
	public void mouseClicked() {
		userInputHandler.mouseClicked(mouseX, mouseY);
	}
	
	static public void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#F0F0F0", "application.SkyscrapAR" });
	}

}
