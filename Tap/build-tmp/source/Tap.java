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

public class Tap extends PApplet {



// GUI\u7528

ControlP5 controlP5;
Slider sl_K;	// \u7d50\u5408\u5f37\u5ea6
Slider sl_K_Master;	// \u7d50\u5408\u5f37\u5ea6

ArrayList<sphereObj> sphereObjList;  // \u7403\u30ea\u30b9\u30c8
ArrayList<sphereObj> masterObjList;  // \u7403\u30ea\u30b9\u30c8
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
  sl_K_Master = controlP5.addSlider("Master")
  		.setPosition(6, 40)
  		.setRange(0, 0.2f)
  		;

	// \u7403\u30ea\u30b9\u30c8
 	sphereObjList = new ArrayList<sphereObj>();
 	masterObjList = new ArrayList<sphereObj>();

 	// \u7403\u306e\u4f5c\u6210
 	for(int i = 0; i < 100; i++){
 		sphereObj theSphere = new sphereObj();
 		theSphere.m_center = new PVector((8 * i), height/2, -200);	// \u4e2d\u5fc3\u5ea7\u6a19
 		theSphere.m_radius = 20;	// \u534a\u5f84
 		theSphere.m_phase = random(1)*TWO_PI;		// \u521d\u671f\u4f4d\u76f8
 		theSphere.m_frequency = random(0.01f, 0.1f);	// \u56fa\u6709\u632f\u52d5\u6570
 		sphereObjList.add(theSphere);
 	}

 	// \u30de\u30b9\u30bf\u30fc\u306e\u4f5c\u6210
 	sphereObj masterSphere = new sphereObj();
	masterSphere.m_center = new PVector(-100, height/2, -200);	// \u4e2d\u5fc3\u5ea7\u6a19
	masterSphere.m_radius = 20;	// \u534a\u5f84
	masterSphere.m_phase = 0;		// \u521d\u671f\u4f4d\u76f8
	masterSphere.m_frequency = 0.1f;	// \u56fa\u6709\u632f\u52d5\u6570
	masterSphere.m_time = 0;
  masterSphere.m_freqList = new ArrayList<Float>();
 	masterObjList.add(masterSphere);;
}

// \u63cf\u753b
public void draw(){

	// \u63cf\u753b\u8a2d\u5b9a
	background(200, 200, 200);
	strokeWeight(0.3f);
	stroke(100);
	sphereDetail(6);

  	// \u7403\u306e\u63cf\u753b
	drawSphere();
}

