package geometry;

import processing.core.PApplet;
import processing.core.PConstants;

abstract class SingleRotation {

	private float rotation = 0.0f;
	protected PApplet applet;
	
	public SingleRotation(PApplet applet) {
		this.applet = applet;
	}
	
	public void incRotation(float amount) {
		rotation += amount;
		adjustAngleToLowerBound();
		adjustAngleToUpperBound();
	}

	public void applyRotation() {
		float ct = PApplet.cos(rotation);
		float st = PApplet.sin(rotation);
		applyRotationMatrix(ct, st);
	}

	protected abstract void applyRotationMatrix(float ct, float st);

	private void adjustAngleToUpperBound() {
		if (rotation < 0)
			rotation += PConstants.TWO_PI;
	}
	private void adjustAngleToLowerBound() {
		if (rotation > PConstants.TWO_PI)
			rotation -= PConstants.TWO_PI;
	}
}

class XRotation extends SingleRotation {
	public XRotation(PApplet applet) {
		super(applet);
	}	
	public void applyRotationMatrix(float ct, float st) {
		applet.applyMatrix(ct, 0.0f, st, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -st, 0.0f,
				ct, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
	}
}

class YRotation extends SingleRotation {
	public YRotation(PApplet applet) {
		super(applet);
	}	
	public void applyRotationMatrix(float ct, float st) {
		applet.applyMatrix(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, ct, -st, 0.0f, 0.0f, st,
				ct, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
	}
}

class ZRotation extends SingleRotation {
	public ZRotation(PApplet applet) {
		super(applet);
	}	
	public void applyRotationMatrix(float ct, float st) {
		applet.applyMatrix(ct, -st, 0.0f, 0.0f, st, ct, 0.0f, 0.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
	}
}