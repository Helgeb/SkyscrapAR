//String INPUT_FILENAME = "awatility.xml";
//String INPUT_FILENAME = "prefuse.xml";
String INPUT_FILENAME = "junit.xml";
/*
TODO list
=========
- no XML, atributo lastVersion pode estar errado
- no tweening, algumas classes vem de baixo.

- busca textual
- regua mostra versoes em que classes highlighted foram modificadas
- ver se nexttext possui texto com outline, que é melhor que texto com shadow que estou usando

DONE
====
- Use churn as height
  - height = sum(churns) from v[i+1] to v[j] minus churn(v[i])
- Highlight classes that were changed in the last version
  - Also: classes changed between first and last version
- Use tweening to animate version change
- Take greater height as a reference height
- Write name of hover class on some kind of title bar
- Speak class name (split words with hyphen)
- Maybe try mrbola speech (Didn't work, only noise is produced).
*/

////////////////////////////////////////////////////////
/////////// Configuration Variables ////////////////////
////////////////////////////////////////////////////////

int THRESHOLD = 80; //45; //85; //110;
double CONFIDENCE_THRESHOLD = 0.51; // default: 0.51
boolean DEBUG = false;
boolean USE_OPENGL = false;
boolean USE_CAM = false;
int WINDOW_WIDTH = 1000; //640;
int WINDOW_HEIGHT = 750; //480;

boolean PERSISTENT_TREEMAP = false;

boolean DRAW_MODELS = true;
boolean FLIPPED_CAM = false;

int TREEMAP_WIDTH = 100;
int TREEMAP_HEIGHT = TREEMAP_WIDTH;
boolean HIDE_NON_SELECTED = false;

color PACKAGE_MIN_COLOR = #000000;
color PACKAGE_MAX_COLOR = #FFFFFF;
color CLASS_FLOOR_COLOR = #009900;
color CLASS_CHANGED_COLOR = #990000;
color CLASS_DEFAULT_COLOR = #CCCCCC;
color CLASS_HIGHLIGHT_COLOR = #FFFF99;
color FLOOR_COLOR = #000000;
color TEXT_BG_COLOR = #000000;
color TEXT_FG_COLOR = #FFFFFF;

double PACKAGE_HEIGHT = 2.0;

double PACKAGE_BASE_RATIO = 0.90;
double CLASS_BASE_RATIO = 0.70;

double DEFAULT_CLASS_MIN_HEIGHT = 10.0;
double CLASS_MIN_HEIGHT = DEFAULT_CLASS_MIN_HEIGHT;
double CLASS_MAX_HEIGHT = (TREEMAP_WIDTH + TREEMAP_HEIGHT) * 0.6;

boolean HIGHLIGHT_CHANGES_IS_CUMULATIVE = false;

double TWEENING_TIME_INTERVAL = 1000; // milliseconds

float MOUSE_SPEED = 50;

float zoomFactor = 1.2;
float xRotation = 0.0;
float yRotation = 0.0;
float zRotation = 0.0;

boolean mouseNavigationEnabled = false;
int lastMouseX;
int lastMouseY;

boolean useLocForBoxHeight = false;
float heightScale = 1.0;

////////////////////////////////////////////////////////
///////////////////// Imports //////////////////////////
////////////////////////////////////////////////////////

import guru.ttslib.*;
import processing.video.*;
import jp.nyatla.nyar4psg.*;
import treemap.*;
import picking.*;

//if (USE_OPENGL) {
  import processing.opengl.*;
//}

////////////////////////////////////////////////////////
/////////// Global Variables ///////////////////////////
////////////////////////////////////////////////////////

PMatrix3D lastMatrix = new PMatrix3D(0.03271547,-0.9987524,0.037727464,7.3349524,0.9948697,0.028926386,-0.09694087,6.203373,0.0957286,0.040705375,0.99457484,-279.99384,0.0,0.0,0.0,1.0);

