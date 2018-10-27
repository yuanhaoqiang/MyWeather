package cn.edu.pku.yuanhaoqiang.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
    public static final int NETWORK_NONE = 0;
    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_MOBILE = 2;

    public static int getNetworkState(Context context){
        ConnectivityManager connManeger = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connManeger.getActiveNetworkInfo();
        if (networkInfo == null){
            return NETWORK_NONE;
        }

        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI){
            return NETWORK_WIFI;
        }else if (nType == ConnectivityManager.TYPE_MOBILE){
            return NETWORK_MOBILE;
        }
        return NETWORK_NONE;
    }

}
