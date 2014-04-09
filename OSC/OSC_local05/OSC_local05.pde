/*
  OSCによる通信のサンプル
  ローカル上での通信
  シーケンサとの同期
*/

import processing.opengl.*;
import oscP5.*;
import netP5.*;
  
OscP5 oscP5;
NetAddress myRemoteLocation;

// ノイズの種
float _noiseSeed;

float[] oscval; //  OSCで取得した値を格納する配列
float oscv; // 音量
float oscp; // パターン

float rad; // 半径

PVector[] pvecs;

// 球上の点リスト
ArrayList<PVector> dotList;


// 通信オブジェクト(引数にprefixを指定)
OscMessage myMessage = new OscMessage("/prefix1");

// 初期設定
void setup() {
  size(1000,600, OPENGL);
  smooth();

  // ノイズの種をランダムに設定
  _noiseSeed = random(10);
  
  // procressingの受信ポート(pdの送信ポート)
  oscP5 = new OscP5(this,7702);
  
  // procressingの送信ポート(pdの受信ポート)
  //myRemoteLocation = new NetAddress("127.0.0.1",7702);

  // 変数の初期化
  oscval = new float[8];
  for (int i = 0; i < 8; i++){
    oscval[i] = 0;
  }

  oscv = 0; // 音量
  oscp = 0; // パターン

  rad = 150;  // 半径

  pvecs = new PVector[500];
  for(int i = 0; i < 500; i++){
    pvecs[i] = new PVector(0, 0, 0);
  }

  // 球上の点リスト
  dotList = new ArrayList<PVector>();

}

// 描画
void draw() {

  // 音量を球の半径にアサイン
  rad = oscv;

  if(oscp == 0){

    // 画面の初期化
    background(20);

    pushMatrix();

      // Y方向の移動　画面中央にオブジェクトを配置
      translate(0, height/2, 0);

      // オブジェクトの描画
      for(int i = 0; i < 8; i++){
        drawSphereBig(i);
      }

    popMatrix();

  }else if(oscp == 1){
    // 画面の初期化
    fill(20, 20, 20, 70);
    rect(-10, -10, width+20, height+20);

    pushMatrix();

      // Y方向の移動　画面中央にオブジェクトを配置
      translate(0, height/2, 0);

      // オブジェクトの描画
      for(int i = 0; i < 8; i++){
        drawSphere_noise(i);
      }
    popMatrix();
  }else if(oscp == 2){

    // 画面の初期化
    background(20);

    pushMatrix();

      // Y方向の移動　画面中央にオブジェクトを配置
      translate(0, height/2, 0);

      // オブジェクトの描画
      for(int i = 0; i < 8; i++){
        drawSphereBasic(i);
      }
    popMatrix();
  }else if(oscp == 3){

    // 画面の初期化
    //fill(20, 20, 20, 70);
    //rect(-10, -10, width+20, height+20);
    background(20);

    pushMatrix();

      // Y方向の移動　画面中央にオブジェクトを配置
      translate(0, height/2, 0);

      // オブジェクトの描画
      for(int i = 0; i < 8; i++){
        drawSphere_E(i);
      }
    popMatrix();
  }else if(oscp == 4){
    // 画面の初期化
    background(20);

    pushMatrix();

      // Y方向の移動　画面中央にオブジェクトを配置
      translate(0, height/2, 0);

      // オブジェクトの描画
      for(int i = 0; i < 8; i++){
        drawSphereBasic(i);
      }

    popMatrix();

    pushMatrix();
      // Y方向の移動　画面中央にオブジェクトを配置
      translate(0, height/2, 0);
      // オブジェクトの描画
      for(int i = 5; i < 8; i++){
        drawSphere2(i);
      }
    popMatrix();
  }
}

