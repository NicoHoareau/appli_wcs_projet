package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wilder on 22/04/18.
 */

public class RequestAdapter extends ArrayAdapter<RequestModel> {

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    private String mUid;

    public RequestAdapter(Context context, int i, ArrayList<RequestModel> requestModel) {
        super(context, 0, requestModel);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final RequestModel request = getItem(position);
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

        ImageView logoValidate = convertView.findViewById(R.id.iv_validate);
        logoValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View mView = li.inflate(R.layout.check_dialog, null);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                //Contains :
                final TextView tvIdRequest = mView.findViewById(R.id.tv_id_request_dialog);
                final TextView tvDateRequest = mView.findViewById(R.id.tv_date_dialog);
                final TextView tvDescriptionRequest = mView.findViewById(R.id.tv_desc_dialog);

                final EditText changeDescription = mView.findViewById(R.id.et_description_modify);

                mDatabase = FirebaseDatabase.getInstance();
                mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                mRef = mDatabase.getReference("Request").child(mUid);
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String idRequest = dataSnapshot.child("idRequest").getValue(String.class);
                        long dateRequest = dataSnapshot.child("date").getValue(long.class);
                        String descriptionRequest = dataSnapshot.child("description").getValue(String.class);

                        tvIdRequest.setText(idRequest);

                        Date d = new Date(dateRequest * 1000);
                        Date today = Calendar.getInstance().getTime();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                        String date = formatter.format(today);
                        tvDateRequest.setText(date);

                        tvDescriptionRequest.setText(descriptionRequest);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Button btnModifyDescription = mView.findViewById(R.id.btn_modify_request);
                btnModifyDescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeDescription.setVisibility(View.VISIBLE);
                        String descModified = changeDescription.getText().toString();
                        mRef = mDatabase.getReference("Request").child(mUid);
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String descriptionHint = dataSnapshot.child("description").getValue(String.class);
                                changeDescription.setHint(descriptionHint);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                Button btnValidate = mView.findViewById(R.id.btn_validate);
                btnValidate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String newDesc = (String) changeDescription.getText().toString();
                        mRef = mDatabase.getReference("Request").child(mUid);
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                RequestModel requestModel = dataSnapshot.getValue(RequestModel.class);
                                mRef.child("description").setValue(newDesc);
                                //boolean validated in true :
                                mRef.child("validated").setValue(true);

                                dialog.cancel();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });




                dialog.show();



            }
        });

        return convertView;

    }
}