// \u7403\u306e\u63cf\u753b
public void drawSphere(){

	// \u4f4d\u7f6e\u60c5\u5831\u8a08\u7b97
	sphereObj masterSphere = masterObjList.get(0);
	masterSphere.m_phase += masterSphere.m_frequency;
	masterSphere.m_center.y = (sin(masterSphere.m_phase) * height)/2 + height/2;
	
	// \u30de\u30b9\u30bf\u30fc\u306e\u63cf\u753b
	pushMatrix();
		translate(masterSphere.m_center.x, masterSphere.m_center.y, masterSphere.m_center.z);
		sphere(masterSphere.m_radius);
	popMatrix();

	// \u5168\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u30eb\u30fc\u30d7
	for(int i = 0; i < sphereObjList.size(); i++){
		
	  // \u4f4d\u7f6e\u60c5\u5831\u8a08\u7b97
	  sphereObj theSphere = sphereObjList.get(i);
	  theSphere.m_phase += theSphere.m_frequency;
	  theSphere.m_center.y = (sin(theSphere.m_phase) * height)/2 + height/2;

	  // \u81ea\u8eab\u4ee5\u5916\u306e\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3068\u306e\u4f4d\u76f8\u5dee\u306e\u6b63\u5f26\u5024\u306e\u7dcf\u548c\u3092\u8a08\u7b97
	  float sigma_dif = 0;
	  for(int j = 0; j < sphereObjList.size(); j++){
	  	if(i != j){
	  			sphereObj otherSphere = sphereObjList.get(j);
	  			float d_phase = otherSphere.m_phase - theSphere.m_phase;
	  			sigma_dif += sin(d_phase);
	  	}	  
	  }

	  // \u7d50\u5408\u5f37\u5ea6(GUI\u3067\u5909\u5316\u3055\u305b\u308b)
	  float K = sl_K.getValue();
	  float K_Master = sl_K_Master.getValue();

	  // \u4f4d\u76f8\u5dee\u306e\u6b63\u5f26\u5024\u306e\u7dcf\u548c\u3092\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u6570\u3067\u9664\u7b97\u3057\u3001\u7d50\u5408\u5f37\u5ea6\u3092\u4e57\u7b97\u3059\u308b
	  //float effectedPhase = theSphere.m_phase + (sigma_dif / sphereObjList.size() * K);
	  float d_phase_master = masterSphere.m_phase - theSphere.m_phase;
	  float effectedPhase = theSphere.m_phase + K * (sigma_dif / sphereObjList.size()) + K_Master * sin(d_phase_master);
		
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

// \u30ad\u30fc\u62bc\u4e0b\u6642\u306e\u30a4\u30d9\u30f3\u30c8
public void keyPressed(){

	// \u30b9\u30da\u30fc\u30b9\u30ad\u30fc\u306e\u30bf\u30c3\u30d7\u3067\u30c6\u30f3\u30dd\u3092\u6c7a\u3081\u308b
  if(key == 32){

		sphereObj masterSphere = masterObjList.get(0);

		// \u73fe\u5728\u30bf\u30c3\u30d7\u3057\u305f\u6642\u523b\u3068\u524d\u56de\u30bf\u30c3\u30d7\u3057\u305f\u6642\u523b\uff08\u30a2\u30d7\u30ea\u958b\u59cb\u304b\u3089\u306e\u7d4c\u904e\u6642\u9593\uff09\u3092\u53d6\u5f97
		int current_time = millis();
		int previous_time = masterSphere.m_time;

		// \u524d\u56de\u306e\u30bf\u30c3\u30d7\u304b\u3089\u306e\u7d4c\u904e\u6642\u9593\u3092\u8a08\u7b97\u3059\u308b
		int d_time = current_time - previous_time;

		// \u6642\u523b\u3092\u66f4\u65b0\u3059\u308b
		masterSphere.m_time = current_time;

		// \u89d2\u901f\u5ea6(rad/s)\u3092\u8a08\u7b97
		// \u4e8c\u5ea6\u306e\u30bf\u30c3\u30d7\u9593\u3067pi\u306e\u4f4d\u76f8\u5dee\u304c\u3042\u308b\u3068\u8003\u3048\u308b
		float d_ang_vel = (PI / d_time) * 1000;
		// 1\u30d5\u30ec\u30fc\u30e0\u3054\u3068\u306e\u4f4d\u76f8\u5dee\u3092\u8a08\u7b97\uff08\u30a2\u30cb\u30e1\u30fc\u30b7\u30e7\u30f3\u306b\u306f\u3053\u306e\u89d2\u901f\u5ea6\u3092\u5229\u7528\u3059\u308b\uff09
		float d_ang_vel_per_frame = d_ang_vel / frameRate;

		// \u89d2\u901f\u5ea6\u306e\u30ea\u30b9\u30c8\u306b\u8ffd\u52a0\u3059\u308b
	  masterSphere.m_freqList.add(d_ang_vel_per_frame);
	  // \u89d2\u901f\u5ea6\u304c4\u3064\u4ee5\u4e0a\u767b\u9332\u3055\u308c\u3066\u3044\u308c\u3070\u3001\u4e00\u756a\u53e4\u3044\u5024\u3092\u524a\u9664
	  if(masterSphere.m_freqList.size() > 4){
	  	masterSphere.m_freqList.remove(0);
	  }

	  // \u904e\u53bb4\u56de\u53d6\u5f97\u3057\u305f\u89d2\u901f\u5ea6\u306e\u5e73\u5747\u5024\u3092\u8a08\u7b97
	  float sum_freq = 0;
	  for(int i = 0; i < masterSphere.m_freqList.size(); i++){
	  	sum_freq += masterSphere.m_freqList.get(i);
	  }
	  float av_freq = sum_freq / masterSphere.m_freqList.size();

	  // \u5e73\u5747\u89d2\u901f\u5ea6\u3092\u30de\u30b9\u30bf\u30fc\u306b\u767b\u9332(\u63cf\u753b\u306b\u5229\u7528\u3059\u308b)
	  masterSphere.m_frequency = av_freq;
  }  
}

// \u7403\u30af\u30e9\u30b9
class sphereObj{
  PVector m_center;		// \u4e2d\u5fc3\u5ea7\u6a19
  float m_radius;			// \u6307\u5b9a\u3057\u305f\u70b9\u3068\u306e\u8ddd\u96e2
  float m_phase;			// \u4f4d\u76f8
  float m_frequency;	// \u56fa\u6709\u632f\u52d5\u6570

  int m_time;	// \u6642\u9593

  ArrayList<Float> m_freqList; // \u89d2\u901f\u5ea6\u30ea\u30b9\u30c8

  // \u30b3\u30f3\u30b9\u30c8\u30e9\u30af\u30bf
  public void sphereObj(){}

}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Tap" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
