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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import fr.nicolashoareau_toulousewcs.appliwcsprojet.R;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.model.UserModel;

public class WildersAdapter extends ArrayAdapter<UserModel>{


    public WildersAdapter( Context context, List<UserModel> objects) {
        super(context, 0, objects);
    }

    FirebaseDatabase mDatabase;
    DatabaseReference mWilderRef;
    private String mUid;

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_wilders, parent, false);
        }
        final UserModel userModel = (UserModel) getItem(position);

        final ImageView ivWilderPicture = convertView.findViewById(R.id.iv_userpic);
        TextView tvWilderPseudo = convertView.findViewById(R.id.tv_pseudo_wilder);
        TextView tvWilderPromo = convertView.findViewById(R.id.tv_promo_wilder);
        final ImageView ivLogoPromo = convertView.findViewById(R.id.iv_wilder_promo);
        Glide.with(getContext()).load(userModel.getProfilPic()).apply(RequestOptions.circleCropTransform()).into(ivWilderPicture);

        tvWilderPseudo.setText(userModel.getPseudo());
        tvWilderPromo.setText(userModel.getPromo());

        if (userModel.getLanguage().equals("Java")){
            Glide.with(getContext()).load(R.drawable.java_logo).apply(
                    RequestOptions.circleCropTransform()).into(ivLogoPromo);
        }
        if (userModel.getLanguage().equals("Javascript")){
            Glide.with(getContext()).load(R.drawable.js_logo).apply(
                    RequestOptions.circleCropTransform()).into(ivLogoPromo);
        }

        if (userModel.getProfilPic() == null || userModel.getProfilPic().isEmpty()) {
            Glide.with(getContext()).load(R.drawable.logo_user2).apply(RequestOptions.centerCropTransform()).into(ivWilderPicture);
        }


        return convertView;

    }
}
