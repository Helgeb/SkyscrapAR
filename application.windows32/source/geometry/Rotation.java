package geometry;

import processing.core.PApplet;
import processing.core.PConstants;

abstract class Rotation {

	private float rotation = 0.0f;
	protected PApplet applet;
	
	public Rotation(PApplet applet) {
		this.applet = applet;
	}
	
	public void incRotation(float amount) {
		rotation = calculateIncreasedAngle(rotation, amount);
	}

	public void applyRotation() {
		float ct = PApplet.cos(rotation);
		float st = PApplet.sin(rotation);
		applyRotationMatrix(ct, st);
	}

	protected abstract void applyRotationMatrix(float ct, float st);

	private float calculateIncreasedAngle(float actualAngle, float delta) {
		return adjustAngleToLowerBound(adjustAngleToUpperBound(actualAngle + delta));
	}
	private float adjustAngleToUpperBound(float angle) {
		if (angle < 0)
			angle += PConstants.TWO_PI;
		return angle;
	}
	private float adjustAngleToLowerBound(float angle) {
		if (angle > PConstants.TWO_PI)
			angle -= PConstants.TWO_PI;
		return angle;
	}
}

class XRotation extends Rotation {
	public XRotation(PApplet applet) {
		super(applet);
	}	
	public void applyRotationMatrix(float ct, float st) {
		applet.applyMatrix(ct, 0.0f, st, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -st, 0.0f,
				ct, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
	}
}

class YRotation extends Rotation {
	public YRotation(PApplet applet) {
		super(applet);
	}	
	public void applyRotationMatrix(float ct, float st) {
		applet.applyMatrix(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, ct, -st, 0.0f, 0.0f, st,
				ct, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
	}
}

class ZRotation extends Rotation {
	public ZRotation(PApplet applet) {
		super(applet);
	}	
	public void applyRotationMatrix(float ct, float st) {
		applet.applyMatrix(ct, -st, 0.0f, 0.0f, st, ct, 0.0f, 0.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
	}
}