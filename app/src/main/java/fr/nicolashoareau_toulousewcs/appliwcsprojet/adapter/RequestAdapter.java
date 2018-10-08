package fr.nicolashoareau_toulousewcs.appliwcsprojet.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fr.nicolashoareau_toulousewcs.appliwcsprojet.R;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.model.RequestModel;

/**
 * Created by wilder on 22/04/18.
 */

public class RequestAdapter extends ArrayAdapter<RequestModel> {
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    public RequestAdapter(Context context, ArrayList<RequestModel> requestModels) {
        super(context, 0, requestModels);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final RequestModel requestModel = (RequestModel) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_fragment_request, parent, false);
        }

        TextView tvDate = convertView.findViewById(R.id.tv_date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateformat = sdf.format(requestModel.getDate());
        tvDate.setText(dateformat);

        final TextView tvDescription = convertView.findViewById(R.id.tv_description);
        tvDescription.setText(requestModel.getDescription());


        ImageView btnRqOK = convertView.findViewById(R.id.iv_accept);
        final ImageView btnEdit = convertView.findViewById(R.id.iv_edit);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Request");


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view = inflater.inflate(R.layout.check_dialog, null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();

                final TextView tvDateRq = view.findViewById(R.id.tv_date);
                final TextView tvDescRq = view.findViewById(R.id.tv_desc);

                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot rqSnap : dataSnapshot.getChildren()) {

                            final String descRq = requestModel.getDescription();
                            tvDescRq.setText(descRq);
                            Date today = Calendar.getInstance().getTime();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            String date = dateFormat.format(today);
                            tvDateRq.setText(date);

                            final ImageView editRq = view.findViewById(R.id.iv_edit_req);
                            final EditText etEditDesc = view.findViewById(R.id.et_desc);
                            editRq.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tvDescRq.setVisibility(View.GONE);
                                    etEditDesc.setVisibility(View.VISIBLE);
                                    etEditDesc.setText(descRq);
                                    editRq.setImageResource(R.drawable.accept);
                                    editRq.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final String newDesc = etEditDesc.getText().toString();
                                            final DatabaseReference rqRef = mRef.child(requestModel.getIdRequest());
                                            rqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    rqRef.child("description").setValue(newDesc);
                                                    etEditDesc.setText(newDesc);
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });
                                            etEditDesc.setVisibility(View.INVISIBLE);
                                            dialog.cancel();
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

        btnRqOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            mRef.child(requestModel.getIdRequest()).child("validated").setValue(true);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        ImageView removeRequest = convertView.findViewById(R.id.iv_remove);
        removeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Request").child(requestModel.getIdRequest());
                databaseReference.removeValue();
                Toast.makeText(getContext(), "Requête supprimée", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;


    }
}