// オブジェクトの描画
void drawSphereBig(int n){

  stroke(random(30)+220);

  // トリガーを取得
  //float radius = 50;
  // トリガーを取得
  float radius = oscval[n] * rad;

  // リストの初期化
  dotList.clear();

  translate(width/9, 0, 0);

  pushMatrix();
    rotateX(frameCount * 0.01);
    rotateY(frameCount * 0.01);
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

      _noiseSeed = _noiseSeed + 0.1;
      lastPos = thisPos.get();
    }

    // 最短点の探索
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
      strokeWeight(0.5);
      line(p1.x, p1.y, p1.z, min_Pos.x, min_Pos.y, min_Pos.z);
    }

  popMatrix();

  // オブジェクトのサイズを縮小
  //float rate = 0.95;
  //oscval[n] = radius / 2 / rad * rate;
  oscval[n] = oscval[n] * 0.9 ;
}


// オブジェクトの描画
void drawSphere2(int n){

  stroke(random(30)+220);

  // トリガーを取得
  float radius = oscval[n] * rad * 2;

  translate(width/4, 0, 0);

  pushMatrix();
    rotateX(frameCount * 0.03 * random(10));
    rotateY(frameCount * 0.04 * random(10));
    float s = 0;
    float t = 0;

    PVector lastPos = new PVector(0.1, 0, 0);
    PVector thisPos = new PVector(0, 0, 0);

    while(t < 180){
      //s += 18;
      //t += 0.3;
      s += noise(_noiseSeed) + 18;
      t += 0.9;
      float radianS = radians(s);
      float radianT = radians(t);

      thisPos.x = 0 + (radius * cos(radianS) * sin(radianT));
      thisPos.y = 0 + (radius * sin(radianS) * sin(radianT));
      thisPos.z = 0 + (radius * cos(radianT));

      if(lastPos.x != 0){
        strokeWeight(5);
        //line(thisPos.x, thisPos.y, thisPos.z, lastPos.x, lastPos.y, lastPos.z);
        if(random(1) > 0.3){
          line(thisPos.x, thisPos.y, thisPos.z, lastPos.x, lastPos.y, lastPos.z);
        }
        point(thisPos.x, thisPos.y, thisPos.z); 
      }

      _noiseSeed = _noiseSeed + 0.01;
      lastPos = thisPos.get();
    }
  popMatrix();

  // オブジェクトのサイズを縮小
  float rate = 0.95;
  oscval[n] = radius / 2 / rad * rate;
}

// オブジェクトの描画
void drawSphere_E(int n){

  stroke(random(30)+220, 200);

  // トリガーを取得
  float radius = oscval[n] * rad;

  translate(width/9, 0, 0);

  pushMatrix();
    rotateX(frameCount * 0.03 * random(10));
    rotateY(frameCount * 0.04 * random(10));
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

      _noiseSeed = _noiseSeed + 0.01;
      lastPos = thisPos.get();
    }
  popMatrix();

  // 画面のフラッシュ
  if(n == 1 && oscval[7] == 1){
    flash();
  }

  // オブジェクトのサイズを縮小
  float rate = 0.95;
  oscval[n] = radius / rad * rate;
}

// オブジェクトの描画
void drawSphere_noise(int n){

  stroke(random(20)+180, 80);

  // トリガーを取得
  float radius = oscval[n] * rad;
  translate(width/9, 0, 0);

  pushMatrix();
    rotateX(frameCount * 0.03 * random(10) * 0.1);
    rotateY(frameCount * 0.04 * random(10) * 0.1);
    float s = 0;
    float t = 0;

    PVector lastPos = new PVector(0, 0, 0);
    PVector thisPos = new PVector(0, 0, 0);

    while(t < 180){
      s += 30;
      t += 0.4;
      float radianS = radians(s);
      float radianT = radians(t);

      thisPos.x = 0 + (radius * cos(radianS) * sin(radianT)) + noise(_noiseSeed) * 100 - 50;
      thisPos.y = 0 + (radius * sin(radianS) * sin(radianT)) + noise(_noiseSeed) * 400 - 200;
      thisPos.z = 0 + (radius * cos(radianT)) + noise(_noiseSeed) * 80 - 40;

      if(lastPos.x != 0){
        drawLine(thisPos.x, thisPos.y, thisPos.z, lastPos.x, lastPos.y, lastPos.z);
      }

      _noiseSeed = _noiseSeed + 0.01;

      lastPos = thisPos.get();
    }
  popMatrix();

  // オブジェクトのサイズを縮小
  float rate = 0.99;
  oscval[n] = radius / rad * rate;
}



