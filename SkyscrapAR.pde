import processing.video.*;
import treemap.*;
import picking.*;
import processing.opengl.*;

String INPUT_FILENAME = "data.xml";

String[] excludedElements = {"/EAS/BI_BILLING","/EAS/BW_BUSINESS_WAREHOUSE",
                             "/EAS/CC_CUSTOMER_CARE", "/EAS/EASY_MG", "/EAS/EASY_PLUS_COMMON",
                             "/EAS/KK_ACCOUNTING", "/EAS/ME_METERING", "/EAS/MK_DACHBODEN",
                             "/EAS/OM_OUTPUTMANGEMENT", "/EAS/QM_QUALITAETSMANAGEMENT",
                             "/EAS/SM_SMART_METERING",
                             "/EAS/ST_STATISTIK", "/EAS/SY_ARCHITEKTUR", "/EAS/XI" };
                             
boolean SAVE_VIDEO = false;
int WINDOW_WIDTH = 640; //1600;
int WINDOW_HEIGHT = 480; //1000;

int TREEMAP_WIDTH = 100;
int TREEMAP_HEIGHT = TREEMAP_WIDTH;

double PACKAGE_HEIGHT = 1.0;

double PACKAGE_BASE_RATIO = 0.90;
double CLASS_BASE_RATIO = 0.70;

double DEFAULT_CLASS_MIN_HEIGHT = 10.0;
double CLASS_MIN_HEIGHT = DEFAULT_CLASS_MIN_HEIGHT;
double CLASS_MAX_HEIGHT = (TREEMAP_WIDTH + TREEMAP_HEIGHT) * 0.6;

float MOUSE_SPEED = 50;

double TWEENING_TIME_INTERVAL = 1000; // milliseconds

float zoomFactor = 1.2;
float xRotation = 0.0;
float yRotation = 0.0;
float zRotation = 0.0;

CityColorHandler colorHandler = new CityColorHandler();

boolean mouseNavigationEnabled = false;
int lastMouseX;
int lastMouseY;

float heightScale = 1.0;

PMatrix3D lastMatrix = new PMatrix3D(0.03271547,-0.9987524,0.037727464,
                                     7.3349524,0.9948697,0.028926386,
                                     -0.09694087,6.203373,0.0957286,
                                     0.040705375,0.99457484,-279.99384,
                                     0.0,0.0,0.0,1.0);

PFont font=createFont("FFScala", 16);
PImage myframe;

Treemap map;
PackageItem cityParent;
int globalIndex = 0;
LinkedList<CityItem> g_treemapItems = new LinkedList<CityItem>();
int g_nSelectedItems = 0;
int g_currentVersion = 1;
int g_firstVersion = 1;
double g_tweeningVersion = g_currentVersion;
double g_maxLoc = 0;
int maxVersion = -1;
CommitLog commitLog;
String projectName;
int previousVisitedVersion = g_firstVersion;
int g_total_loc;
int g_total_objects;
int g_total_packages;
int g_total_subroutines;

String titleString = "";
Picker picker;

void loadTreemap() {
  MapLayout algorithm = new PivotBySplitSize();

  XMLElement elem = new XMLElement(this, INPUT_FILENAME);
  XMLElement elemCode = elem.getChild("CodeInfo");
  XMLElement elemLog = elem.getChild("LogInfo");
  projectName = elem.getString("name");
  
  cityParent = (PackageItem)(new XMLConverterFactory()).getPackageItemConverter().convertItem(null, elemCode, 0);
  maxVersion = elem.getInt("lastVersion");
  commitLog = new CommitLog(elemLog);

  map = new Treemap(cityParent, 0, 0, width, height);
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
  colorHandler.fillFloor();
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

//////////////// tweening //////////////////////////

int startTime = 0;
double startTweeningVersion = g_tweeningVersion;

void tweenVersion() {
  int time = millis();
  double progress = (time - startTime) / TWEENING_TIME_INTERVAL;
  if (progress > 1.0)
    progress = 1.0;
    
  g_tweeningVersion = progress*(g_currentVersion) + (1 - progress)*(startTweeningVersion);
}

void setCurrentVersion(int v) {
  if (v < 1)
    v = 1;
  else if (v > maxVersion)
    v = maxVersion;
    
  if (g_currentVersion != v) {
    previousVisitedVersion = g_currentVersion;
    
    g_currentVersion = v;
    startTime = millis();
    startTweeningVersion = g_tweeningVersion;
  }
}

////////////////////////////////////////////////////

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
  colorHandler.fillText();  
  text(titleString, 10, 32);
  String mode = "scale=" + heightScale;
  text(mode + 
       "\n" + getVersionText() +
       "\n" + getInfoText(), 10, height - 50);
}

String getInfoText() {
  return "Packages: " + NumberFormat.getInstance().format( g_total_packages ) + 
         " Objects: " + NumberFormat.getInstance().format( g_total_objects ) + 
         " Subroutines: " + NumberFormat.getInstance().format( g_total_subroutines ) + 
         " Total LOC: " + NumberFormat.getInstance().format( g_total_loc );
}

String getVersionText() {
  return "v" + g_currentVersion + " of " + maxVersion + ": " + commitLog.getDate(g_currentVersion);
}

void draw() {
  tweenVersion();
  if (mouseNavigationEnabled) {
    incXRotation(-((lastMouseY - (float)mouseY)/MOUSE_SPEED));    
    lastMouseY = mouseY;
    incZRotation((lastMouseX - (float)mouseX)/MOUSE_SPEED);
    lastMouseX = mouseX;
  }
  
    background(255);
    drawOnLastMarker();
  drawText();
  if (SAVE_VIDEO) 
	saveFrame("/output/seq-####.tga"); 
}

// interaction
void mouseMoved() {
  int x = mouseX;
  int y = mouseY;
      
  int id = picker.get(x, y);
  if (id > -1 && id < g_treemapItems.size()) {
    CityItem item = g_treemapItems.get(id);
    titleString = item.printTitleString();
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
    CityItem item = g_treemapItems.get(id);
    item.toggleSelect(id);
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

Set<CityItem> getSelectedItems() {
  Set<CityItem> selected = new HashSet<CityItem>();
  for (CityItem item : g_treemapItems) {
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
  else if (key == 'r') {
    SAVE_VIDEO = !SAVE_VIDEO;
	if (SAVE_VIDEO)
		println("Recording aktive");
  }
} 
