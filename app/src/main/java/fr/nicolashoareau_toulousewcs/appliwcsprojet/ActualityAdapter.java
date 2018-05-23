package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ActualityAdapter extends ArrayAdapter<ActualityModel> {

    public ActualityAdapter(Context context, ArrayList<ActualityModel> actualityModels) {
        super(context, 0, actualityModels);
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ActualityModel actualityModel = (ActualityModel) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_fragment_actuality, parent, false);
        }








        return convertView;
    }
}
