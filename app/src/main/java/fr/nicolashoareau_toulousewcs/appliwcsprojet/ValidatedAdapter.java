package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ValidatedAdapter extends ArrayAdapter<RequestModel> {

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    private String mUid;

    public ValidatedAdapter(Context context, int i, ArrayList<RequestModel> requestModel) {
        super(context, 0, requestModel);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final RequestModel request = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_validated_request, parent, false);
        }

        TextView tvIdRequest = convertView.findViewById(R.id.tv_id_request_validated);
        TextView tvDescription = convertView.findViewById(R.id.tv_description_request_validated);
        TextView tvDate = convertView.findViewById(R.id.tv_date_request_validated);

        tvIdRequest.setText(request.getIdRequest());
        tvDescription.setText(request.getDescription());

        //créer un nouveau SimpleDateFormat et le pattern correspondant
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String dateformat = sdf.format(request.getDate());
        tvDate.setText(dateformat);

        return convertView;

    }







}
