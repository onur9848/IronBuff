package com.senerunosoft.ironbuff.MainMenuFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.table.FoodTable;

import java.util.ArrayList;
import java.util.List;

public class AddMealListAdapter extends ArrayAdapter<FoodTable> {

    private LayoutInflater inflater;
    private ArrayList<FoodTable> foodTables;
    private ViewHolder holder;

    public AddMealListAdapter(@NonNull Context context, ArrayList<FoodTable> foodTables) {
        super(context, 0);
        this.inflater = LayoutInflater.from(context);
        this.foodTables = foodTables;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.meal_in_listview, null);

            holder = new ViewHolder();
            holder.foodNameText = (TextView) convertView.findViewById(R.id.listview_food_name);
            holder.foodGramText = (TextView) convertView.findViewById(R.id.listview_food_gram);
            holder.foodCalorieText = (TextView) convertView.findViewById(R.id.listview_food_kalori);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (foodTables !=null){
            FoodTable food = foodTables.get(position);
            holder.foodNameText.setText(food.getFoodName());
            holder.foodGramText.setText(food.getFoodGram()+"g");
            holder.foodCalorieText.setText(food.getFoodCalorie()+" Kcal");
        }
        return convertView;
    }

    @Nullable
    @Override
    public FoodTable getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return foodTables.size();
    }

    public static class ViewHolder {
        TextView foodNameText, foodGramText, foodCalorieText;
    }
}
