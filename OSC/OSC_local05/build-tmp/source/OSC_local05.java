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

public class OSC_local05 extends PApplet {

/*
  OSC\u306b\u3088\u308b\u901a\u4fe1\u306e\u30b5\u30f3\u30d7\u30eb
  \u30ed\u30fc\u30ab\u30eb\u4e0a\u3067\u306e\u901a\u4fe1
  \u30b7\u30fc\u30b1\u30f3\u30b5\u3068\u306e\u540c\u671f
*/




  
OscP5 oscP5;
NetAddress myRemoteLocation;

// \u30ce\u30a4\u30ba\u306e\u7a2e
float _noiseSeed;

float[] oscval; //  OSC\u3067\u53d6\u5f97\u3057\u305f\u5024\u3092\u683c\u7d0d\u3059\u308b\u914d\u5217
float oscv; // \u97f3\u91cf
float oscp; // \u30d1\u30bf\u30fc\u30f3

float rad; // \u534a\u5f84

PVector[] pvecs;

// \u7403\u4e0a\u306e\u70b9\u30ea\u30b9\u30c8
ArrayList<PVector> dotList;


// \u901a\u4fe1\u30aa\u30d6\u30b8\u30a7\u30af\u30c8(\u5f15\u6570\u306bprefix\u3092\u6307\u5b9a)
OscMessage myMessage = new OscMessage("/prefix1");

// \u521d\u671f\u8a2d\u5b9a
public void setup() {
  size(1000,600, OPENGL);
  smooth();

  // \u30ce\u30a4\u30ba\u306e\u7a2e\u3092\u30e9\u30f3\u30c0\u30e0\u306b\u8a2d\u5b9a
  _noiseSeed = random(10);
  
  // procressing\u306e\u53d7\u4fe1\u30dd\u30fc\u30c8(pd\u306e\u9001\u4fe1\u30dd\u30fc\u30c8)
  oscP5 = new OscP5(this,7702);
  
  // procressing\u306e\u9001\u4fe1\u30dd\u30fc\u30c8(pd\u306e\u53d7\u4fe1\u30dd\u30fc\u30c8)
  //myRemoteLocation = new NetAddress("127.0.0.1",7702);

  // \u5909\u6570\u306e\u521d\u671f\u5316
  oscval = new float[8];
  for (int i = 0; i < 8; i++){
    oscval[i] = 0;
  }

  oscv = 0; // \u97f3\u91cf
  oscp = 0; // \u30d1\u30bf\u30fc\u30f3

  rad = 150;  // \u534a\u5f84

  pvecs = new PVector[500];
  for(int i = 0; i < 500; i++){
    pvecs[i] = new PVector(0, 0, 0);
  }

  // \u7403\u4e0a\u306e\u70b9\u30ea\u30b9\u30c8
  dotList = new ArrayList<PVector>();

}

// \u63cf\u753b
public void draw() {

  // \u97f3\u91cf\u3092\u7403\u306e\u534a\u5f84\u306b\u30a2\u30b5\u30a4\u30f3
  rad = oscv;

  if(oscp == 0){

    // \u753b\u9762\u306e\u521d\u671f\u5316
    background(20);

    pushMatrix();

      // Y\u65b9\u5411\u306e\u79fb\u52d5\u3000\u753b\u9762\u4e2d\u592e\u306b\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u914d\u7f6e
      translate(0, height/2, 0);

      // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
      for(int i = 0; i < 8; i++){
        drawSphereBig(i);
      }

    popMatrix();

  }else if(oscp == 1){
    // \u753b\u9762\u306e\u521d\u671f\u5316
    fill(20, 20, 20, 70);
    rect(-10, -10, width+20, height+20);

    pushMatrix();

      // Y\u65b9\u5411\u306e\u79fb\u52d5\u3000\u753b\u9762\u4e2d\u592e\u306b\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u914d\u7f6e
      translate(0, height/2, 0);

      // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
      for(int i = 0; i < 8; i++){
        drawSphere_noise(i);
      }
    popMatrix();
  }else if(oscp == 2){

    // \u753b\u9762\u306e\u521d\u671f\u5316
    background(20);

    pushMatrix();

      // Y\u65b9\u5411\u306e\u79fb\u52d5\u3000\u753b\u9762\u4e2d\u592e\u306b\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u914d\u7f6e
      translate(0, height/2, 0);

      // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
      for(int i = 0; i < 8; i++){
        drawSphereBasic(i);
      }
    popMatrix();
  }else if(oscp == 3){

    // \u753b\u9762\u306e\u521d\u671f\u5316
    //fill(20, 20, 20, 70);
    //rect(-10, -10, width+20, height+20);
    background(20);

    pushMatrix();

      // Y\u65b9\u5411\u306e\u79fb\u52d5\u3000\u753b\u9762\u4e2d\u592e\u306b\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u914d\u7f6e
      translate(0, height/2, 0);

      // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
      for(int i = 0; i < 8; i++){
        drawSphere_E(i);
      }
    popMatrix();
  }else if(oscp == 4){
    // \u753b\u9762\u306e\u521d\u671f\u5316
    background(20);

    pushMatrix();

      // Y\u65b9\u5411\u306e\u79fb\u52d5\u3000\u753b\u9762\u4e2d\u592e\u306b\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u914d\u7f6e
      translate(0, height/2, 0);

      // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
      for(int i = 0; i < 8; i++){
        drawSphereBasic(i);
      }

    popMatrix();

    pushMatrix();
      // Y\u65b9\u5411\u306e\u79fb\u52d5\u3000\u753b\u9762\u4e2d\u592e\u306b\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u914d\u7f6e
      translate(0, height/2, 0);
      // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
      for(int i = 5; i < 8; i++){
        drawSphere2(i);
      }
    popMatrix();
  }
}

// \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
public void drawSphereBig(int n){

  stroke(random(30)+220);

  // \u30c8\u30ea\u30ac\u30fc\u3092\u53d6\u5f97
  //float radius = 50;
  // \u30c8\u30ea\u30ac\u30fc\u3092\u53d6\u5f97
  float radius = oscval[n] * rad;

  // \u30ea\u30b9\u30c8\u306e\u521d\u671f\u5316
  dotList.clear();

  translate(width/9, 0, 0);

  pushMatrix();
    rotateX(frameCount * 0.01f);
    rotateY(frameCount * 0.01f);
    float s = 0;
    float t = 0;

    PVector lastPos = new PVector(0, 0, 0);
    PVector thisPos = new PVector(0, 0, 0);

    PVector min_Pos = new PVector(0, 0, 0);

    while(t < 180){
      s += 18;
      t += 1;
      float radianS = radians(s);
      float radianT = radians(t);

      thisPos.x = 0 + (radius * cos(radianS) * sin(radianT));
      thisPos.y = 0 + (radius * sin(radianS) * sin(radianT));
      thisPos.z = 0 + (radius * cos(radianT));

      float radius2 = radius + (100 * noise(_noiseSeed)) - 50;
      float radius3 = radius + (80 * noise(_noiseSeed)) - 40;
      float radius4 = radius + (30 * noise(_noiseSeed)) - 15;
      thisPos.y = 0 + (radius2 * sin(radianS) * sin(radianT));
      thisPos.x = 0 + (radius3 * cos(radianS) * sin(radianT));
      thisPos.z = 0 + (radius4 * cos(radianT));

      if(lastPos.x != 0){
        //drawLine(thisPos.x, thisPos.y, thisPos.z, lastPos.x, lastPos.y, lastPos.z);
        if(t % 3 == 0){
          strokeWeight(3);
          point(thisPos.x, thisPos.y, thisPos.z);

          dotList.add(thisPos.get());
        }
      }

      _noiseSeed = _noiseSeed + 0.1f;
      lastPos = thisPos.get();
    }

    // \u6700\u77ed\u70b9\u306e\u63a2\u7d22
    for(int i = 1; i < dotList.size(); i++){
      PVector p1 = dotList.get(i);
      for(int j = 1; j < dotList.size(); j++){
        PVector p2 = dotList.get(j);
        float d1 = abs(dist(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z));
        float d2 = abs(dist(p1.x, p1.y, p1.z, min_Pos.x, min_Pos.y, min_Pos.z));
        if(d1 <= d2 && d1 != 0){
          min_Pos = p2.get();
        }
      }
      strokeWeight(0.5f);
      line(p1.x, p1.y, p1.z, min_Pos.x, min_Pos.y, min_Pos.z);
    }

  popMatrix();

  // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u30b5\u30a4\u30ba\u3092\u7e2e\u5c0f
  //float rate = 0.95;
  //oscval[n] = radius / 2 / rad * rate;
  oscval[n] = oscval[n] * 0.9f ;
}


// \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
public void drawSphere2(int n){

  stroke(random(30)+220);

  // \u30c8\u30ea\u30ac\u30fc\u3092\u53d6\u5f97
  float radius = oscval[n] * rad * 2;

  translate(width/4, 0, 0);

  pushMatrix();
    rotateX(frameCount * 0.03f * random(10));
    rotateY(frameCount * 0.04f * random(10));
    float s = 0;
    float t = 0;

    PVector lastPos = new PVector(0.1f, 0, 0);
    PVector thisPos = new PVector(0, 0, 0);

    while(t < 180){
      //s += 18;
      //t += 0.3;
      s += noise(_noiseSeed) + 18;
      t += 0.9f;
      float radianS = radians(s);
      float radianT = radians(t);

      thisPos.x = 0 + (radius * cos(radianS) * sin(radianT));
      thisPos.y = 0 + (radius * sin(radianS) * sin(radianT));
      thisPos.z = 0 + (radius * cos(radianT));

      if(lastPos.x != 0){
        strokeWeight(5);
        //line(thisPos.x, thisPos.y, thisPos.z, lastPos.x, lastPos.y, lastPos.z);
        if(random(1) > 0.3f){
          line(thisPos.x, thisPos.y, thisPos.z, lastPos.x, lastPos.y, lastPos.z);
        }
        point(thisPos.x, thisPos.y, thisPos.z); 
      }

      _noiseSeed = _noiseSeed + 0.01f;
      lastPos = thisPos.get();
    }
  popMatrix();

  // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u30b5\u30a4\u30ba\u3092\u7e2e\u5c0f
  float rate = 0.95f;
  oscval[n] = radius / 2 / rad * rate;
}

// \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
public void drawSphere_E(int n){

  stroke(random(30)+220, 200);

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
      s += noise(_noiseSeed);
      t += noise(_noiseSeed);
      float radianS = radians(s);
      float radianT = radians(t);

      thisPos.x = 0 + (radius * cos(radianS) * sin(radianT));
      thisPos.y = 0 + (radius * sin(radianS) * sin(radianT)) + noise(_noiseSeed);
      thisPos.z = 0 + (radius * cos(radianT));

      if(lastPos.x != 0){
        strokeWeight(3);
        line(thisPos.x, thisPos.y, thisPos.z, lastPos.x, lastPos.y, lastPos.z);
        point(thisPos.x, thisPos.y, thisPos.z); 
      }

      _noiseSeed = _noiseSeed + 0.01f;
      lastPos = thisPos.get();
    }
  popMatrix();