// NyAR4Psg
Capture cam;
MultiMarker nya;
PFont font=createFont("FFScala", 16);
NyAR4PsgConfig nyarConf = NyAR4PsgConfig.CONFIG_PSG;
//NyAR4PsgConfig nyarConf = new NyAR4PsgConfig(NyAR4PsgConfig.CS_RIGHT_HAND, NyAR4PsgConfig.TM_NYARTK);
PImage myframe;

// Treemap
Treemap map;
PackageItem mapModel;
int globalIndex = 0;
LinkedList<ClassItem> g_treemapItems = new LinkedList<ClassItem>();
int g_nSelectedItems = 0;
int g_currentVersion = 1;
int g_firstVersion = 1;
double g_tweeningVersion = g_currentVersion;
double g_maxChurn = 0;
int maxVersion = -1;
CommitLog commitLog;
String projectName;
int previousVisitedVersion = g_firstVersion;

// misc
TTS tts;
Announcer announcer = null;
String titleString = "";
Picker picker;


////////////////////////////////////////////////////////
////////////////// Functions ///////////////////////////
////////////////////////////////////////////////////////

void loadTreemap() {
  // different choices for the layout method
  //MapLayout algorithm = new SliceLayout(); // linhas finas
  //MapLayout algorithm = new StripTreemap(); // linhas finas subdivididas
  MapLayout algorithm = new PivotBySplitSize(); // default
  //MapLayout algorithm = new SquarifiedLayout();

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
  size(WINDOW_WIDTH, WINDOW_HEIGHT, USE_OPENGL ? OPENGL : P3D);
  println(MultiMarker.VERSION);
  if (USE_CAM)
    cam=new Capture(this, WINDOW_WIDTH, WINDOW_HEIGHT);
  nya=new MultiMarker(this,width,height,"camera_para.dat",nyarConf);
  nya.addARMarker("patt.sample1",80);
  nya.addARMarker("patt.sample2",80);
  nya.setThreshold(THRESHOLD);
  nya.setConfidenceThreshold(CONFIDENCE_THRESHOLD);
  myframe = new PImage(width, height, RGB);

  println("default confidence: " + MultiMarker.DEFAULT_CF_THRESHOLD);

  loadTreemap();
  picker = new Picker(this);
  
  tts = new TTS();
  
  textFont(font);
  textMode(SCREEN);
}

// Rodrigo, 2011-06-06
// should be the last method call on draw()
// based on http://forum.processing.org/topic/nyartoolkit-when-i-flip-videocapture-mirror-effect-the-overlayed-objects-don%C2%B4t-flip
void flipScreen() {
  loadPixels();
  for (int x=0; x<width; x++) {
      for (int y=0; y<height; y++) {
        int loc = (width- x- 1) + y*width;
        myframe.pixels[x+y*width] = color(red(pixels[loc]), green(pixels[loc]), blue(pixels[loc]));
      }
    }
  myframe.updatePixels();
      hint (DISABLE_DEPTH_TEST);
      image(myframe, 0, 0);
    hint (ENABLE_DEPTH_TEST);
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
  //translate(0,0,40);
  //box(80, 80, 80);
  
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
//  drawModelCube();
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
    lastMatrix.m30, lastMatrix.m31, lastMatrix.m32, lastMatrix.m33
    );

    applyZoom(1.75);
    
    if (DRAW_MODELS)
      drawModel();

    popMatrix();
}

void shadowedText(String str, float x, float y, color fg, color bg) {
  fill(bg);
  text(str, x+2, y+2);
  fill(fg);
  text(str, x, y);
}

void shadowedText(String str, float x, float y) {
  shadowedText(str, x, y, TEXT_FG_COLOR, TEXT_BG_COLOR);
}

