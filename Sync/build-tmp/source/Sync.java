import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.ArrayList; 
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Sync extends PApplet {



// GUI\u7528

ControlP5 controlP5;
Slider sl_K;	// \u7d50\u5408\u5f37\u5ea6

ArrayList<sphereObj> sphereObjList;  // \u7403\u30ea\u30b9\u30c8
float _noiseSeed; // \u30ce\u30a4\u30ba\u306e\u7a2e

// \u521d\u671f\u8a2d\u5b9a
public void setup(){
	size(800, 400, OPENGL);
	frameRate(64);
	noFill();
	smooth();

	// GUI
  controlP5 = new ControlP5(this);
  sl_K = controlP5.addSlider("K")
  		.setPosition(6, 20)
  		.setRange(0, 0.15f)
  		;

	// \u7403\u30ea\u30b9\u30c8
 	sphereObjList = new ArrayList<sphereObj>();

 	// \u7403\u306e\u4f5c\u6210
 	for(int i = 0; i < 100; i++){
 		sphereObj theSphere = new sphereObj();
 		theSphere.m_center = new PVector((8 * i), height/2, -200);	// \u4e2d\u5fc3\u5ea7\u6a19
 		theSphere.m_radius = 20;	// \u534a\u5f84
 		theSphere.m_phase = random(1)*TWO_PI;		// \u521d\u671f\u4f4d\u76f8
 		theSphere.m_frequency = random(0.01f, 0.1f);	// \u56fa\u6709\u632f\u52d5\u6570
 		sphereObjList.add(theSphere);
 	}
}

// \u63cf\u753b
public void draw(){

	// \u63cf\u753b\u8a2d\u5b9a
	background(200, 200, 200);
	//background(10, 10, 10);
	strokeWeight(0.3f);
	stroke(100);
	sphereDetail(10);

  // \u7403\u306e\u63cf\u753b
	drawSphere();
}

// \u7403\u306e\u63cf\u753b
public void drawSphere(){

	// \u5168\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u30eb\u30fc\u30d7
	for(int i = 0; i < sphereObjList.size(); i++){
		
		// \u4f4d\u7f6e\u60c5\u5831\u8a08\u7b97
	  sphereObj theSphere = sphereObjList.get(i);
	  theSphere.m_phase += theSphere.m_frequency;
	  theSphere.m_center.y = (sin(theSphere.m_phase) * height)/2 + height/2;
	  // theSphere.m_radius = (sin(theSphere.m_phase) * 30);

	  // \u81ea\u8eab\u4ee5\u5916\u306e\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3068\u306e\u4f4d\u76f8\u5dee\u306e\u6b63\u5f26\u5024\u306e\u7dcf\u548c\u3092\u8a08\u7b97
	  float sigma_dif = 0;
	  for(int j = 0; j < sphereObjList.size(); j++){
	  	if(i != j){
	  			sphereObj otherSphere = sphereObjList.get(j);
	  			float d_phase = otherSphere.m_phase - theSphere.m_phase;
	  			sigma_dif += sin(d_phase);
	  	}	  
	  }

	  // \u7d50\u5408\u5f37\u5ea6(LFO\u3067\u5909\u5316\u3055\u305b\u308b)
	  // float K = map(sin(frameCount * 0.01), -1, 1, 0, 0.12);

	  // \u7d50\u5408\u5f37\u5ea6(GUI\u3067\u5909\u5316\u3055\u305b\u308b)
	  float K = sl_K.getValue();

	  // \u4f4d\u76f8\u5dee\u306e\u6b63\u5f26\u5024\u306e\u7dcf\u548c\u3092\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u6570\u3067\u9664\u7b97\u3057\u3001\u7d50\u5408\u5f37\u5ea6\u3092\u4e57\u7b97\u3059\u308b
	  float effectedPhase = theSphere.m_phase + (sigma_dif / sphereObjList.size() * K);
		
	  // \u30ce\u30a4\u30ba\u3092\u52a0\u3048\u308b
		effectedPhase += noise(_noiseSeed) * 0.05f; 

	  // \u540c\u671f\u3055\u308c\u305f\u4f4d\u76f8\u3092\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306b\u8a2d\u5b9a
	  theSphere.m_phase = effectedPhase;

	  // \u63cf\u753b
	  pushMatrix();
		  translate(theSphere.m_center.x, theSphere.m_center.y, theSphere.m_center.z);
		  rotateX(frameCount * 0.04f);
		  rotateY(frameCount * 0.04f);
		  sphere(theSphere.m_radius);
	  popMatrix();

	  // \u30ce\u30a4\u30ba\u306e\u66f4\u65b0
	  _noiseSeed += 0.01f;
	}
}

// \u7403\u30af\u30e9\u30b9
class sphereObj{
  PVector m_center;  // \u4e2d\u5fc3\u5ea7\u6a19
  float m_radius;    // \u6307\u5b9a\u3057\u305f\u70b9\u3068\u306e\u8ddd\u96e2
  float m_phase;	 // \u4f4d\u76f8
  float m_frequency; // \u56fa\u6709\u632f\u52d5\u6570

  // \u30b3\u30f3\u30b9\u30c8\u30e9\u30af\u30bf
  public void sphereObj(){}

  public void sphereObj(float x, float y, float z){
    m_center = new PVector(x, y, z);
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Sync" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
