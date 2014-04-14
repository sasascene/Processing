import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.opengl.*; 
import java.util.ArrayList; 
import java.util.Collections; 
import java.util.Comparator; 
import java.util.List; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class DrawSphere extends PApplet {



  
  
  
  

float _noiseSeed;	// \u30ce\u30a4\u30ba\u306e\u7a2e

ArrayList<dotObj> dotList;	// \u7403\u4e0a\u306e\u70b9\u30ea\u30b9\u30c8
ArrayList<dotObj> minDotList; // \u7403\u4e0a\u306e\u70b9\u30ea\u30b9\u30c8

int step = 0;
float t = 0;

float lightPower;        //  \u5149\u306e\u5f37\u3055
float lightPowerAdd = 1; //  \u5149\u306e\u5f37\u3055\u306e\u52a0\u7b97\u5024


float oscp;

// \u521d\u671f\u8a2d\u5b9a
public void setup() {
  size(1000,600, OPENGL);
  smooth();

  // \u30ce\u30a4\u30ba\u306e\u7a2e\u3092\u30e9\u30f3\u30c0\u30e0\u306b\u8a2d\u5b9a
  _noiseSeed = random(10);

  // \u7403\u4e0a\u306e\u70b9\u30ea\u30b9\u30c8
  dotList = new ArrayList<dotObj>();
  minDotList = new ArrayList<dotObj>();

  oscp = 0;
}


// \u5149\u308b\u30d1\u30fc\u30c6\u30a3\u30af\u30eb\u306e\u63cf\u753b
public void drwawParticle(PVector particle, float s) {
	
	//	\u5149\u306e\u5f37\u3055\u3092\u5909\u66f4
	float radianS = radians(s);
	lightPower = abs(sin(radianS) * 30);
 
  loadPixels();
	for (int y = 0; y < height; y++) {
	  for (int x = 0; x < width; x++) {
	    int pixelIndex = x + y * width;

	    //  \u30d4\u30af\u30bb\u30eb\u304b\u3089\u3001\u8d64\u30fb\u7dd1\u30fb\u9752\u306e\u8981\u7d20\u3092\u53d6\u308a\u3060\u3059
	    int r = pixels[pixelIndex] >> 16 & 0xFF;
	    int g = pixels[pixelIndex] >> 8 & 0xFF;
	    int b = pixels[pixelIndex] & 0xFF;

	    //  \u30d1\u30fc\u30c6\u30a3\u30af\u30eb\u306e\u4e2d\u5fc3\u3068\u3001\u30d4\u30af\u30bb\u30eb\u3068\u306e\u8ddd\u96e2\u3092\u8a08\u7b97\u3059\u308b
	    float distance = dist(particle.x, particle.y, particle.z, x, y, 0);

	    //  0\u9664\u7b97\u306e\u56de\u907f
	    if(distance < 1){
	      distance = 1;
	    }

	    //  \u5149\u306e\u5f37\u3055\u3092\u8ddd\u96e2\u3067\u5272\u308b
	    r += (255 * lightPower) / distance;
	    g += (255 * lightPower) / distance;
	    b += (255 * lightPower) / distance;

	    //  \u30d4\u30af\u30bb\u30eb\u306e\u8272\u3092\u5909\u66f4\u3059\u308b
	    pixels[pixelIndex] = color(r, g, b);
	  }
	}
  updatePixels();
}

// \u63cf\u753b
public void draw() {

	// \u753b\u9762\u306e\u521d\u671f\u5316
	background(20);

	pushMatrix();

	  // Y\u65b9\u5411\u306e\u79fb\u52d5\u3000\u753b\u9762\u4e2d\u592e\u306b\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u914d\u7f6e
	  translate(width/2, height/2, 0);

	  // \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
	  for(int i = 3; i < 8; i++){
      stroke(255 - (10 * i), 200 - (20 * i));
	    drawSphereRotate(i);
	  }

	popMatrix();

	PVector particle = new PVector(width/2, height/2, 0);
	drwawParticle(particle, t);
}


// \u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u63cf\u753b
public void drawSphereRotate(int n){

  // \u30c8\u30ea\u30ac\u30fc\u3092\u53d6\u5f97
  float radius = n * 40;

  strokeWeight(5);

  // \u30ea\u30b9\u30c8\u306e\u521d\u671f\u5316
  dotList.clear();

  t = t + 1;
  float radt = radians(t);

  pushMatrix();
    rotateX(frameCount * n * 0.1f + oscp);
    rotateY(frameCount * n * 0.1f + oscp);
    float s = 0;

    PVector lastPos = new PVector(0, 0, 0);
    PVector thisPos = new PVector(0, 0, 0);

    float noisFactor = noise(_noiseSeed);
    float noisVal = 0;
    for(float i = 0; i < 360; i = i + 36){ // \u6a2a
    //for(float i = 0; i < 360; i = i + 64*noise(_noiseSeed)){ // \u6a2a
      float radianS = radians(i);
      for(float j = 0; j < 360; j = j + 18){ // \u7e26
      //for(float j = 0; j < 360; j = j + 64*noise(_noiseSeed)){ // \u7e26
        float radianT = radians(j);
        float radiant = radians(t);
        thisPos.x = 0 + (radius * cos(radianS) * sin(radianT)) + (noisFactor*10)-5;
        thisPos.y = 0 + (radius * sin(radianS) * sin(radianT));
        thisPos.z = 0 + (radius * cos(radianT))  + (noisFactor*10)-5;
        if(lastPos.x != 0){
          point(thisPos.x, thisPos.y, thisPos.z);
        }
        
        dotObj d1 = new dotObj();
        d1.p = new PVector(thisPos.x, thisPos.y, thisPos.z);
        dotList.add(d1);
        //dotList.add(thisPos.get());

        lastPos = thisPos.get();
        _noiseSeed = _noiseSeed + 0.01f;
      }
    }

    step++;

    // \u70b9\u306e\u9593\u5f15\u304d
    for(int i = 0; i < dotList.size(); i++){
      int index = (int)random(dotList.size());
      dotList.remove(index);
    }

    // \u6700\u77ed\u70b9\u306e\u63a2\u7d22
    drawCloseDot(n);

  popMatrix();
}

// \u6700\u77ed\u70b9\u306e\u63a2\u7d22
public void drawCloseDot(int n){

  strokeWeight((9 - n)/2);

  // \u30ea\u30b9\u30c8\u306e\u521d\u671f\u5316
  pushMatrix();
    //beginShape();
    noFill();
    for(int i = 1; i < dotList.size(); i++){
      minDotList.clear();
      PVector min_Pos = new PVector(0, 0, 0);
      PVector p1 = dotList.get(i).p;
      for(int j = 1; j < dotList.size(); j++){
        PVector p2 = dotList.get(j).p;
        float d1 = dist(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
        float d2 = dist(p1.x, p1.y, p1.z, min_Pos.x, min_Pos.y, min_Pos.z);
        if(d1 <= d2 && d1 != 0){
          min_Pos = p2.get();
          dotObj dm = new dotObj();
          dm.p = new PVector(p2.x, p2.y, p2.z);
          dm.d = d1;
          minDotList.add(dm);
          //minDotList.add(p2.get());
        }
      }

      // \u914d\u5217\u306e\u30bd\u30fc\u30c8
      Collections.sort(minDotList, new CompareTwoPoint());
      for(int k = 0; k < minDotList.size(); k++){
        PVector pmin = minDotList.get(k).p;
        //curveVertex(pmin.x, pmin.y, pmin.z);
        line(p1.x, p1.y, p1.z, pmin.x, pmin.y, pmin.z);
      }
    }
    //endShape();
  popMatrix();
}

// \u4e8c\u70b9\u9593\u306e\u8ddd\u96e2\u3092\u6bd4\u8f03
class CompareTwoPoint implements Comparator<dotObj>
{
  //@Override
  public int compare(dotObj v1, dotObj v2)
  {
    int d = 0;
    if(v1.d > v2.d){
      d = 1;
    }else if(v1.d == v2.d){
      d = 0;
    }else{
      d = -1;
    }

    return d;
  }
}

// \u70b9
class dotObj{
  PVector p;
  float d;

  public void dotObj(){}

  public void dotObj(float x, float y, float z){
    p = new PVector(x, y, z);
  }
}

public void mousePressed(){
  oscp = 100;
}

public void mouseReleased(){
  oscp = -100;
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "DrawSphere" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
