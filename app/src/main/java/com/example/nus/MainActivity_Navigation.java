package com.example.nus;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

public class MainActivity_Navigation extends AppCompatActivity {
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private final int ERRORCODE = 2;
    public ArrayList<String> building = new ArrayList<String>();
    public Hashtable<String, Integer> id_dic = new Hashtable<String, Integer>();
    public Hashtable <String, String> location_dic = new Hashtable<String, String>();
    public Hashtable <String, String> area_dic = new Hashtable<String, String>();
    public Hashtable <String, String> tel_dic = new Hashtable<String, String>();
    public Hashtable <String, String> fax_dic = new Hashtable<String, String>();
    public Hashtable <String, String> imageUrl_dic = new Hashtable<String, String>();
    public Hashtable <String, String> locationUrl_dic = new Hashtable<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        getinformation();
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONAnalysis(msg.obj.toString());
                    } catch (Exception e) {
                        Toast.makeText(MainActivity_Navigation.this, "Decode failed", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    }
//                    Toast.makeText(MainActivity_Navigation.this, "Data download succeed", Toast.LENGTH_SHORT)
//                            .show();
                    break;

                case FAILURE:
                    Toast.makeText(MainActivity_Navigation.this, msg.obj.toString() , Toast.LENGTH_SHORT)
                            .show();
                    break;

                default:
                    break;
            }
        };
    };

    protected void JSONAnalysis(String string) throws JSONException, Exception {
        JSONArray array = null;
        array = new JSONArray(string);
        for(int i=0;i<array.length();i++){
            JSONObject ObjectInfo = array.getJSONObject(i);
            int id = ObjectInfo.optInt("id");
            String name = ObjectInfo.optString("name");
            String location = ObjectInfo.optString("location");
            String area = ObjectInfo.optString("area");
            String tel = ObjectInfo.optString("tel");
            String fax = ObjectInfo.optString("fax");
            String imageUrl = ObjectInfo.optString("imageUrl");
            String locationUrl = ObjectInfo.optString("locationUrl");
            building.add(name);
            id_dic.put(name,id);
            location_dic.put(name,location);
            area_dic.put(name,area);
            tel_dic.put(name,tel);
            fax_dic.put(name,fax);
            imageUrl_dic.put(name,imageUrl);
            locationUrl_dic.put(name,locationUrl);
        }
    }



    protected void getinformation(){
        String path = "http://ec2-18-232-62-169.compute-1.amazonaws.com/api/v1/building";
        JSONObject json;
        new Thread(){
            public void run(){
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setConnectTimeout(5000);
                    InputStream is = conn.getInputStream();
                    String result = readMyInputStream(is);
                    Message msg = new Message();
                    msg.obj = result;
                    msg.what = SUCCESS;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    Message msg = new Message();
                    msg.what = FAILURE;
                    msg.obj = e.getMessage();
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public static String readMyInputStream(InputStream is) {
        byte[] result;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();
            baos.close();
            result = baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return "Data download failed";
        }
        return new String(result);
    }
}