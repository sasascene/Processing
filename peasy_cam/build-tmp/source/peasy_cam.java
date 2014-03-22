import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import peasy.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class peasy_cam extends PApplet {

/*
  3D\u8996\u70b9\u30b3\u30f3\u30c8\u30ed\u30fc\u30eb\u7528\u30e9\u30a4\u30d6\u30e9\u30ea peasy \u306e\u30b5\u30f3\u30d7\u30eb
  \u30c9\u30e9\u30c3\u30b0\uff1a\u56de\u8ee2
  \u30b9\u30af\u30ed\u30fc\u30eb\uff1aZ\u8ef8\u65b9\u5411\u306e\u79fb\u52d5
*/

 // \u8996\u70b9\u30b3\u30f3\u30c8\u30ed\u30fc\u30eb\u7528\u30e9\u30a4\u30d6\u30e9\u30ea

PeasyCam cam; // \u30ab\u30e1\u30e9

//\u3000\u521d\u671f\u8a2d\u5b9a
public void setup() {

  size(500,500,P3D);

  // \u30ab\u30e1\u30e9\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u751f\u6210
  cam = new PeasyCam(this, 100);
  cam.setMinimumDistance(100);  // \u6700\u5c0f\u8ddd\u96e2\u306e\u6307\u5b9a
  cam.setMaximumDistance(500);  // \u6700\u5927\u8ddd\u96e2\u306e\u6307\u5b9a
  
  smooth();
  strokeWeight(0.5f);
}

// \u63cf\u753b
public void draw() {
  
  noFill(); // \u5857\u308a\u3064\u3076\u3057\u306a\u3057
  stroke(100,100,100);  // \u7dda\u8272
  background(255);  // \u80cc\u666f\u63cf\u753b
  sphere(30); // \u7403\u4f53\u306e\u63cf\u753b
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "peasy_cam" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
