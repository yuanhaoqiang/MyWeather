package cn.edu.pku.yuanhaoqiang.myweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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

import cn.edu.pku.yuanhaoqiang.cn.edu.pku.yuanhaoqiang.bean.TodayWeather;
import cn.edu.pku.yuanhaoqiang.util.NetUtil;

public class MainActicity extends Activity implements View.OnClickListener {
    private static final int UPDATE_TODAY_WEATHER = 1;
    private ImageView mUpdateBtn;
    private ImageView mCitySelect;
    private TextView cityTv,timeTv,humidityTv,weekTv, pmDataTv, pmQualityTv, temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView weatherImg, pmImg;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
            Log.d("MyWeather", "网络ok");
            Toast.makeText(MainActicity.this, "网络ok", Toast.LENGTH_LONG).show();
        } else {
            Log.d("MyWeather", "网络挂了！");
            Toast.makeText(MainActicity.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }

        mCitySelect = (ImageView)findViewById(R.id.city_manager);
        mCitySelect.setOnClickListener(this);

        initView();

    }

    void initView() {
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm2_5);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);

        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
 /*           */
    }

    //更新今日天气界面
    void updateTodayWeather(TodayWeather todayWeather){
        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime()+ "发布");
        humidityTv.setText("湿度："+todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:"+todayWeather.getFengli());

        //天气图片
        if(todayWeather.getType().equals("晴")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
        }else if(todayWeather.getType().equals("多云")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        }else if(todayWeather.getType().equals("暴雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        }else if(todayWeather.getType().equals("暴雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        }else if(todayWeather.getType().equals("大暴雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        }else if(todayWeather.getType().equals("大雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
        }else if(todayWeather.getType().equals("大雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
        }else if(todayWeather.getType().equals("雷阵雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        }else if(todayWeather.getType().equals("雷阵雨冰雹")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        }else if(todayWeather.getType().equals("沙尘暴")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        }else if(todayWeather.getType().equals("特大暴雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        }else if(todayWeather.getType().equals("雾")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
        }else if(todayWeather.getType().equals("小雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        }else if(todayWeather.getType().equals("小雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        }else if(todayWeather.getType().equals("阴")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
        }else if(todayWeather.getType().equals("雨夹雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        }else if(todayWeather.getType().equals("阵雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        }else if(todayWeather.getType().equals("阵雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        }else if(todayWeather.getType().equals("中雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        }else if(todayWeather.getType().equals("中雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        }
        //空气质量图标,不是按照pm2.5来的
  /*     if ((Integer.parseInt(todayWeather.getPm25())<51)&&(Integer.parseInt(todayWeather.getPm25())>=0)){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
        }else if ((Integer.parseInt(todayWeather.getPm25())<101)&&(Integer.parseInt(todayWeather.getPm25())>50)){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
        }else if ((Integer.parseInt(todayWeather.getPm25())<151)&&(Integer.parseInt(todayWeather.getPm25())>100)){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
        }else if ((Integer.parseInt(todayWeather.getPm25())<201)&&(Integer.parseInt(todayWeather.getPm25())>150)){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
        }else if ((Integer.parseInt(todayWeather.getPm25())<301)&&(Integer.parseInt(todayWeather.getPm25())>200)){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
        }else if (Integer.parseInt(todayWeather.getPm25())>300){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
        }
*/
        if (todayWeather.getQuality().equals("优")){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
        }else if (todayWeather.getQuality().equals("良")){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
        }else if (todayWeather.getQuality().equals("轻度污染")){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
        }else if (todayWeather.getQuality().equals("中毒污染")){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
        }else if (todayWeather.getQuality().equals("中毒污染")){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
        }else if (todayWeather.getQuality().equals("重度污染")){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
        }

        Toast.makeText(MainActicity.this,"更新成功！",Toast.LENGTH_SHORT).show();
    }


    public void onClick(View view){
        if (view.getId() == R.id.city_manager){
            Intent i = new Intent(this, SelectCity.class);
            //startActivity(i);
            startActivityForResult(i, 1);

        }

        if (view.getId()==R.id.title_update_btn){
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101180101");
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode= data.getStringExtra("cityCode");
            Log.d("myWeather", "选择的城市代码为"+newCityCode);

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(newCityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActicity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }
    }


    //解析函数，包括城市名称，更新时间信息等
  //
    private TodayWeather parseXML(String xmldata){
        TodayWeather todayWeather = null;
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
                        if(xmlPullParser.getName().equals("resp")){
                            todayWeather = new TodayWeather();
                        }
                        if(todayWeather != null){
                            if(xmlPullParser.getName().equals("city")){
                                evenType = xmlPullParser.next();
                                Log.d("myWeather", "city:   "+xmlPullParser.getText());
                                todayWeather.setCity(xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("updatetime")){
                                evenType = xmlPullParser.next();
                                Log.d("myWeather","updatetime:  "+xmlPullParser.getText());
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("wendu")){
                                evenType = xmlPullParser.next();
                                Log.d("myWeather","wendu:  "+xmlPullParser.getText());
                                todayWeather.setWendu(xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("fengli")&&fengliCount ==0){
                                evenType = xmlPullParser.next();
                                Log.d("myWeather","fengli:  "+xmlPullParser.getText());
                                fengliCount++;
                                todayWeather.setFengli(xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("shidu")){
                                evenType = xmlPullParser.next();
                                Log.d("myWeather","shidu:  "+xmlPullParser.getText());
                                todayWeather.setShidu(xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("fengxiang")&&fengxiangCount == 0){
                                evenType = xmlPullParser.next();
                                Log.d("myWeather","fengxiang:  "+xmlPullParser.getText());
                                fengxiangCount++;
                                todayWeather.setFengxiang(xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("sunrise_1")){
                                evenType = xmlPullParser.next();
                                Log.d("myWeather","sunrise_1:  "+xmlPullParser.getText());

                            }else if(xmlPullParser.getName().equals("sunset_1")){
                                evenType = xmlPullParser.next();
                                Log.d("myWeather","sunset_1:  "+xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("pm25")){
                                evenType = xmlPullParser.next();
                                Log.d("myWeather","pm25:  "+xmlPullParser.getText());
                                todayWeather.setPm25(xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("quality")){
                                evenType = xmlPullParser.next();
                                Log.d("myWeather","quality:  "+xmlPullParser.getText());
                                todayWeather.setQuality(xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("date") && dateCount == 0){
                                evenType = xmlPullParser.next();
                                Log.d("myWeather","date:  "+xmlPullParser.getText());
                                dateCount++;
                                todayWeather.setDate(xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("high") && highCount == 0){
                                evenType = xmlPullParser.next();
                                Log.d("myWeather","high:  "+xmlPullParser.getText());
                                highCount++;
                                //substring(2)从第2个字开始，trim()去掉了前面的空格
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                            }else if(xmlPullParser.getName().equals("low") && lowCount == 0){
                                evenType = xmlPullParser.next();
                                Log.d("myWeather","low:  "+xmlPullParser.getText());
                                lowCount++;
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                            }else if(xmlPullParser.getName().equals("type") && typeCount == 0){
                                evenType = xmlPullParser.next();
                                Log.d("myWeather","type:  "+xmlPullParser.getText());
                                typeCount++;
                                todayWeather.setType(xmlPullParser.getText());
                            }
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
        return todayWeather;
    }

    //根据城市代码，访问网址，得到数据；调用解析函数，得到天气信息
    private void queryWeatherCode(String cityCode){
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                TodayWeather todayWeather = null;

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

                    //调用解析, 返回todayWeather
                    todayWeather = parseXML(responseStr);
                    if(todayWeather != null){
                        Log.d("myWeather", todayWeather.toString());
                    }

                    Message msg =new Message();
                    msg.what = UPDATE_TODAY_WEATHER;
                    msg.obj=todayWeather;
                    mHandler.sendMessage(msg);
                 //   mHandler.handleMessage(msg);  //为什么是在handle中更新的却在send中使用？使用hendle却不行
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
