String INPUT_FILENAME = "data.xml";

int WINDOW_WIDTH = 1000; //640;
int WINDOW_HEIGHT = 750; //480;

int TREEMAP_WIDTH = 100;
int TREEMAP_HEIGHT = TREEMAP_WIDTH;

color PACKAGE_MIN_COLOR = #000000;
color PACKAGE_MAX_COLOR = #FFFFFF;
color CLASS_FLOOR_COLOR = #009900;
color CLASS_CHANGED_COLOR = #990000;
color CLASS_DEFAULT_COLOR = #CCCCCC;
color CLASS_HIGHLIGHT_COLOR = #FFFF99;
color FLOOR_COLOR = #000000;
color TEXT_COLOR = #000000;

double PACKAGE_HEIGHT = 2.0;

double PACKAGE_BASE_RATIO = 0.90;
double CLASS_BASE_RATIO = 0.70;

double DEFAULT_CLASS_MIN_HEIGHT = 1.0;
double CLASS_MIN_HEIGHT = DEFAULT_CLASS_MIN_HEIGHT;
double CLASS_MAX_HEIGHT = (TREEMAP_WIDTH + TREEMAP_HEIGHT) * 0.6;

float MOUSE_SPEED = 50;

float zoomFactor = 1.2;
float xRotation = 0.0;
float yRotation = 0.0;
float zRotation = 0.0;

boolean mouseNavigationEnabled = false;
int lastMouseX;
int lastMouseY;

float heightScale = 1.0;

import treemap.*;
import picking.*;
import processing.opengl.*;

PMatrix3D lastMatrix = new PMatrix3D(0.03271547,-0.9987524,0.037727464,
                                     7.3349524,0.9948697,0.028926386,
                                     -0.09694087,6.203373,0.0957286,
                                     0.040705375,0.99457484,-279.99384,
                                     0.0,0.0,0.0,1.0);

PFont font=createFont("FFScala", 16);
PImage myframe;

Treemap map;
PackageItem mapModel;
int globalIndex = 0;
LinkedList<ClassItem> g_treemapItems = new LinkedList<ClassItem>();
int g_nSelectedItems = 0;
int g_currentVersion = 1;
int g_firstVersion = 1;
double g_maxLoc = 0;
int maxVersion = -1;
CommitLog commitLog;
String projectName;

String titleString = "";
Picker picker;

void loadTreemap() {
  MapLayout algorithm = new PivotBySplitSize();

  XMLElement elem = new XMLElement(this, INPUT_FILENAME);
  XMLElement elemCode = elem.getChild("CodeInfo");
  XMLElement elemLog = elem.getChild("LogInfo");
  projectName = elem.getString("name");
  
  mapModel = new PackageItem(null, elemCode, 0);
  maxVersion = elem.getInt("lastVersion");
  commitLog = new CommitLog(elemLog);

  map = new Treemap(mapModel, 0, 0, width, height);
  map.setLayout(algorithm);
  map.updateLayout(-TREEMAP_WIDTH/2, -TREEMAP_HEIGHT/2, TREEMAP_WIDTH, TREEMAP_HEIGHT);
}

void setup() {
  size(WINDOW_WIDTH, WINDOW_HEIGHT,OPENGL);
  myframe = new PImage(width, height, RGB);

  loadTreemap();
  picker = new Picker(this);
  
  textFont(font);
  textMode(SCREEN);
}

//*******************************************************/
// Drawing
//*******************************************************/

void drawXmlTreemap3D() {
  picker.start(32767);
  lights();
  noStroke();
  
  pushMatrix();
  translate(0, 0, -6.0f);
  fill(FLOOR_COLOR);
  noStroke();
  strokeWeight(0);
  box((float)TREEMAP_WIDTH, (float)TREEMAP_HEIGHT, 12.0f);
  popMatrix();
  
  stroke(0x33000000);
  map.draw();
  noLights();
}

void drawModelCube() {
  fill(0,0,255);  
  textMode(MODEL);
  fill(255);
  box(90, 90, -0.2);
  textAlign(CENTER);
  fill(0);
  rotateZ(PI);
  text(projectName, 0, 0);
  textAlign(LEFT);
  textMode(SCREEN);
}

void drawModel() {
  applyZoom(zoomFactor);
  applyXRotation(xRotation);
  applyYRotation(yRotation);
  applyZRotation(zRotation);
  drawXmlTreemap3D();
}

void setCurrentVersion(int v) {
  if (v < 1)
    v = 1;
  else if (v > maxVersion)
    v = maxVersion;
    g_currentVersion = v;
}

void applyZoom(float s) {
  applyMatrix(
    s, 0, 0, 0,
    0, s, 0, 0,
    0, 0, s, 0,
    0, 0, 0, 1);
}

void applyXRotation(float angle) {
  float ct = cos(angle);
  float st = sin(angle);
  applyMatrix(  ct, 0.0,  st,  0.0,
               0.0, 1.0, 0.0,  0.0,
               -st, 0.0,  ct,  0.0,
               0.0, 0.0, 0.0,  1.0);
}

