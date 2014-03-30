import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
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

public class OSC_local03 extends PApplet {

/*
  OSC\u306b\u3088\u308b\u901a\u4fe1\u306e\u30b5\u30f3\u30d7\u30eb
  \u30ed\u30fc\u30ab\u30eb\u4e0a\u3067\u306e\u901a\u4fe1
  \u30b7\u30fc\u30b1\u30f3\u30b5\u3068\u306e\u540c\u671f
*/



  
OscP5 oscP5;
NetAddress myRemoteLocation;

//  OSC\u3067\u53d6\u5f97\u3057\u305f\u5024\u3092\u683c\u7d0d\u3059\u308b\u914d\u5217
float[] oscval;

// \u901a\u4fe1\u30aa\u30d6\u30b8\u30a7\u30af\u30c8(\u5f15\u6570\u306bprefix\u3092\u6307\u5b9a)
OscMessage myMessage = new OscMessage("/prefix1");

// \u521d\u671f\u8a2d\u5b9a
public void setup() {
  size(800,400, P3D);
  smooth();
  
  // procressing\u306e\u53d7\u4fe1\u30dd\u30fc\u30c8(pd\u306e\u9001\u4fe1\u30dd\u30fc\u30c8)
  oscP5 = new OscP5(this,7702);
  
  // procressing\u306e\u9001\u4fe1\u30dd\u30fc\u30c8(pd\u306e\u53d7\u4fe1\u30dd\u30fc\u30c8)
  //myRemoteLocation = new NetAddress("127.0.0.1",7702);

  oscval = new float[8];
  for (int i = 0; i < 8; i++){
    oscval[i] = 0;
  }

}

// \u63cf\u753b
public void draw() {

  // \u753b\u9762\u306e\u521d\u671f\u5316
  background(240);
  // Y\u65b9\u5411\u306e\u79fb\u52d5\u3000\u753b\u9762\u4e2d\u592e\u306b\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u914d\u7f6e
  translate(0, height/2, 0);
  
  // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
  for(int i = 4; i < 8; i++){
    drawSphere(i);
  }
}

// \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
public void drawSphere(int n){

  // \u521d\u671f\u8a2d\u5b9a
  stroke(255, 50);
  translate(width/5, 0, 0);
  rotateX(mouseY * 0.05f);
  sphereDetail(20);

  // \u30c8\u30ea\u30ac\u30fc\u3092\u53d6\u5f97
  float r = oscval[n];

  // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3078\u30a2\u30b5\u30a4\u30f3\u3059\u308b
  fill(0, 0, 0, r * 60 + 20);
  sphere(r * 100);

  // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u30b5\u30a4\u30ba\u3092\u7e2e\u5c0f
  float rate = 0.93f;
  oscval[n] = r * rate;
}

// \u30de\u30a6\u30b9\u30d7\u30ec\u30b9\u6642\u30a4\u30d9\u30f3\u30c8
public void mousePressed() {

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

// \u30de\u30a6\u30b9\u30ea\u30ea\u30fc\u30b9\u6642\u30a4\u30d9\u30f3\u30c8
public void mouseReleased(){

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
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "OSC_local03" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
