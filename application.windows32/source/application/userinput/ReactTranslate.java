package application.userinput;

import application.draw.geometry.CoordinateHandler;

public class ReactTranslate extends UserCommandReaction {

	private CoordinateHandler coordinateHandler;
	private float xTranslation;
	private float yTranslation;
	private float zTranslation;


	public ReactTranslate(UserCommand commandToReactOn, CoordinateHandler coordinateHandler, 
					float xTranslation, float yTranslation, float zTranslation) {
		super(commandToReactOn);
		this.coordinateHandler = coordinateHandler;
		this.xTranslation = xTranslation;
		this.yTranslation = yTranslation;
		this.zTranslation = zTranslation;
	}


	@Override
	public void reactOnCommand(UserCommand command) {
		if (shoudReactOnCommand(command))
			coordinateHandler.incPosition(xTranslation,yTranslation,zTranslation);
	}
}
