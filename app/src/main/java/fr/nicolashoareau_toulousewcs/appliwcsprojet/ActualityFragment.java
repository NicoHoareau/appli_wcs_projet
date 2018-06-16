package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActualityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActualityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActualityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ActualityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActualityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActualityFragment newInstance(String param1, String param2) {
        ActualityFragment fragment = new ActualityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_actuality, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    FirebaseDatabase mDatabase;
    DatabaseReference mActualityRef;
    private String mUid;
    FloatingActionButton mBtnFloat;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance();

        final ListView listActuality = getView().findViewById(R.id.list_actuality);
        final ArrayList<ActualityModel> actualityModelArrayList = new ArrayList<>();
        final ActualityAdapter adapter = new ActualityAdapter(getContext(), actualityModelArrayList);
        listActuality.setAdapter(adapter);

        mBtnFloat = view.findViewById(R.id.btn_float_actuality);
        mBtnFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToCreatePost = new Intent(getContext(), CreatePostActivity.class);
                startActivity(goToCreatePost);
            }
        });

        mActualityRef = mDatabase.getReference("Post");
        mActualityRef.orderByChild("datePost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                actualityModelArrayList.clear();
                for (DataSnapshot listActualitySnapshot : dataSnapshot.getChildren()){
                    ActualityModel actualityModel = listActualitySnapshot.getValue(ActualityModel.class);
                    actualityModelArrayList.add(actualityModel);

                }
                adapter.notifyDataSetChanged();
                Collections.reverse(actualityModelArrayList);
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });




    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
