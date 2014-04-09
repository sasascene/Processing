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

float _noiseSeed;

//  OSCで取得した値を格納する配列
float[] oscval;
float oscv;

float rad;

// 通信オブジェクト(引数にprefixを指定)
OscMessage myMessage = new OscMessage("/prefix1");

// 初期設定
void setup() {
  size(1000,600, OPENGL);
  smooth();

  _noiseSeed = random(10);
  
  // procressingの受信ポート(pdの送信ポート)
  oscP5 = new OscP5(this,7702);
  
  // procressingの送信ポート(pdの受信ポート)
  //myRemoteLocation = new NetAddress("127.0.0.1",7702);

  oscval = new float[8];
  for (int i = 0; i < 8; i++){
    oscval[i] = 0;
  }

  oscv = 0;

  rad = 150;

}

// 描画
void draw() {

  // 画面の初期化
  background(20);

  // 音量を球の半径にアサイン
  rad = oscv;

  pushMatrix();

    // Y方向の移動　画面中央にオブジェクトを配置
    translate(0, height/2, 0);

    // オブジェクトの描画
    for(int i = 0; i < 8; i++){
      drawNewSphere2(i);
    }
  popMatrix();

  pushMatrix();

    // Y方向の移動　画面中央にオブジェクトを配置
    translate(0, height/2, 0);

    // オブジェクトの描画
    for(int i = 0; i < 8; i++){
      drawNewSphere(i);
    }
  popMatrix();

}

// オブジェクトの描画
void drawNewSphere2(int n){

  stroke(random(20)+180, 80);

  // トリガーを取得
  float radius = oscval[n] * rad;
  translate(width/9, 0, 0);

  pushMatrix();
    rotateX(frameCount * 0.03);
    rotateY(frameCount * 0.04);
    float s = 0;
    float t = 0;

    PVector lastPos = new PVector(0, 0, 0);
    PVector lastPos2 = new PVector(0, 0, 0);
    PVector thisPos = new PVector(0, 0, 0);

    while(t < 180){
      s += noise(_noiseSeed) * 200; // 点の間隔？
      t += 2; // 螺旋の間隔
      float radianS = radians(s);
      float radianT = radians(t);

      thisPos.x = 0 + (radius * cos(radianS) * sin(radianT));
      thisPos.y = 0 + (radius * sin(radianS) * sin(radianT));
      thisPos.z = 0 + (radius * cos(radianT));

      if(lastPos.x != 0){
        //drawLine(thisPos.x, thisPos.y, thisPos.z, lastPos.x, lastPos.y, lastPos.z);
        drawLine(thisPos.x, thisPos.y, thisPos.z, lastPos2.x, lastPos2.y, lastPos2.z);
        point(thisPos.x, thisPos.y, thisPos.z);
      }

      if(t % 5 == 0){
        lastPos2 = thisPos.get(); 
      }
      lastPos = thisPos.get();

      _noiseSeed = _noiseSeed + 0.01;
    }
  popMatrix();

  // オブジェクトのサイズを縮小
  float rate = 0.99;
  oscval[n] = radius / rad * rate;
}



// オブジェクトの描画
void drawNewSphere(int n){

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
      thisPos.y = 0 + (radius * sin(radianS) * sin(radianT)) + noise(random(100));
      thisPos.z = 0 + (radius * cos(radianT));

      if(lastPos.x != 0){
        drawLine(thisPos.x, thisPos.y, thisPos.z, lastPos.x, lastPos.y, lastPos.z);
        point(thisPos.x, thisPos.y, thisPos.z); 
      }

      lastPos = thisPos.get();
    }
  popMatrix();

  // オブジェクトのサイズを縮小
  float rate = 0.95;
  oscval[n] = radius / rad * rate;
}

// 線の描画
void drawLine(float x, float y, float z, float ex, float ey, float ez){
 for(int i = 5; i > 0; i--){
   stroke(240, 255 - i * 60); // gray alpha
   strokeWeight(i/3);
   line(x, y, z, ex, ey, ez);
 }
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
  }
}
