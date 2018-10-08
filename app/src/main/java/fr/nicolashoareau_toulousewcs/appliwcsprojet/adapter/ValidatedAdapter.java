package fr.nicolashoareau_toulousewcs.appliwcsprojet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import fr.nicolashoareau_toulousewcs.appliwcsprojet.R;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.model.RequestModel;

public class ValidatedAdapter extends ArrayAdapter<RequestModel> {

    public ValidatedAdapter(Context context, ArrayList<RequestModel> validatedList) {
        super(context, 0, validatedList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_validated_requests, parent, false);
        }
        RequestModel requestModel = getItem(position);

        //TextView tvIdRequestValidated = convertView.findViewById(R.id.tv_id_request_validated);
        TextView tvDateRequestValidated = convertView.findViewById(R.id.tv_date);
        TextView tvDescriptionValidated = convertView.findViewById(R.id.tv_description);

        //tvIdRequestValidated.setText(requestModel.getIdRequest());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateformat = sdf.format(requestModel.getDate());
        tvDateRequestValidated.setText(dateformat);
        tvDescriptionValidated.setText(requestModel.getDescription());

        return convertView;
    }
}
