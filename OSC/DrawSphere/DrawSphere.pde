
import processing.opengl.*;
import java.util.ArrayList;  
import java.util.Collections;  
import java.util.Comparator;  
import java.util.List;  

float _noiseSeed;	// ノイズの種

ArrayList<dotObj> dotList;	// 球上の点リスト
ArrayList<dotObj> minDotList; // 球上の点リスト

int step = 0;
float t = 0;

float lightPower;        //  光の強さ
float lightPowerAdd = 1; //  光の強さの加算値


float oscp;

// 初期設定
void setup() {
  size(1000,600, OPENGL);
  smooth();

  // ノイズの種をランダムに設定
  _noiseSeed = random(10);

  // 球上の点リスト
  dotList = new ArrayList<dotObj>();
  minDotList = new ArrayList<dotObj>();

  oscp = 0;
}


// 光るパーティクルの描画
void drwawParticle(PVector particle, float s) {
	
	//	光の強さを変更
	float radianS = radians(s);
	lightPower = abs(sin(radianS) * 30);
 
  loadPixels();
	for (int y = 0; y < height; y++) {
	  for (int x = 0; x < width; x++) {
	    int pixelIndex = x + y * width;

	    //  ピクセルから、赤・緑・青の要素を取りだす
	    int r = pixels[pixelIndex] >> 16 & 0xFF;
	    int g = pixels[pixelIndex] >> 8 & 0xFF;
	    int b = pixels[pixelIndex] & 0xFF;

	    //  パーティクルの中心と、ピクセルとの距離を計算する
	    float distance = dist(particle.x, particle.y, particle.z, x, y, 0);

	    //  0除算の回避
	    if(distance < 1){
	      distance = 1;
	    }

	    //  光の強さを距離で割る
	    r += (255 * lightPower) / distance;
	    g += (255 * lightPower) / distance;
	    b += (255 * lightPower) / distance;

	    //  ピクセルの色を変更する
	    pixels[pixelIndex] = color(r, g, b);
	  }
	}
  updatePixels();
}

// 描画
void draw() {

	// 画面の初期化
	background(20, 60);

	pushMatrix();

	  // Y方向の移動　画面中央にオブジェクトを配置
	  translate(width/2, height/2, 0);

	  // オブジェクトの描画
	  for(int i = 0; i < 8; i++){
      stroke(255 - (10 * i), 200 - (30 * i));
	    drawSphereRotate(i);
	  }

	popMatrix();

  // 光る点を描画
	PVector particle = new PVector(width/2, height/2, 0);
	//drwawParticle(particle, t);
}


// オブジェクトの描画
void drawSphereRotate(int n){

  // トリガーを取得
  float radius = n * 30;

  // 点の太さ
  strokeWeight(3);

  // リストの初期化
  dotList.clear();

  t = t + 1;

  pushMatrix();
    rotateX(frameCount * n * 0.005);
    rotateY(frameCount * n * 0.005);
    rotateZ(frameCount * n * 0.005);
    rotateZ(oscp);

    PVector lastPos = new PVector(0, 0, 0);
    PVector thisPos = new PVector(0, 0, 0);
    float noisFactor = noise(_noiseSeed);
    
    for(float i = 0; i < 360; i = i + 32){ // 横
    //for(float i = 0; i < 360; i = i + 64*noise(_noiseSeed)){ // 横
      float radianS = radians(i);
      for(float j = 0; j < 360; j = j + 18){ // 縦
      //for(float j = 0; j < 360; j = j + 64*noise(_noiseSeed)){ // 縦
        float radianT = radians(j);
        float radiant = radians(t);
        thisPos.x = 0 + (radius * cos(radianS) * sin(radianT));// + (noisFactor*10)-5;
        thisPos.y = 0 + (radius * sin(radianS) * sin(radianT));
        thisPos.z = 0 + (radius * cos(radianT));//  + (noisFactor*10)-5;
        if(lastPos.x != 0){
          //point(thisPos.x, thisPos.y, thisPos.z);
        }
        
        // リストへの追加
        dotObj d1 = new dotObj();
        d1.p = new PVector(thisPos.x, thisPos.y, thisPos.z);
        dotList.add(d1);

        // 直前の点を更新
        lastPos = thisPos.get();
        // ノイズの種を更新
        _noiseSeed = _noiseSeed + 0.01;
      }
    }

    step++;

    // 点の間引き
    for(int i = 0; i < dotList.size(); i++){
      int index = (int)random(dotList.size());
      dotList.remove(index);
    }

    // 最短点の探索
    drawCloseDot(n);

  popMatrix();
}

// 最短点の探索
void drawCloseDot(int n){

  // 線の太さ（外側をより細く）
  strokeWeight((9 - n)/3 + 0.5);

  pushMatrix();
    // リストの全要素をループ
    for(int i = 1; i < dotList.size(); i++){
      // リストの初期化
      minDotList.clear();
      // 最短距離の点
      PVector min_Pos = new PVector(0, 0, 0);
      // リストから要素を取得
      PVector p1 = dotList.get(i).p;
      //　取得した要素との距離を全要素に対して計算
      for(int j = 1; j < dotList.size(); j++){
        PVector p2 = dotList.get(j).p;
        // 二点間の距離
        float d1 = dist(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
        // 最短距離にある点との距離
        float d2 = dist(p1.x, p1.y, p1.z, min_Pos.x, min_Pos.y, min_Pos.z);
        // 二つの要素が同一のもので無い場合
        // 二点間の距離を計算し、最小値を更新する
        if(d1 <= d2 && d1 != 0){
          // 最短距離のオブジェクト
          min_Pos = p2.get();
          dotObj dm = new dotObj();
          dm.p = new PVector(p2.x, p2.y, p2.z);
          dm.d = d1; // 点からの距離を保持
          // リストへの追加
          minDotList.add(dm);
        }
      }

      // 配列のソート（現在の点との距離でソート）
      Collections.sort(minDotList, new CompareTwoPoint());
      for(int k = 0; k < minDotList.size(); k++){
        PVector pmin = minDotList.get(k).p;
        // 現在の点から線を引く
        line(p1.x, p1.y, p1.z, pmin.x, pmin.y, pmin.z);
      }
    }
  popMatrix();
}

// 二点間の距離を比較
class CompareTwoPoint implements Comparator<dotObj>
{
  //@Override
  int compare(dotObj v1, dotObj v2)
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

// 点
class dotObj{
  PVector p;  // 座標
  float d;    // 指定した点との距離

  // コンストラクタ
  void dotObj(){}

  void dotObj(float x, float y, float z){
    p = new PVector(x, y, z);
  }
}

void mousePressed(){
  oscp = 100;
}

void mouseReleased(){
  oscp = -100;
}
