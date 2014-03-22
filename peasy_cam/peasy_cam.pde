/*
  3D視点コントロール用ライブラリ peasy のサンプル
  ドラッグ：回転
  スクロール：Z軸方向の移動
*/

import peasy.*; // 視点コントロール用ライブラリ

PeasyCam cam; // カメラ

//　初期設定
void setup() {

  size(500,500,P3D);

  // カメラオブジェクトの生成
  cam = new PeasyCam(this, 100);
  cam.setMinimumDistance(100);  // 最小距離の指定
  cam.setMaximumDistance(500);  // 最大距離の指定
  
  smooth();
  strokeWeight(0.5);
}

// 描画
void draw() {
  
  noFill(); // 塗りつぶしなし
  stroke(100,100,100);  // 線色
  background(255);  // 背景描画
  sphere(30); // 球体の描画
}
