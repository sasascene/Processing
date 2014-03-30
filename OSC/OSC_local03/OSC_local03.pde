/*
  OSCによる通信のサンプル
  ローカル上での通信
  シーケンサとの同期
*/

import oscP5.*;
import netP5.*;
  
OscP5 oscP5;
NetAddress myRemoteLocation;

//  OSCで取得した値を格納する配列
float[] oscval;

// 通信オブジェクト(引数にprefixを指定)
OscMessage myMessage = new OscMessage("/prefix1");

// 初期設定
void setup() {
  size(800,400, P3D);
  smooth();
  
  // procressingの受信ポート(pdの送信ポート)
  oscP5 = new OscP5(this,7702);
  
  // procressingの送信ポート(pdの受信ポート)
  //myRemoteLocation = new NetAddress("127.0.0.1",7702);

  oscval = new float[8];
  for (int i = 0; i < 8; i++){
    oscval[i] = 0;
  }

}

// 描画
void draw() {

  // 画面の初期化
  background(240);
  // Y方向の移動　画面中央にオブジェクトを配置
  translate(0, height/2, 0);
  
  // オブジェクトの描画
  for(int i = 4; i < 8; i++){
    drawSphere(i);
  }
}

// オブジェクトの描画
void drawSphere(int n){

  // 初期設定
  stroke(255, 50);
  translate(width/5, 0, 0);
  rotateX(mouseY * 0.05);
  sphereDetail(20);

  // トリガーを取得
  float r = oscval[n];

  // オブジェクトへアサインする
  fill(0, 0, 0, r * 60 + 20);
  sphere(r * 100);

  // オブジェクトのサイズを縮小
  float rate = 0.93;
  oscval[n] = r * rate;
}

// マウスプレス時イベント
void mousePressed() {

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

// マウスリリース時イベント
void mouseReleased(){

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
  }
}
