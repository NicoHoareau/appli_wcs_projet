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
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

        final ImageView ivUserPhoto = convertView.findViewById(R.id.iv_userphoto);
        final TextView tvUsernameUser = convertView.findViewById(R.id.tv_username_user);
        ImageView ivAddPhoto = convertView.findViewById(R.id.iv_photo_added);
        TextView tvDescription = convertView.findViewById(R.id.tv_description_actuality);
        TextView tvDatePost = convertView.findViewById(R.id.tv_date_post);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateformat = sdf.format(actualityModel.getDatePost());
        tvDatePost.setText(dateformat);

        Glide.with(parent.getContext()).load(actualityModel.getUrlPhoto()).into(ivAddPhoto);

        tvDescription.setText(actualityModel.getDescription());

        tvUsernameUser.setText(actualityModel.getPseudoUser());

        Glide.with(parent.getContext()).load(actualityModel.getUrlPhotoUser()).apply(RequestOptions.circleCropTransform()).into(ivUserPhoto);

        return convertView;
    }
}
