abstract class Rotation {

  private float rotation = 0.0;
  
  void incRotation(float amount) {
    rotation = calculateIncreasedAngle(rotation, amount);
  }
  
  void applyRotation() {
    float ct = cos(rotation);
    float st = sin(rotation);
    applyRotationMatrix(ct, st);
  }
  
  abstract void applyRotationMatrix(float ct, float st);
  
  float calculateIncreasedAngle(float actualAngle, float delta) {
    float result = actualAngle + delta;
    if (result < 0)
      result += TWO_PI;
    else if (result > TWO_PI)
      result -= TWO_PI;
      
    return result;  
  }
}

class XRotation extends Rotation {
  void applyRotationMatrix(float ct, float st) {
    applyMatrix(  ct, 0.0,  st,  0.0,
               0.0, 1.0, 0.0,  0.0,
               -st, 0.0,  ct,  0.0,
               0.0, 0.0, 0.0,  1.0);
  }
}

class YRotation extends Rotation {
  void applyRotationMatrix(float ct, float st) {
  applyMatrix(  1.0, 0.0,  0.0,  0.0,
               0.0, ct, -st,  0.0,
               0.0, st,  ct,  0.0,
               0.0, 0.0, 0.0,  1.0);  
  }
}

class ZRotation extends Rotation {
  void applyRotationMatrix(float ct, float st) {
  applyMatrix(  ct, -st,  0.0,  0.0,
               st, ct, 0.0,  0.0,
               0.0, 0.0,  1.0,  0.0,
               0.0, 0.0, 0.0,  1.0);  
  }
}
