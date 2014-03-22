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

public class OSC_local01 extends PApplet {

/*
  OSC\u306b\u3088\u308b\u901a\u4fe1\u306e\u30b5\u30f3\u30d7\u30eb
  \u30ed\u30fc\u30ab\u30eb\u4e0a\u3067\u306e\u901a\u4fe1
  \u30de\u30a6\u30b9\u30c9\u30e9\u30c3\u30b0\u6642\u3001\u30de\u30a6\u30b9\u30dd\u30a4\u30f3\u30bf\u306e\u5ea7\u6a19\u3092\u9001\u4fe1\u3059\u308b
*/



  
OscP5 oscP5;
NetAddress myRemoteLocation;

// \u901a\u4fe1\u30aa\u30d6\u30b8\u30a7\u30af\u30c8(\u5f15\u6570\u306bprefix\u3092\u6307\u5b9a)
OscMessage myMessage = new OscMessage("/prefix1");

// \u521d\u671f\u8a2d\u5b9a
public void setup() {
  size(400,400);
  
  // procressing\u306e\u53d7\u4fe1\u30dd\u30fc\u30c8(pd\u306e\u9001\u4fe1\u30dd\u30fc\u30c8)
  oscP5 = new OscP5(this,7702);
  
  // procressing\u306e\u9001\u4fe1\u30dd\u30fc\u30c8(pd\u306e\u53d7\u4fe1\u30dd\u30fc\u30c8)
  myRemoteLocation = new NetAddress("127.0.0.1",7702);

}

// \u63cf\u753b
public void draw() {

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
  oscP5.send(myMessage, myRemoteLocation);
}

// \u30de\u30a6\u30b9\u30ea\u30ea\u30fc\u30b9\u6642\u30a4\u30d9\u30f3\u30c8
public void mouseReleased(){

}

// OSC\u53d7\u4fe1\u30a4\u30d9\u30f3\u30c8
public void oscEvent(OscMessage theOscMessage) {

    // prefix\u306e\u8868\u793a
  print(theOscMessage.addrPattern() + " ");
  // \u5024\u306e\u8868\u793a
  //String val = theOscMessage.get(0).stringValue();
  String val = theOscMessage.get(0).intValue() + " " + theOscMessage.get(1).intValue();
  println(val);
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "OSC_local01" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
