package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

        TextView tvIdRequestValidated = convertView.findViewById(R.id.tv_id_request_validated);
        TextView tvDateRequestValidated = convertView.findViewById(R.id.tv_date_validated_request);
        TextView tvDescriptionValidated = convertView.findViewById(R.id.tv_description_validated_request);

        tvIdRequestValidated.setText(requestModel.getIdRequest());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String dateformat = sdf.format(requestModel.getDate());
        tvDateRequestValidated.setText(dateformat);
        tvDescriptionValidated.setText(requestModel.getDescription());

        return convertView;
    }
}
