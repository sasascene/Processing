import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.opengl.*; 
import oscP5.*; 
import netP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class OSC_local04 extends PApplet {

/*
  OSC\u306b\u3088\u308b\u901a\u4fe1\u306e\u30b5\u30f3\u30d7\u30eb
  \u30ed\u30fc\u30ab\u30eb\u4e0a\u3067\u306e\u901a\u4fe1
  \u30b7\u30fc\u30b1\u30f3\u30b5\u3068\u306e\u540c\u671f
*/




  
OscP5 oscP5;
NetAddress myRemoteLocation;

float _noiseSeed;

//  OSC\u3067\u53d6\u5f97\u3057\u305f\u5024\u3092\u683c\u7d0d\u3059\u308b\u914d\u5217
float[] oscval;
float oscv;

float rad;

// \u901a\u4fe1\u30aa\u30d6\u30b8\u30a7\u30af\u30c8(\u5f15\u6570\u306bprefix\u3092\u6307\u5b9a)
OscMessage myMessage = new OscMessage("/prefix1");

// \u521d\u671f\u8a2d\u5b9a
public void setup() {
  size(1000,600, OPENGL);
  smooth();

  _noiseSeed = random(10);
  
  // procressing\u306e\u53d7\u4fe1\u30dd\u30fc\u30c8(pd\u306e\u9001\u4fe1\u30dd\u30fc\u30c8)
  oscP5 = new OscP5(this,7702);
  
  // procressing\u306e\u9001\u4fe1\u30dd\u30fc\u30c8(pd\u306e\u53d7\u4fe1\u30dd\u30fc\u30c8)
  //myRemoteLocation = new NetAddress("127.0.0.1",7702);

  oscval = new float[8];
  for (int i = 0; i < 8; i++){
    oscval[i] = 0;
  }

  oscv = 0;

  rad = 150;

}

// \u63cf\u753b
public void draw() {

  // \u753b\u9762\u306e\u521d\u671f\u5316
  background(20);

  // \u97f3\u91cf\u3092\u7403\u306e\u534a\u5f84\u306b\u30a2\u30b5\u30a4\u30f3
  rad = oscv;

  pushMatrix();

    // Y\u65b9\u5411\u306e\u79fb\u52d5\u3000\u753b\u9762\u4e2d\u592e\u306b\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u914d\u7f6e
    translate(0, height/2, 0);

    // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
    for(int i = 0; i < 8; i++){
      drawNewSphere2(i);
    }
  popMatrix();

  pushMatrix();

    // Y\u65b9\u5411\u306e\u79fb\u52d5\u3000\u753b\u9762\u4e2d\u592e\u306b\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u914d\u7f6e
    translate(0, height/2, 0);

    // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
    for(int i = 0; i < 8; i++){
      drawNewSphere(i);
    }
  popMatrix();

}

// \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
public void drawNewSphere2(int n){

  stroke(random(20)+180, 80);

  // \u30c8\u30ea\u30ac\u30fc\u3092\u53d6\u5f97
  float radius = oscval[n] * rad;
  translate(width/9, 0, 0);

  pushMatrix();
    rotateX(frameCount * 0.03f);
    rotateY(frameCount * 0.04f);
    float s = 0;
    float t = 0;

    PVector lastPos = new PVector(0, 0, 0);
    PVector lastPos2 = new PVector(0, 0, 0);
    PVector thisPos = new PVector(0, 0, 0);

    while(t < 180){
      s += noise(_noiseSeed) * 200; // \u70b9\u306e\u9593\u9694\uff1f
      t += 2; // \u87ba\u65cb\u306e\u9593\u9694
      float radianS = radians(s);
      float radianT = radians(t);

      thisPos.x = 0 + (radius * cos(radianS) * sin(radianT));
      thisPos.y = 0 + (radius * sin(radianS) * sin(radianT));
      thisPos.z = 0 + (radius * cos(radianT));

      if(lastPos.x != 0){
        //drawLine(thisPos.x, thisPos.y, thisPos.z, lastPos.x, lastPos.y, lastPos.z);
        drawLine(thisPos.x, thisPos.y, thisPos.z, lastPos2.x, lastPos2.y, lastPos2.z);
        point(thisPos.x, thisPos.y, thisPos.z);
      }

      if(t % 5 == 0){
        lastPos2 = thisPos.get(); 
      }
      lastPos = thisPos.get();

      _noiseSeed = _noiseSeed + 0.01f;
    }
  popMatrix();

  // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u30b5\u30a4\u30ba\u3092\u7e2e\u5c0f
  float rate = 0.99f;
  oscval[n] = radius / rad * rate;
}



// \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
public void drawNewSphere(int n){

  stroke(random(30)+100);

  // \u30c8\u30ea\u30ac\u30fc\u3092\u53d6\u5f97
  float radius = oscval[n] * rad;
  translate(width/9, 0, 0);

  pushMatrix();
    rotateX(frameCount * 0.03f * random(10));
    rotateY(frameCount * 0.04f * random(10));
    float s = 0;
    float t = 0;

    PVector lastPos = new PVector(0, 0, 0);
    PVector thisPos = new PVector(0, 0, 0);

    while(t < 180){
      s += noise(random(10))*random(360);
      t += noise(random(10))*random(20);
      float radianS = radians(s);
      float radianT = radians(t);

      thisPos.x = 0 + (radius * cos(radianS) * sin(radianT));
      thisPos.y = 0 + (radius * sin(radianS) * sin(radianT)) + noise(random(100));
      thisPos.z = 0 + (radius * cos(radianT));

      if(lastPos.x != 0){
        drawLine(thisPos.x, thisPos.y, thisPos.z, lastPos.x, lastPos.y, lastPos.z);
        point(thisPos.x, thisPos.y, thisPos.z); 
      }

      lastPos = thisPos.get();
    }
  popMatrix();

  // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u30b5\u30a4\u30ba\u3092\u7e2e\u5c0f
  float rate = 0.95f;
  oscval[n] = radius / rad * rate;
}

// \u7dda\u306e\u63cf\u753b
public void drawLine(float x, float y, float z, float ex, float ey, float ez){
 for(int i = 5; i > 0; i--){
   stroke(240, 255 - i * 60); // gray alpha
   strokeWeight(i/3);
   line(x, y, z, ex, ey, ez);
 }
}

// \u30de\u30a6\u30b9\u30c9\u30e9\u30c3\u30b0\u6642\u30a4\u30d9\u30f3\u30c8
public void mouseDragged(){
  // \u901a\u4fe1\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u521d\u671f\u5316
  myMessage.clear();
  // prefix\u306e\u6307\u5b9a
  myMessage.setAddrPattern("/prefix1");
  // \u5024\u306e\u6307\u5b9a(\u4e0b\u8a18\u306e\u3088\u3046\u306b\u7d9a\u3051\u3066\u8ffd\u52a0\u3059\u308b\u3053\u3068\u3067PureData\u3067\u306flist\u3068\u3057\u3066\u6271\u3046\u3053\u3068\u304c\u3067\u304d\u308b)
  myMessage.add(mouseX);
  myMessage.add(mouseY);
  // OSC\u306e\u9001\u4fe1
  //oscP5.send(myMessage, myRemoteLocation);
}


// OSC\u53d7\u4fe1\u30a4\u30d9\u30f3\u30c8
public void oscEvent(OscMessage theOscMessage) {

  // prefix\u306e\u53d6\u5f97
  String prefix = theOscMessage.addrPattern();

  // prefix\u304c"/nseq/"\u306e\u5834\u5408\u306e\u307f\u5b9f\u884c
  if(prefix.equals("/nseq/")){
    // \u30c8\u30e9\u30c3\u30afNo\u3092\u53d6\u5f97
    int index = theOscMessage.get(0).intValue();
    // \u30c8\u30ea\u30ac\u30fc\u3092\u53d6\u5f97
    float fval = (float)theOscMessage.get(1).intValue();
    oscval[index] = fval;
  }else if(prefix.equals("/nseqv/")){ // prefix\u304c"/nseqv/"\u306e\u5834\u5408\u306e\u307f\u5b9f\u884c
    float fval = (float)theOscMessage.get(0).floatValue();
    oscv = fval * 2;
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "OSC_local04" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