void drawText() {
  shadowedText(titleString, 10, 32);
  String mode = " (scale=" + heightScale + " LOC-mode=" + useLocForBoxHeight + ")";
  shadowedText(commitLog.getAuthor() + mode +  "\nv" + g_currentVersion + " of " + maxVersion + ": " +
    commitLog.getMessage(), 10, height - 30);
}

void draw()
{
  tweenVersion();
  if (mouseNavigationEnabled) {
    incXRotation(-((lastMouseY - (float)mouseY)/MOUSE_SPEED));    
    lastMouseY = mouseY;
    incZRotation((lastMouseX - (float)mouseX)/MOUSE_SPEED);
    lastMouseX = mouseX;
  }
  
  if (!USE_CAM) {
    background(255);
    drawOnLastMarker();
    drawText();
    return;
  }
  else if (cam.available() !=true) {
    return;
  }  
  cam.read();
  nya.detect(cam);
  background(0);
  
  if (DEBUG) {
    loadPixels();
    for (int i = 0; i < width*height; i++) {
      color c = cam.pixels[i];
      if (brightness(c) > THRESHOLD)
        pixels[i] = #FFFFFF;
      else
        pixels[i] = #000000;
    }
    updatePixels();
  }
  else {
    nya.drawBackground(cam);
  }
  
  if((nya.isExistMarker(0))){
    lastMatrix = nya.getMarkerMatrix(0);
    nya.beginTransform(0);
    if (DRAW_MODELS)
      drawModel();
    else if (DEBUG) {
      int len = 80;
      strokeWeight(4);
      stroke(#CCCC00);
      noFill();
      rect(-len/2, -len/2, len, len);
      strokeWeight(8);
      stroke(#FF0000);
      line(0, 0, 0, len, 0, 0);
      stroke(#00FF00);
      line(0, 0, 0, 0, len, 0);
      stroke(#0000FF);
      line(0, 0, 0, 0, 0, len);
    }
    nya.endTransform();
  }
  else if (PERSISTENT_TREEMAP) {
    drawOnLastMarker();
  }
  
  if((nya.isExistMarker(1))){
    nya.beginTransform(1);
    if (DRAW_MODELS)
      drawModelCube();
    nya.endTransform();
  }

  
  if (FLIPPED_CAM)
    flipScreen();

  drawText();    
}

// interaction
void mouseMoved() {
  int x = mouseX;
  int y = mouseY;
  
  if (FLIPPED_CAM)
    x = width - x;
    
  int id = picker.get(x, y);
  if (id > -1 && id < g_treemapItems.size()) {
    ClassItem item = g_treemapItems.get(id);
    titleString = "[" + item.type + "] " + item.fullName;
    if (!(item instanceof PackageItem)) // Δ
      titleString += "\nLOC:" + item.getIntForCurrentVersion("curr_loc") + " churn: " + (item.getIntForCurrentVersion("churn") - item.firstChurn);
  }
  else {
    titleString = "";
  }
}

void mouseClicked() {
  int x = mouseX;
  int y = mouseY;
  if (FLIPPED_CAM)
    x = width - x;
    
  int id = picker.get(x, y);
  if (id > -1 && id < g_treemapItems.size()) {
    ClassItem item = g_treemapItems.get(id);
    if (!(item instanceof PackageItem)) {
      item.toggleSelect();
      println("" + id + ": " + item.name + " level=" + item.level);
      if (item.isSelected())
        speak(item.name);
    }
  }  
}

void speak(String name) {
  String hyphenatedName = "";
  int i = 0;
  for (char c : name.toCharArray()) {
    if (i > 0 && c >= 'A' && c <= 'Z')
      hyphenatedName += "-";
    hyphenatedName += c;
    i++;
  }
  
  println("Speak " + hyphenatedName);
  announcer = new Announcer(hyphenatedName);
  announcer.start();
}

void updateThreshold(int newThreshold) {
  if (newThreshold > 255)
    THRESHOLD = 255;
  else if (newThreshold < 0)
    THRESHOLD = 0;
  else   
    THRESHOLD = newThreshold;
  nya.setThreshold(THRESHOLD);
  
  println("New THRESHOLD = " + THRESHOLD);
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

void nextVersionThatChangedAnySelectedItem(int step) {
  int v = g_currentVersion;
  Set<ClassItem> selected = getSelectedItems();
  
  if (selected.size() == 0)
    return;
  
  boolean found = false;
  do {
    v += step;
    if (v > maxVersion)
      v = maxVersion;
    if (v < 1)
      v = 1;
      
    for (ClassItem item : selected) {
      if (item.hasChangedInVersion(v)) {
        found = true;
        break;
      }
    }
  } while(!found && v < maxVersion && v > 1);
  
  if (found)
    setCurrentVersion(v);
}

void keyPressed() {
  if (key == 'd')
    DEBUG = !DEBUG;
  else if (key == 'm')
    DRAW_MODELS = !DRAW_MODELS;
  else if (key == 'h') {
    HIDE_NON_SELECTED = !HIDE_NON_SELECTED;
  }
  else if (key == '+' || key == '=') {
    updateThreshold(THRESHOLD + 5);
  }
  else if (key == '-') {
    updateThreshold(THRESHOLD - 5);
  }  
  else if (key == 'z') {
    incZoomFactor(0.1);
  }
  else if (key == 'Z') {
    incZoomFactor(-0.1);
  }
  else if (key == 'e') {
    setCurrentVersion(maxVersion);
  }
  else if (key == 8) {
    setCurrentVersion(previousVisitedVersion);
    println ("8!!");
  }
  else if (key >='0' && key <='9') {
    // 1 = first, 0 = last version)
    // 1=0%
    // 2=1/9
    // 9=8/9
    // 0=9/9
    if (key=='0'){
      setCurrentVersion(maxVersion);
    }
    else {
      int parsedKey = Character.getNumericValue(key);
      float position = (((float)parsedKey)-1f)/9f;
      setCurrentVersion((int)(maxVersion*position));
    }
  }
  else if (key == 'l') {
    useLocForBoxHeight = !useLocForBoxHeight;
    if (useLocForBoxHeight) {
      CLASS_MIN_HEIGHT = 1;
    }
    else {
      CLASS_MIN_HEIGHT = DEFAULT_CLASS_MIN_HEIGHT;
    }
  }
  else if (key == 's') {
    heightScale -= 0.1;
  }
  else if (key == 'S') {
    heightScale += 0.1;
  }  
  else if (key == 'p') {
    PERSISTENT_TREEMAP = !PERSISTENT_TREEMAP;
  }
  else if (key == 'n') {
    for (ClassItem item : getSelectedItems())
      item.toggleSelect();
  }
  else if (key == 'q') {
    mouseNavigationEnabled = !mouseNavigationEnabled;
    lastMouseX = mouseX;
    lastMouseY = mouseY;
  }  

//  else if (key == 'c') {
//    HIGHLIGHT_CHANGES_IS_CUMULATIVE = !HIGHLIGHT_CHANGES_IS_CUMULATIVE;
//  }

  if (g_nSelectedItems == 0) {
    if (key == CODED && keyCode == RIGHT) {
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
    else if (key == 'x') {
      incXRotation(0.1);
    }    
    else if (key == 'X') {
      incXRotation(-0.1);
    }    
    else if (key == 'y') {
      incYRotation(0.1);
    }    
    else if (key == 'Y') {
      incYRotation(-0.1);
    }    
    else if (key == 'a') {
      incZRotation(0.1);
    }    
    else if (key == 'A') {
      incZRotation(-0.1);
    }    

  }
  else {
    if (key == CODED && keyCode == RIGHT) {
      nextVersionThatChangedAnySelectedItem(1);
    }
    else if (key == CODED && keyCode == LEFT) {
      nextVersionThatChangedAnySelectedItem(-1);
    }
    
  }

} 
