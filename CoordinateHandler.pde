class CoordinateHandler {

private Rotation xRotation = new XRotation();
private Rotation yRotation = new YRotation();
private Rotation zRotation = new ZRotation();
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

void incZoomFactor(float amount) {
  zoomFactor.incZoomFactor(amount);
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
