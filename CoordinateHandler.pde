class CoordinateHandler {

private Rotation xRotation = new XRotation();
private Rotation yRotation = new YRotation();
private Rotation zRotation = new ZRotation();
private float xTranslation = 0;
private float yTranslation = 0;
private float zTranslation = 0;
private ZoomFactor zoomFactor = new ZoomFactor(1.75);

void applyZoom() {
    zoomFactor.applyZoom();
}

void applyXRotation() {
    xRotation.applyRotation();
}

void applyYRotation() {
    yRotation.applyRotation();
}

void applyZRotation() {
    zRotation.applyRotation();
}

void applyTranslation() {
  translate(xTranslation,yTranslation,zTranslation);
}

void incZoomFactor(float amount) {
  zoomFactor.incZoomFactor(amount);
}

void incXPosition(float amount) {
  xTranslation += amount;
}

void incYPosition(float amount) {
  yTranslation += amount;
}
void incZPosition(float amount) {
  zTranslation += amount;
}

void incXRotation(float amount) {
  xRotation.incRotation(amount);
}

void incYRotation(float amount) {
  yRotation.incRotation(amount);
}

void incZRotation(float amount) {
  zRotation.incRotation(amount);
}
} 
