package cn.edu.pku.yuanhaoqiang.myweather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.edu.pku.yuanhaoqiang.util.NetUtil;

public class MainActicity extends Activity implements View.OnClickListener {
    private ImageView mUpdateBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE){
            Log.d("MyWeather","网络ok");
            Toast.makeText(MainActicity.this,"网络ok",Toast.LENGTH_LONG).show();
        }else
        {
            Log.d("MyWeather","网络挂了！");
            Toast.makeText(MainActicity.this,"网络挂了！",Toast.LENGTH_LONG).show();
        }
    }

    public void onClick(View view){
        if (view.getId()==R.id.title_update_btn){
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101010100");
            Log.d("myWeather", cityCode);
            queryWeatherCode(cityCode);
        }

        if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE){
            Log.d("MyWeather","网络ok");
            Toast.makeText(MainActicity.this,"网络ok",Toast.LENGTH_LONG).show();
        }else
        {
            Log.d("MyWeather","网络挂了！");
            Toast.makeText(MainActicity.this,"网络挂了！",Toast.LENGTH_LONG).show();
        }
    }

    //解析城市名称，更新时间信息
    private void parseXML(String xmldata){
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try{
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int evenType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (evenType != XmlPullParser.END_DOCUMENT){
                switch (evenType){
                    //start_document
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //start_tag
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("city")){
                            evenType = xmlPullParser.next();
                            Log.d("myWeather", "city:   "+xmlPullParser.getText());

                        }else if(xmlPullParser.getName().equals("updatetime")){
                            evenType = xmlPullParser.next();
                            Log.d("myWeather","updatetime:  "+xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("wendu")){
                            evenType = xmlPullParser.next();
                            Log.d("myWeather","wendu:  "+xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("fengli")&&fengliCount ==0){
                            evenType = xmlPullParser.next();
                            Log.d("myWeather","fengli:  "+xmlPullParser.getText());
                            fengliCount++;
                        }else if(xmlPullParser.getName().equals("shidu")){
                            evenType = xmlPullParser.next();
                            Log.d("myWeather","shidu:  "+xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("fengxiang")&&fengxiangCount == 0){
                            evenType = xmlPullParser.next();
                            Log.d("myWeather","fengxiang:  "+xmlPullParser.getText());
                            fengxiangCount++;
                        }else if(xmlPullParser.getName().equals("sunrise_1")){
                            evenType = xmlPullParser.next();
                            Log.d("myWeather","sunrise_1:  "+xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("sunset_1")){
                            evenType = xmlPullParser.next();
                            Log.d("myWeather","sunset_1:  "+xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("pm25")){
                            evenType = xmlPullParser.next();
                            Log.d("myWeather","pm25:  "+xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("quality")){
                            evenType = xmlPullParser.next();
                            Log.d("myWeather","quality:  "+xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("date") && dateCount == 0){
                            evenType = xmlPullParser.next();
                            Log.d("myWeather","date:  "+xmlPullParser.getText());
                            dateCount++;
                        }else if(xmlPullParser.getName().equals("high") && highCount == 0){
                            evenType = xmlPullParser.next();
                            Log.d("myWeather","high:  "+xmlPullParser.getText());
                            highCount++;
                        }else if(xmlPullParser.getName().equals("low") && lowCount == 0){
                            evenType = xmlPullParser.next();
                            Log.d("myWeather","low:  "+xmlPullParser.getText());
                            lowCount++;
                        }else if(xmlPullParser.getName().equals("type") && typeCount == 0){
                            evenType = xmlPullParser.next();
                            Log.d("myWeather","type:  "+xmlPullParser.getText());
                            typeCount++;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                //
                evenType = xmlPullParser.next();
            }
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //根据城市代码，访问网址，得到数据；调用解析函数，得到天气信息
    private void queryWeatherCode(String cityCode){
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try{
                    URL url = new URL(address);
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str=reader.readLine())!=null){
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather", responseStr);
                    //调用解析
                    parseXML(responseStr);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(con != null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }
}
