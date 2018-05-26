package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class WildersAdapter extends ArrayAdapter<WildersModel>{


    public WildersAdapter( Context context, List<WildersModel> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_wilders, parent, false);
        }
        WildersModel wildersModel = (WildersModel) getItem(position);





        return convertView;

    }
}