  // \u753b\u9762\u306e\u30d5\u30e9\u30c3\u30b7\u30e5
  if(n == 1 && oscval[7] == 1){
    flash();
  }

  // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u30b5\u30a4\u30ba\u3092\u7e2e\u5c0f
  float rate = 0.95f;
  oscval[n] = radius / rad * rate;
}

// \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
public void drawSphere_noise(int n){

  stroke(random(20)+180, 80);

  // \u30c8\u30ea\u30ac\u30fc\u3092\u53d6\u5f97
  float radius = oscval[n] * rad;
  translate(width/9, 0, 0);

  pushMatrix();
    rotateX(frameCount * 0.03f * random(10) * 0.1f);
    rotateY(frameCount * 0.04f * random(10) * 0.1f);
    float s = 0;
    float t = 0;

    PVector lastPos = new PVector(0, 0, 0);
    PVector thisPos = new PVector(0, 0, 0);

    while(t < 180){
      s += 30;
      t += 0.4f;
      float radianS = radians(s);
      float radianT = radians(t);

      thisPos.x = 0 + (radius * cos(radianS) * sin(radianT)) + noise(_noiseSeed) * 100 - 50;
      thisPos.y = 0 + (radius * sin(radianS) * sin(radianT)) + noise(_noiseSeed) * 400 - 200;
      thisPos.z = 0 + (radius * cos(radianT)) + noise(_noiseSeed) * 80 - 40;

      if(lastPos.x != 0){
        drawLine(thisPos.x, thisPos.y, thisPos.z, lastPos.x, lastPos.y, lastPos.z);
      }

      _noiseSeed = _noiseSeed + 0.01f;

      lastPos = thisPos.get();
    }
  popMatrix();

  // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u30b5\u30a4\u30ba\u3092\u7e2e\u5c0f
  float rate = 0.99f;
  oscval[n] = radius / rad * rate;
}



