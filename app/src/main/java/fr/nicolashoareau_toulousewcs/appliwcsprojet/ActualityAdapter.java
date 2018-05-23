package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

        ImageView ivUserPhoto = convertView.findViewById(R.id.iv_userphoto);
        TextView tvUsernameUser = convertView.findViewById(R.id.tv_username_user);
        ImageView ivAddPhoto = convertView.findViewById(R.id.iv_photo_added);
        EditText etDescriptionPhoto = convertView.findViewById(R.id.tv_description_actuality);

        Glide.with(parent.getContext()).load(actualityModel.getUrlPhotoUser()).into(ivUserPhoto);
        Glide.with(parent.getContext()).load(actualityModel.getUrlPhoto()).into(ivAddPhoto);
        tvUsernameUser.setText(actualityModel.getUsername());
        etDescriptionPhoto.setText(actualityModel.getDescription());

        return convertView;
    }
}