void applyYRotation(float angle) {
  float ct = cos(angle);
  float st = sin(angle);
  applyMatrix(  1.0, 0.0,  0.0,  0.0,
               0.0, ct, -st,  0.0,
               0.0, st,  ct,  0.0,
               0.0, 0.0, 0.0,  1.0);
}

void applyZRotation(float angle) {
  float ct = cos(angle);
  float st = sin(angle);
  applyMatrix(  ct, -st,  0.0,  0.0,
               st, ct, 0.0,  0.0,
               0.0, 0.0,  1.0,  0.0,
               0.0, 0.0, 0.0,  1.0);
}

void drawOnLastMarker() {
    pushMatrix();
    resetMatrix();
    
    applyMatrix(lastMatrix.m00, lastMatrix.m01, lastMatrix.m02, lastMatrix.m03,
    lastMatrix.m10, lastMatrix.m11, lastMatrix.m12, lastMatrix.m13,
    lastMatrix.m20, lastMatrix.m21, lastMatrix.m22, lastMatrix.m23,
    lastMatrix.m30, lastMatrix.m31, lastMatrix.m32, lastMatrix.m33 );

    applyZoom(1.75);
    
    drawModel();

    popMatrix();
}

void drawText() {
  fill(TEXT_COLOR);  
  text(titleString, 10, 32);
  String mode = "scale=" + heightScale;
  text(mode +  "\nv" + g_currentVersion + " of " + maxVersion + ": " + commitLog.getDate(), 
               10, height - 30);
}

void draw() {
  if (mouseNavigationEnabled) {
    incXRotation(-((lastMouseY - (float)mouseY)/MOUSE_SPEED));    
    lastMouseY = mouseY;
    incZRotation((lastMouseX - (float)mouseX)/MOUSE_SPEED);
    lastMouseX = mouseX;
  }
  
  background(255);
  drawOnLastMarker();
  drawText();
}

// interaction
void mouseMoved() {
  int x = mouseX;
  int y = mouseY;
      
  int id = picker.get(x, y);
  if (id > -1 && id < g_treemapItems.size()) {
    ClassItem item = g_treemapItems.get(id);
    titleString = "[" + item.type + "] " + item.name;
    if (!(item instanceof PackageItem))
      titleString += "\nLOC:" + item.getIntForCurrentVersion("avloc") + 
                     " methods: " + item.getIntForCurrentVersion("methods");
  }
  else {
    titleString = "";
  }
}

void mouseClicked() {
  int x = mouseX;
  int y = mouseY;
    
  int id = picker.get(x, y);
  if (id > -1 && id < g_treemapItems.size()) {
    ClassItem item = g_treemapItems.get(id);
    if (!(item instanceof PackageItem)) {
      item.toggleSelect();
      println("" + id + ": " + item.name + " level=" + item.level);
    }
  }  
}

void setZoomFactor(float factor) {
  if (factor < 0.1)
    factor = 0.1;
  else if (factor > 30.0)
    factor = 5.0;
    
  zoomFactor = factor;
  println("zoom = " + zoomFactor);
}

void incZoomFactor(float amount) {
  setZoomFactor(zoomFactor + amount);
}

void incXRotation(float amount) {
  xRotation = calculateIncreasedAngle(xRotation, amount);
}

void incYRotation(float amount) {
  yRotation = calculateIncreasedAngle(yRotation, amount);
}

void incZRotation(float amount) {
  zRotation = calculateIncreasedAngle(zRotation, amount);
}

float calculateIncreasedAngle(float actualAngle, float delta) {
  float result = actualAngle + delta;
  if (result < 0)
    result += TWO_PI;
  else if (result > TWO_PI)
    result -= TWO_PI;
    
  return result;  
}

Set<ClassItem> getSelectedItems() {
  Set<ClassItem> selected = new HashSet<ClassItem>();
  for (ClassItem item : g_treemapItems) {
    if (item.isSelected())
      selected.add(item);
  }
  return selected;
}

void keyPressed() {
  if (key == 'z') {
    incZoomFactor(0.1);
  }
  else if (key == 'Z') {
    incZoomFactor(-0.1);
  }
  else if (key == 's') {
    heightScale -= 0.1;
  }
  else if (key == 'S') {
    heightScale += 0.1;
  }
  else if (key == 'q') {
    mouseNavigationEnabled = !mouseNavigationEnabled;
    lastMouseX = mouseX;
    lastMouseY = mouseY;
  }  
  else if (key == CODED && keyCode == RIGHT) {
    setCurrentVersion(g_currentVersion + 1);
  }
  else if (key == CODED && keyCode == LEFT) {
    setCurrentVersion(g_currentVersion - 1);
  }
  else if (key == CODED && keyCode == DOWN) {
    setCurrentVersion(g_currentVersion - 10);
  }
  else if (key == CODED && keyCode == UP) {
    setCurrentVersion(g_currentVersion + 10);
  }
  else if (key == 'p') {
    save("output.png");
  }
} 