// \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
public void drawSphereBasic(int n){

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
      thisPos.y = 0 + (radius * sin(radianS) * sin(radianT));
      thisPos.z = 0 + (radius * cos(radianT));

      if(lastPos.x != 0){
        drawLine(thisPos.x, thisPos.y, thisPos.z, lastPos.x, lastPos.y, lastPos.z);
        point(thisPos.x, thisPos.y, thisPos.z); 
      }

      lastPos = thisPos.get();
    }
  popMatrix();

  // \u753b\u9762\u306e\u30d5\u30e9\u30c3\u30b7\u30e5
  if(n == 1 && oscval[7] == 1){
    flash();
  }

  // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u30b5\u30a4\u30ba\u3092\u7e2e\u5c0f
  float rate = 0.95f;
  oscval[n] = radius / rad * rate;
}

// \u7dda\u306e\u63cf\u753b
public void drawLine(float x, float y, float z, float ex, float ey, float ez){
 for(int i = 5; i > 0; i--){
   stroke(240, 255 - i * 70); // gray alpha
   strokeWeight(i/3);
   line(x, y, z, ex, ey, ez);
 }
}

// \u753b\u9762\u306e\u30d5\u30e9\u30c3\u30b7\u30e5
public void flash(int n){

  if(oscval[n] == 1){
    loadPixels();
    //  \u30d1\u30fc\u30c6\u30a3\u30af\u30eb\u306e\u5f71\u97ff\u7bc4\u56f2\u306e\u30d4\u30af\u30bb\u30eb\u306b\u3064\u3044\u3066\u3001\u8272\u306e\u52a0\u7b97\u3092\u884c\u3046
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int pixelIndex = x + y * width;
 
        //  \u30d4\u30af\u30bb\u30eb\u304b\u3089\u3001\u8d64\u30fb\u7dd1\u30fb\u9752\u306e\u8981\u7d20\u3092\u53d6\u308a\u3060\u3059
        int r = pixels[pixelIndex] >> 16 & 0xFF;
        int g = pixels[pixelIndex] >> 8 & 0xFF;
        int b = pixels[pixelIndex] & 0xFF;
 
        r += 255;
        g += 255;
        b += 255;
 
        //  \u30d4\u30af\u30bb\u30eb\u306e\u8272\u3092\u5909\u66f4\u3059\u308b
        pixels[pixelIndex] = color(r, g, b);
      }
    }
    updatePixels();
  }

}

