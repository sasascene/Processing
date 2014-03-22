/*
  OSCによる通信のサンプル
  ローカル上での通信
  マウスドラッグ時、マウスポインタの座標を送信する
*/

import oscP5.*;
import netP5.*;
  
OscP5 oscP5;
NetAddress myRemoteLocation;

// 通信オブジェクト(引数にprefixを指定)
OscMessage myMessage = new OscMessage("/prefix1");

// 初期設定
void setup() {
  size(400,400);
  
  // procressingの受信ポート(pdの送信ポート)
  oscP5 = new OscP5(this,7702);
  
  // procressingの送信ポート(pdの受信ポート)
  myRemoteLocation = new NetAddress("127.0.0.1",7702);

}

// 描画
void draw() {

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
  oscP5.send(myMessage, myRemoteLocation);
}

// マウスリリース時イベント
void mouseReleased(){

}

// OSC受信イベント
void oscEvent(OscMessage theOscMessage) {

  // prefixの表示
  print(theOscMessage.addrPattern() + " ");
  // 値の表示
  //String val = theOscMessage.get(0).stringValue();
  String val = theOscMessage.get(0).intValue() + " " + theOscMessage.get(1).intValue();
  println(val);

}
