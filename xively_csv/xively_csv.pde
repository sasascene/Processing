import java.util.*;
import java.text.SimpleDateFormat;

int i;

// 初期設定
void setup(){ 
  size(1000, 400);
  frameRate(0.2);   // 5秒に1回のデータ更新
  i = 1;
}

// 描画
void draw(){

  // xivelyからのデータ取得
  String lines[] = getData();

  // 取得したデータの可視化
  showData(lines);
}

// xivelyからのデータ取得
String[] getData(){

  // 現在の日付を取得
  Calendar cal = Calendar.getInstance();
  i = i + 60;
  // 指定した日数を加算
  cal.add(Calendar.DATE,-9);
  // 指定した分を加算
  cal.add(Calendar.MINUTE, i);
  // 指定した日付をData型で取得
  Date theDate = cal.getTime();

  // 指定した日付をISO 8601(UTC)フォーマットで文字列化
  SimpleDateFormat dateFormat = getDateFormat();
  String startDate = dateFormat.format(theDate);
  
  // xivelyへのURLリクエストを作成
  String URL = "https://api.xively.com/v2/feeds/1138710374.csv";
  String MY_KEY ="key=7DGFbpKeGma3UanSeYVdSeI47yAt87DGWOux0nYtZnwIzH5s";
  String URL_REQUEST = URL + "?start=" + startDate + "?" + MY_KEY;
  
  String[] lines = new String[0];
  try {
    // xivelyからのデータ取得
    lines = loadStrings(URL_REQUEST);
    //println("there are " + lines.length + " lines");
  }catch (Exception e) {
    // 例外発生時の処理
  }

  return lines;
}

// 日付フォーマットの取得
SimpleDateFormat getDateFormat(){
  // ISO 8601(UTC)　のフォーマットを作成
  String format = "yyyy-MM-dd'T'HH:mm:00.000000'Z'";
  SimpleDateFormat sdfIso8601ExtendedFormatUtc = new SimpleDateFormat(format);
  sdfIso8601ExtendedFormatUtc.setTimeZone(TimeZone.getTimeZone("UTC"));

  return sdfIso8601ExtendedFormatUtc;
}

// 取得したデータの可視化
void showData(String[] lines){
  
  try{
    // データを取得できた場合のみ背景を更新
    if(lines.length > 0){
      background(255);
    }

    // 全データを参照し、値(要素2)のみを取り出す
    for (int i = 0 ; i < lines.length; i++) {
      //println(lines[i]);
      String data [] = split(lines[i], ',');  // ','で区切り配列に格納
      String theData = data[2]; // 要素2が値
      // 値の描画
      line(i*10, height/2, i*10, float(theData)*500 + height);
    }
  } catch (Exception e){
    // 例外発生時の処理
  }
}

