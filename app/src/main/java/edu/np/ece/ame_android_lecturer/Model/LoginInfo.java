package edu.np.ece.ame_android_lecturer.Model;

import android.content.Context;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by MIYA on 31/08/17.
 */

public class LoginInfo {
    String username = "NULL";
    String password = "NULL";
   /* String device_hash = "NULL";*/

    public LoginInfo(String _username, String _password, Context context){
        username = _username;
        password = _password;
       /* device_hash = getMacAddr();*/
    }

    /*public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }*/


}
