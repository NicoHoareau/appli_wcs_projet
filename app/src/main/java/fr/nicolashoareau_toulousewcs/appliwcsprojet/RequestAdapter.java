package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

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

    public RequestAdapter(Context context, ArrayList<RequestModel> requestModels) {
        super(context, 0, requestModels);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final RequestModel requestModel = (RequestModel) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_fragment_request, parent, false);
        }
        TextView tvIdRequest =  convertView.findViewById(R.id.tv_id_request);
        tvIdRequest.setText(requestModel.getIdRequest());

        TextView tvDate = convertView.findViewById(R.id.tv_dateRequest);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String dateformat = sdf.format(requestModel.getDate());
        tvDate.setText(dateformat);

        TextView tvDescription = convertView.findViewById(R.id.tv_descriptionRequest);
        tvDescription.setText(requestModel.getDescription());

        mDatabase = FirebaseDatabase.getInstance();
        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ImageView logoValidate = convertView.findViewById(R.id.iv_modify_request);
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


                mRef = mDatabase.getReference("Request").child(mUid);
                mRef.orderByKey().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                            final String idRequest = requestSnapshot.child("idRequest").getValue(String.class);
                            String descriptionRequest = requestSnapshot.child("description").getValue(String.class);

                            long dateRequest = requestSnapshot.child("date").getValue(long.class);
                            Date d = new Date(dateRequest * 1000);
                            Date today = Calendar.getInstance().getTime();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                            String date = formatter.format(today);
                            tvDateRequest.setText(date);

                            tvDescriptionRequest.setText(descriptionRequest);
                            tvIdRequest.setText(idRequest);

                            Button btnModifyDescription = mView.findViewById(R.id.btn_modify_request);
                            btnModifyDescription.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    changeDescription.setVisibility(View.VISIBLE);
                                }
                            });
                            Button btnValidateModification = mView.findViewById(R.id.btn_ok_modifiy_request);
                            btnValidateModification.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final String descModified = changeDescription.getText().toString();
                                    mRef = mDatabase.getReference("Request").child(mUid).child(idRequest);
                                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            mRef.child("description").setValue(descModified);
                                            changeDescription.setHint(descModified);
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                    changeDescription.setVisibility(View.GONE);
                                }
                            });
                            Button btnValidate = mView.findViewById(R.id.btn_validate);
                            btnValidate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mRef = mDatabase.getReference("Request").child(mUid).child(idRequest);
                                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            mRef.child("validated").setValue(true);
                                            dialog.cancel();
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                dialog.show();

            }
        });
        ImageView removeRequest = convertView.findViewById(R.id.iv_remove_request);
        removeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Request").child(mUid).child(requestModel.getIdRequest());
                databaseReference.removeValue();
                Toast.makeText(getContext(), "Requête supprimée", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;


    }
}


