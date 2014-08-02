import java.util.ArrayList;

// GUI用
import controlP5.*;
ControlP5 controlP5;
Slider sl_K;	// 結合強度
Slider sl_K_Master;	// 結合強度

ArrayList<sphereObj> sphereObjList;  // 球リスト
ArrayList<sphereObj> masterObjList;  // 球リスト
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
  sl_K_Master = controlP5.addSlider("Master")
  		.setPosition(6, 40)
  		.setRange(0, 0.2)
  		;

	// 球リスト
 	sphereObjList = new ArrayList<sphereObj>();
 	masterObjList = new ArrayList<sphereObj>();

 	// 球の作成
 	for(int i = 0; i < 100; i++){
 		sphereObj theSphere = new sphereObj();
 		theSphere.m_center = new PVector((8 * i), height/2, -200);	// 中心座標
 		theSphere.m_radius = 20;	// 半径
 		theSphere.m_phase = random(1)*TWO_PI;		// 初期位相
 		theSphere.m_frequency = random(0.01, 0.1);	// 固有振動数
 		sphereObjList.add(theSphere);
 	}

 	// マスターの作成
 	sphereObj masterSphere = new sphereObj();
	masterSphere.m_center = new PVector(-100, height/2, -200);	// 中心座標
	masterSphere.m_radius = 20;	// 半径
	masterSphere.m_phase = 0;		// 初期位相
	masterSphere.m_frequency = 0.1;	// 固有振動数
	masterSphere.m_time = 0;
  masterSphere.m_freqList = new ArrayList<Float>();
 	masterObjList.add(masterSphere);;
}

// 描画
void draw(){

	// 描画設定
	background(200, 200, 200);
	strokeWeight(0.3);
	stroke(100);
	sphereDetail(6);

  	// 球の描画
	drawSphere();
}

// 球の描画
void drawSphere(){

	// 位置情報計算
	sphereObj masterSphere = masterObjList.get(0);
	masterSphere.m_phase += masterSphere.m_frequency;
	masterSphere.m_center.y = (sin(masterSphere.m_phase) * height)/2 + height/2;
	
	// マスターの描画
	pushMatrix();
		translate(masterSphere.m_center.x, masterSphere.m_center.y, masterSphere.m_center.z);
		sphere(masterSphere.m_radius);
	popMatrix();

	// 全オブジェクトをループ
	for(int i = 0; i < sphereObjList.size(); i++){
		
	  // 位置情報計算
	  sphereObj theSphere = sphereObjList.get(i);
	  theSphere.m_phase += theSphere.m_frequency;
	  theSphere.m_center.y = (sin(theSphere.m_phase) * height)/2 + height/2;

	  // 自身以外のオブジェクトとの位相差の正弦値の総和を計算
	  float sigma_dif = 0;
	  for(int j = 0; j < sphereObjList.size(); j++){
	  	if(i != j){
	  			sphereObj otherSphere = sphereObjList.get(j);
	  			float d_phase = otherSphere.m_phase - theSphere.m_phase;
	  			sigma_dif += sin(d_phase);
	  	}	  
	  }

	  // 結合強度(GUIで変化させる)
	  float K = sl_K.getValue();
	  float K_Master = sl_K_Master.getValue();

	  // 位相差の正弦値の総和をオブジェクト数で除算し、結合強度を乗算する
	  //float effectedPhase = theSphere.m_phase + (sigma_dif / sphereObjList.size() * K);
	  float d_phase_master = masterSphere.m_phase - theSphere.m_phase;
	  float effectedPhase = theSphere.m_phase + K * (sigma_dif / sphereObjList.size()) + K_Master * sin(d_phase_master);
		
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

// キー押下時のイベント
void keyPressed(){

	// スペースキーのタップでテンポを決める
  if(key == 32){

		sphereObj masterSphere = masterObjList.get(0);

		// 現在タップした時刻と前回タップした時刻（アプリ開始からの経過時間）を取得
		int current_time = millis();
		int previous_time = masterSphere.m_time;

		// 前回のタップからの経過時間を計算する
		int d_time = current_time - previous_time;

		// 時刻を更新する
		masterSphere.m_time = current_time;

		// 角速度(rad/s)を計算
		// 二度のタップ間でpiの位相差があると考える
		float d_ang_vel = (PI / d_time) * 1000;
		// 1フレームごとの位相差を計算（アニメーションにはこの角速度を利用する）
		float d_ang_vel_per_frame = d_ang_vel / frameRate;

		// 角速度のリストに追加する
	  masterSphere.m_freqList.add(d_ang_vel_per_frame);
	  // 角速度が4つ以上登録されていれば、一番古い値を削除
	  if(masterSphere.m_freqList.size() > 4){
	  	masterSphere.m_freqList.remove(0);
	  }

	  // 過去4回取得した角速度の平均値を計算
	  float sum_freq = 0;
	  for(int i = 0; i < masterSphere.m_freqList.size(); i++){
	  	sum_freq += masterSphere.m_freqList.get(i);
	  }
	  float av_freq = sum_freq / masterSphere.m_freqList.size();

	  // 平均角速度をマスターに登録(描画に利用する)
	  masterSphere.m_frequency = av_freq;
  }  
}

// 球クラス
class sphereObj{
  PVector m_center;		// 中心座標
  float m_radius;			// 指定した点との距離
  float m_phase;			// 位相
  float m_frequency;	// 固有振動数

  int m_time;	// 時間

  ArrayList<Float> m_freqList; // 角速度リスト

  // コンストラクタ
  void sphereObj(){}

}