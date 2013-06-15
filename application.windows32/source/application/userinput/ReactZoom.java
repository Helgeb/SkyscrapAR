package application.userinput;

import application.draw.geometry.CoordinateHandler;

public class ReactZoom extends UserCommandReaction {

	private CoordinateHandler coordinateHandler;
	private float zoomIncrement;

	public ReactZoom(UserCommand commandToReactOn, CoordinateHandler coordinateHandler, float zoomIncrement) {
		super(commandToReactOn);
		this.coordinateHandler = coordinateHandler;
		this.zoomIncrement = zoomIncrement;
	}

	@Override
	public void reactOnCommand(UserCommand command) {
		coordinateHandler.incZoomFactor(zoomIncrement);
	}

}
