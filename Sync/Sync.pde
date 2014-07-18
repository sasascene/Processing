import java.util.ArrayList;

// GUI用
import controlP5.*;
ControlP5 controlP5;
Slider sl_K;	// 結合強度

ArrayList<sphereObj> sphereObjList;  // 球リスト
float _noiseSeed; // ノイズの種

// 初期設定
void setup(){
	size(800, 400, OPENGL);
	frameRate(64);
	noFill();
	smooth();

	// GUI
  controlP5 = new ControlP5(this);
  sl_K = controlP5.addSlider("K")
  		.setPosition(6, 20)
  		.setRange(0, 0.15)
  		;

	// 球リスト
 	sphereObjList = new ArrayList<sphereObj>();

 	// 球の作成
 	for(int i = 0; i < 100; i++){
 		sphereObj theSphere = new sphereObj();
 		theSphere.m_center = new PVector((8 * i), height/2, -200);	// 中心座標
 		theSphere.m_radius = 20;	// 半径
 		theSphere.m_phase = random(1)*TWO_PI;		// 初期位相
 		theSphere.m_frequency = random(0.01, 0.1);	// 固有振動数
 		sphereObjList.add(theSphere);
 	}
}

// 描画
void draw(){

	// 描画設定
	background(200, 200, 200);
	//background(10, 10, 10);
	strokeWeight(0.3);
	stroke(100);
	sphereDetail(10);

  // 球の描画
	drawSphere();
}

// 球の描画
void drawSphere(){

	// 全オブジェクトをループ
	for(int i = 0; i < sphereObjList.size(); i++){
		
		// 位置情報計算
	  sphereObj theSphere = sphereObjList.get(i);
	  theSphere.m_phase += theSphere.m_frequency;
	  theSphere.m_center.y = (sin(theSphere.m_phase) * height)/2 + height/2;
	  // theSphere.m_radius = (sin(theSphere.m_phase) * 30);

	  // 自身以外のオブジェクトとの位相差の正弦値の総和を計算
	  float sigma_dif = 0;
	  for(int j = 0; j < sphereObjList.size(); j++){
	  	if(i != j){
	  			sphereObj otherSphere = sphereObjList.get(j);
	  			float d_phase = otherSphere.m_phase - theSphere.m_phase;
	  			sigma_dif += sin(d_phase);
	  	}	  
	  }

	  // 結合強度(LFOで変化させる)
	  // float K = map(sin(frameCount * 0.01), -1, 1, 0, 0.12);

	  // 結合強度(GUIで変化させる)
	  float K = sl_K.getValue();

	  // 位相差の正弦値の総和をオブジェクト数で除算し、結合強度を乗算する
	  float effectedPhase = theSphere.m_phase + (sigma_dif / sphereObjList.size() * K);
		
	  // ノイズを加える
		effectedPhase += noise(_noiseSeed) * 0.05; 

	  // 同期された位相をオブジェクトに設定
	  theSphere.m_phase = effectedPhase;

	  // 描画
	  pushMatrix();
		  translate(theSphere.m_center.x, theSphere.m_center.y, theSphere.m_center.z);
		  rotateX(frameCount * 0.04);
		  rotateY(frameCount * 0.04);
		  sphere(theSphere.m_radius);
	  popMatrix();

	  // ノイズの更新
	  _noiseSeed += 0.01;
	}
}

// 球クラス
class sphereObj{
  PVector m_center;  // 中心座標
  float m_radius;    // 半径
  float m_phase;	 // 位相
  float m_frequency; // 固有振動数

  // コンストラクタ
  void sphereObj(){}

  void sphereObj(float x, float y, float z){
    m_center = new PVector(x, y, z);
  }
}