// オブジェクトの描画
void drawSphereBasic(int n){

  stroke(random(30)+100);

  // トリガーを取得
  float radius = oscval[n] * rad;
  translate(width/9, 0, 0);

  pushMatrix();
    rotateX(frameCount * 0.03 * random(10));
    rotateY(frameCount * 0.04 * random(10));
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

  // 画面のフラッシュ
  if(n == 1 && oscval[7] == 1){
    flash();
  }

  // オブジェクトのサイズを縮小
  float rate = 0.95;
  oscval[n] = radius / rad * rate;
}

// 線の描画
void drawLine(float x, float y, float z, float ex, float ey, float ez){
 for(int i = 5; i > 0; i--){
   stroke(240, 255 - i * 70); // gray alpha
   strokeWeight(i/3);
   line(x, y, z, ex, ey, ez);
 }
}

// 画面のフラッシュ
void flash(int n){

  if(oscval[n] == 1){
    loadPixels();
    //  パーティクルの影響範囲のピクセルについて、色の加算を行う
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int pixelIndex = x + y * width;
 
        //  ピクセルから、赤・緑・青の要素を取りだす
        int r = pixels[pixelIndex] >> 16 & 0xFF;
        int g = pixels[pixelIndex] >> 8 & 0xFF;
        int b = pixels[pixelIndex] & 0xFF;
 
        r += 255;
        g += 255;
        b += 255;
 
        //  ピクセルの色を変更する
        pixels[pixelIndex] = color(r, g, b);
      }
    }
    updatePixels();
  }

}

void flash(){

    loadPixels();
    //  パーティクルの影響範囲のピクセルについて、色の加算を行う
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int pixelIndex = x + y * width;
 
        //  ピクセルから、赤・緑・青の要素を取りだす
        int r = pixels[pixelIndex] >> 16 & 0xFF;
        int g = pixels[pixelIndex] >> 8 & 0xFF;
        int b = pixels[pixelIndex] & 0xFF;
 
        r += 255;
        g += 255;
        b += 255;
 
        //  ピクセルの色を変更する
        pixels[pixelIndex] = color(r, g, b);
      }
    }
    updatePixels();
}

// マウスドラッグ時イベント
void mouseDragged(){
  // 通信オブジェクトの初期化
  myMessage.clear();
  // prefixの指定
  myMessage.setAddrPattern("/prefix1");
  // 値の指定(下記のように続けて追加することでPureDataではlistとして扱うことができる)
  myMessage.add(mouseX);
  myMessage.add(mouseY);
  // OSCの送信
  //oscP5.send(myMessage, myRemoteLocation);
}

// マウス押下時イベント
void mousePressed(){
  flash();
}

// キー押下時イベント
void keyPressed(){

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


// OSC受信イベント
void oscEvent(OscMessage theOscMessage) {

  // prefixの取得
  String prefix = theOscMessage.addrPattern();

  // prefixが"/nseq/"の場合のみ実行
  if(prefix.equals("/nseq/")){
    // トラックNoを取得
    int index = theOscMessage.get(0).intValue();
    // トリガーを取得
    float fval = (float)theOscMessage.get(1).intValue();
    oscval[index] = fval;
  }else if(prefix.equals("/nseqv/")){ // prefixが"/nseqv/"の場合のみ実行
    float fval = (float)theOscMessage.get(0).floatValue();
    oscv = fval * 2;
  }else if(prefix.equals("/nseqp/")){ // prefixが"/nseqp/"の場合のみ実行
    float fval = (float)theOscMessage.get(0).intValue();
    oscp = fval;
  }
}