public void flash(){

    loadPixels();
    //  \u30d1\u30fc\u30c6\u30a3\u30af\u30eb\u306e\u5f71\u97ff\u7bc4\u56f2\u306e\u30d4\u30af\u30bb\u30eb\u306b\u3064\u3044\u3066\u3001\u8272\u306e\u52a0\u7b97\u3092\u884c\u3046
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int pixelIndex = x + y * width;
 
        //  \u30d4\u30af\u30bb\u30eb\u304b\u3089\u3001\u8d64\u30fb\u7dd1\u30fb\u9752\u306e\u8981\u7d20\u3092\u53d6\u308a\u3060\u3059
        int r = pixels[pixelIndex] >> 16 & 0xFF;
        int g = pixels[pixelIndex] >> 8 & 0xFF;
        int b = pixels[pixelIndex] & 0xFF;
 
        r += 255;
        g += 255;
        b += 255;
 
        //  \u30d4\u30af\u30bb\u30eb\u306e\u8272\u3092\u5909\u66f4\u3059\u308b
        pixels[pixelIndex] = color(r, g, b);
      }
    }
    updatePixels();
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

// \u30de\u30a6\u30b9\u62bc\u4e0b\u6642\u30a4\u30d9\u30f3\u30c8
public void mousePressed(){
  flash();
}

// \u30ad\u30fc\u62bc\u4e0b\u6642\u30a4\u30d9\u30f3\u30c8
public void keyPressed(){

  if(key == 'a'){
    oscp = 0;
  }else if(key == 's'){
    oscp = 1;
  }else if(key == 'd'){
    oscp = 2;
  }else if(key == 'f'){
    oscp = 3;
  }else if(key == 'g'){
    oscp = 4;
  }

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
  }else if(prefix.equals("/nseqp/")){ // prefix\u304c"/nseqp/"\u306e\u5834\u5408\u306e\u307f\u5b9f\u884c
    float fval = (float)theOscMessage.get(0).intValue();
    oscp = fval;
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "OSC_local05" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
