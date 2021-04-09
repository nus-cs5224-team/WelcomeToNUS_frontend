package com.example.nus.ui.promotion;

import com.example.nus.ui.promotion.classUtil.ConcreteBuildingForAdapter;
import com.example.nus.ui.promotion.classUtil.ConcreteEventForAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PromotionRestUtil {
    public PromotionRestUtil() {
    }

    public void getMethod(String var1) throws IOException {
        URL var2 = new URL(var1);
        HttpURLConnection var3 = (HttpURLConnection)var2.openConnection();
        var3.setRequestMethod("GET");
        var3.setRequestProperty("Accept", "application/json");
        BufferedReader var4 = new BufferedReader(new InputStreamReader(var3.getInputStream()));

        String var5;
        while((var5 = var4.readLine()) != null) {
            System.out.println(var5);
        }

        var4.close();
    }

    public void postMethod(String var1, String var2) throws IOException {
        URL var3 = new URL(var1);
        HttpURLConnection var4 = (HttpURLConnection)var3.openConnection();
        var4.setRequestMethod("POST");
        var4.setRequestProperty("Content-Type", "application/json");
        var4.setDoOutput(true);
        PrintStream var5 = new PrintStream(var4.getOutputStream());
        var5.print(var2);
        var5.close();
        BufferedReader var6 = new BufferedReader(new InputStreamReader(var4.getInputStream()));

        String var7;
        while((var7 = var6.readLine()) != null) {
            System.out.println(var7);
        }

        var6.close();
    }

    public List<ConcreteEventForAdapter> getPromotions(String var1) throws IOException {
        URL var2 = new URL(var1);
        HttpURLConnection var3 = (HttpURLConnection)var2.openConnection();
        var3.setRequestMethod("GET");
        var3.setRequestProperty("Accept", "application/json");
        BufferedReader var4 = new BufferedReader(new InputStreamReader(var3.getInputStream()));
        List<ConcreteEventForAdapter> promotions = new ArrayList<>();

        String var5;
        while((var5 = var4.readLine()) != null) {
            System.out.println(var5);
            try {
                JSONArray jsonRoot = new JSONArray(var5);
                for(int i = 0; i < jsonRoot.length(); i++) {
                    promotions.add(parsePromotion((JSONObject) jsonRoot.get(i)));
                }
            }
            catch (Exception ex){
                System.out.println(ex);
                System.out.println("This round of parsing is ended");
            }
        }

        var4.close();

        return promotions;
    }

    public ConcreteBuildingForAdapter parseBuildingForAdapter(JSONObject jsonRoot) throws JSONException {

        int id = jsonRoot.getInt("id");
        String name = jsonRoot.optString("name");
        String location = jsonRoot.optString("location");
        String area = jsonRoot.optString("area");
        String imageUrl = jsonRoot.optString("imageUrl");
        String locationUrl = jsonRoot.optString("locationUrl");

        return new ConcreteBuildingForAdapter(id, name);
    }


    public ConcreteBuildingForAdapter getOneBuilding(String var1) throws IOException {
        URL var2 = new URL(var1);
        HttpURLConnection var3 = (HttpURLConnection)var2.openConnection();
        var3.setRequestMethod("GET");
        var3.setRequestProperty("Accept", "application/json");
        BufferedReader var4 = new BufferedReader(new InputStreamReader(var3.getInputStream()));
        ConcreteBuildingForAdapter resultBuilding = null;

        String var5;
        while((var5 = var4.readLine()) != null) {
//            System.out.println(var5);
            try {
                JSONObject jsonRoot = new JSONObject(var5);
                resultBuilding = parseBuildingForAdapter(jsonRoot);
            }
            catch (Exception ex){
                System.out.println(ex);
                System.out.println("This round of parsing is ended");
            }
        }
        var4.close();

        return resultBuilding;
    }


    public ConcreteEventForAdapter parsePromotion(JSONObject jsonRoot) throws IOException, ParseException, JSONException {

        int id = jsonRoot.getInt("id");
        int buildingId = jsonRoot.getInt("buildingId");
        String name = jsonRoot.optString("abstracts");
        String content = jsonRoot.optString("details");

//        ConcreteBuildingForAdapter building = getOneBuilding("http://ec2-18-232-62-169.compute-1.amazonaws.com/api/v1/building/" + buildingId);
        System.out.println(jsonRoot.get("building"));
        ConcreteBuildingForAdapter building = parseBuildingForAdapter((JSONObject) jsonRoot.get("building"));

//        String location = jsonRoot.optString("location");
//        String area = jsonRoot.optString("area");
//        String imageUrl = jsonRoot.optString("imageUrl");
//        String locationUrl = jsonRoot.optString("locationUrl");

        String startTime = jsonRoot.optString("starttime");
        String endTime = jsonRoot.optString("endtime");

        return new ConcreteEventForAdapter(id, name, buildingId, building, content, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(startTime));
    }

}
