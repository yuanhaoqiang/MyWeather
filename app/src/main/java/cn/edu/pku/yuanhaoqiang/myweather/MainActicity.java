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
