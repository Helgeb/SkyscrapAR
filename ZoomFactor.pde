class ZoomFactor {

private float zoomFactor = 1.75;

public ZoomFactor(float zoomFactor) {
  this.zoomFactor = zoomFactor;  
}

void applyZoom() {
  applyMatrix(
    zoomFactor, 0, 0, 0,
    0, zoomFactor, 0, 0,
    0, 0, zoomFactor, 0,
    0, 0, 0, 1);
}


private void setZoomFactor(float factor) {
  zoomFactor = factor;
  adjustFactorToUpperBound();
  adjustFactorToLowerBound();
}

private void adjustFactorToUpperBound() {
  if (zoomFactor > 5.0)
    zoomFactor = 5.0;
}

private void adjustFactorToLowerBound() {
  if (zoomFactor < 0.1)
    zoomFactor = 0.1;
}

void incZoomFactor(float amount) {
  setZoomFactor(zoomFactor + amount);
  println("zoom = " + zoomFactor);
}
} 
