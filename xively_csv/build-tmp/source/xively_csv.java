import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 
import java.text.SimpleDateFormat; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class xively_csv extends PApplet {




int i;

// \u521d\u671f\u8a2d\u5b9a
public void setup(){ 
  size(1000, 400);
  frameRate(0.2f);   // 5\u79d2\u306b1\u56de\u306e\u30c7\u30fc\u30bf\u66f4\u65b0
  i = 1;
}

// \u63cf\u753b
public void draw(){

  // xively\u304b\u3089\u306e\u30c7\u30fc\u30bf\u53d6\u5f97
  String lines[] = getData();

  // \u53d6\u5f97\u3057\u305f\u30c7\u30fc\u30bf\u306e\u53ef\u8996\u5316
  showData(lines);
}

// xively\u304b\u3089\u306e\u30c7\u30fc\u30bf\u53d6\u5f97
public String[] getData(){

  // \u73fe\u5728\u306e\u65e5\u4ed8\u3092\u53d6\u5f97
  Calendar cal = Calendar.getInstance();
  i = i + 60;
  // \u6307\u5b9a\u3057\u305f\u65e5\u6570\u3092\u52a0\u7b97
  cal.add(Calendar.DATE,-9);
  // \u6307\u5b9a\u3057\u305f\u5206\u3092\u52a0\u7b97
  cal.add(Calendar.MINUTE, i);
  // \u6307\u5b9a\u3057\u305f\u65e5\u4ed8\u3092Data\u578b\u3067\u53d6\u5f97
  Date theDate = cal.getTime();

  // \u6307\u5b9a\u3057\u305f\u65e5\u4ed8\u3092ISO 8601(UTC)\u30d5\u30a9\u30fc\u30de\u30c3\u30c8\u3067\u6587\u5b57\u5217\u5316
  SimpleDateFormat dateFormat = getDateFormat();
  String startDate = dateFormat.format(theDate);
  
  // xively\u3078\u306eURL\u30ea\u30af\u30a8\u30b9\u30c8\u3092\u4f5c\u6210
  String URL = "https://api.xively.com/v2/feeds/1138710374.csv";
  String MY_KEY ="key=7DGFbpKeGma3UanSeYVdSeI47yAt87DGWOux0nYtZnwIzH5s";
  String URL_REQUEST = URL + "?start=" + startDate + "?" + MY_KEY;
  
  String[] lines = new String[0];
  try {
    // xively\u304b\u3089\u306e\u30c7\u30fc\u30bf\u53d6\u5f97
    lines = loadStrings(URL_REQUEST);
    //println("there are " + lines.length + " lines");
  }catch (Exception e) {
    // \u4f8b\u5916\u767a\u751f\u6642\u306e\u51e6\u7406
  }

  return lines;
}

// \u65e5\u4ed8\u30d5\u30a9\u30fc\u30de\u30c3\u30c8\u306e\u53d6\u5f97
public SimpleDateFormat getDateFormat(){
  // ISO 8601(UTC)\u3000\u306e\u30d5\u30a9\u30fc\u30de\u30c3\u30c8\u3092\u4f5c\u6210
  String format = "yyyy-MM-dd'T'HH:mm:00.000000'Z'";
  SimpleDateFormat sdfIso8601ExtendedFormatUtc = new SimpleDateFormat(format);
  sdfIso8601ExtendedFormatUtc.setTimeZone(TimeZone.getTimeZone("UTC"));

  return sdfIso8601ExtendedFormatUtc;
}

// \u53d6\u5f97\u3057\u305f\u30c7\u30fc\u30bf\u306e\u53ef\u8996\u5316
public void showData(String[] lines){
  
  try{
    // \u30c7\u30fc\u30bf\u3092\u53d6\u5f97\u3067\u304d\u305f\u5834\u5408\u306e\u307f\u80cc\u666f\u3092\u66f4\u65b0
    if(lines.length > 0){
      background(255);
    }

    // \u5168\u30c7\u30fc\u30bf\u3092\u53c2\u7167\u3057\u3001\u5024(\u8981\u7d202)\u306e\u307f\u3092\u53d6\u308a\u51fa\u3059
    for (int i = 0 ; i < lines.length; i++) {
      //println(lines[i]);
      String data [] = split(lines[i], ',');  // ','\u3067\u533a\u5207\u308a\u914d\u5217\u306b\u683c\u7d0d
      String theData = data[2]; // \u8981\u7d202\u304c\u5024
      // \u5024\u306e\u63cf\u753b
      line(i*10, height/2, i*10, PApplet.parseFloat(theData)*500 + height);
    }
  } catch (Exception e){
    // \u4f8b\u5916\u767a\u751f\u6642\u306e\u51e6\u7406
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "xively_csv" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
