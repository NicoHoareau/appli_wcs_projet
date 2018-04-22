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

    public RequestAdapter(Context context, int i, ArrayList<RequestModel> requestModels) {
        super(context, 0, requestModels);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RequestModel request = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_fragment_request, parent, false);
        }
        TextView tvRequest = convertView.findViewById(R.id.tv_request);
        TextView tvDescription = convertView.findViewById(R.id.tv_description_request);

        tvRequest.setText(request.getRequest());
        tvDescription.setText(request.getDescription());

        return convertView;
    }


}
