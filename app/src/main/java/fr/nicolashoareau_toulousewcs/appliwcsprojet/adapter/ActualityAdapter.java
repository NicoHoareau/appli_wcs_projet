package fr.nicolashoareau_toulousewcs.appliwcsprojet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import fr.nicolashoareau_toulousewcs.appliwcsprojet.model.ActualityModel;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.R;

public class ActualityAdapter extends ArrayAdapter<ActualityModel> {

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    private String mUid;

    public ActualityAdapter(Context context, ArrayList<ActualityModel> actualityModels) {
        super(context, 0, actualityModels);
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ActualityModel actualityModel = (ActualityModel) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_fragment_actuality, parent, false);
        }

        final ImageView ivUserPhoto = convertView.findViewById(R.id.iv_user_pix);
        final TextView tvUsernameUser = convertView.findViewById(R.id.tv_username);
        ImageView ivAddPhoto = convertView.findViewById(R.id.iv_photo_added);
        TextView tvDescription = convertView.findViewById(R.id.tv_description_actuality);
        TextView tvDatePost = convertView.findViewById(R.id.tv_date);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateformat = sdf.format(actualityModel.getDatePost());
        tvDatePost.setText(dateformat);

        Glide.with(parent.getContext()).load(actualityModel.getUrlPhoto()).apply(RequestOptions.centerCropTransform()).into(ivAddPhoto);

        tvDescription.setText(actualityModel.getDescription());

        tvUsernameUser.setText(actualityModel.getPseudoUser());

        Glide.with(parent.getContext()).load(actualityModel.getUrlPhotoUser()).apply(RequestOptions.circleCropTransform()).into(ivUserPhoto);
        if (actualityModel.getUrlPhotoUser() == null || actualityModel.getUrlPhotoUser().isEmpty()) {
            Glide.with(getContext()).load(R.drawable.logo_user2).apply(RequestOptions.centerCropTransform()).into(ivUserPhoto);
        }


        return convertView;
    }
}
