package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by wilder on 22/04/18.
 */

public class RequestAdapter  extends ArrayAdapter<RequestModel> {

    public RequestAdapter(Context context, int i, ArrayList<RequestModel> requestModel) {
        super(context, 0, requestModel);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RequestModel request = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_fragment_request, parent, false);
        }
        TextView tvCodeRequest = convertView.findViewById(R.id.tv_id_request);
        TextView tvDescription = convertView.findViewById(R.id.tv_description_request);
        TextView tvDate = convertView.findViewById(R.id.tv_date_request);

        tvCodeRequest.setText(request.getIdRequest());
        tvDescription.setText(request.getDescription());

        //créer un nouveau SimpleDateFormat et le pattern correspondant
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String dateformat = sdf.format(request.getDate());
        tvDate.setText(dateformat);

        return convertView;
    }


}
