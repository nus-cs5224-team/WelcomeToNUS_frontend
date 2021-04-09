package com.example.nus.ui.promotion;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nus.R;
import com.example.nus.ui.promotion.classUtil.ConcreteEventForAdapter;

import java.util.List;

public class PromotionArrayAdapter extends ArrayAdapter<ConcreteEventForAdapter>{

    public PromotionArrayAdapter(@NonNull Context context, @NonNull List<ConcreteEventForAdapter> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.promotion_item, parent, false);
        }

        ConcreteEventForAdapter currentEvent = getItem(position);
        TextView placeTextView = (TextView) listItemView.findViewById(R.id.promotion_place_text);
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.promotion_name_text);
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.promotion_time_text);
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.promotion_image);

        placeTextView.setText(currentEvent.building.name);
        nameTextView.setText(currentEvent.name);
        timeTextView.setText(currentEvent.time.toString());


        return listItemView;
    }


}
