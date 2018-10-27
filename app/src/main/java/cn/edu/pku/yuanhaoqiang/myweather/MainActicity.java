package cn.edu.pku.yuanhaoqiang.myweather;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.widget.Toast;

import cn.edu.pku.yuanhaoqiang.util.NetUtil;

public class MainActicity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE){
            Log.d("MyWeather","网络ok");
            Toast.makeText(MainActicity.this,"网络ok",Toast.LENGTH_LONG).show();
        }else
        {
            Log.d("MyWeather","网络挂了！");
            Toast.makeText(MainActicity.this,"网络挂了！",Toast.LENGTH_LONG).show();
        }
    }
}
