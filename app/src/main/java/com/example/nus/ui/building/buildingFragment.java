package com.example.nus.ui.building;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nus.MainActivity_Navigation;
import com.example.nus.R;
import com.example.nus.building_information;

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

public class buildingFragment extends Fragment {
    public SearchView mSearchView;
    private ListView mListView;
    public static final String information = "";
    private String info;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        MainActivity_Navigation main = (MainActivity_Navigation)getActivity();
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mSearchView = (SearchView) root.findViewById(R.id.searchView1);
        mListView = (ListView) root.findViewById(R.id.listView1);
        mListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, main.building));
        mListView.setTextFilterEnabled(true);
        mListView.clearTextFilter();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(main.location_dic.contains(info)){
                    Intent intent = new Intent(getActivity(), building_information.class);
                    String des = info +"\n"+main.location_dic.get(info)+"\n"+main.area_dic.get(info)+"\nTel: "+main.tel_dic.get(info)+"\nFax: "+main.fax_dic.get(info);
                    intent.putExtra(information, new String[] {des, main.imageUrl_dic.get(info), main.locationUrl_dic.get(info), info});
                    startActivity(intent);
                }
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    info = newText;
                    mListView.setFilterText(newText);
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                info = (String)((TextView) view).getText();
                Intent intent = new Intent(getActivity(), building_information.class);
                String des = info +"\n"+main.location_dic.get(info)+"\n"+main.area_dic.get(info)+"\nTel: "+main.tel_dic.get(info)+"\nFax: "+main.fax_dic.get(info);
                intent.putExtra(information, new String[] {des, main.imageUrl_dic.get(info), main.locationUrl_dic.get(info), info});
                startActivity(intent);
            }
        });
        return root;
    }
}