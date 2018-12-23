package com.job.darasalecturer.ui.settings;


import android.app.Dialog;
import android.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.job.darasalecturer.R;
import com.job.darasalecturer.model.LecTeach;
import com.job.darasalecturer.util.UnitsViewHolder;
import com.job.darasalecturer.viewmodel.UnitsViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.job.darasalecturer.util.Constants.LECTEACHCOL;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassAttendanceListFragment extends AppCompatDialogFragment {


    public static final String TAG = "attendclasslist";

    @BindView(R.id.frg_class_list)
    RecyclerView frgClassList;
    @BindView(R.id.frg_class_no_class)
    View noStudView;
    @BindView(R.id.frg_class_delete)
    MaterialButton frgClassDelete;
    @BindView(R.id.frg_class_dismiss)
    MaterialButton frgClassDismiss;

    Unbinder unbinder;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private FirestoreRecyclerAdapter adapter;
    private UnitsViewModel unitsViewModel;

    public ClassAttendanceListFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.getWindow().requestFeature(1);
        dialog.getWindow().setFlags(32, 1024);
        dialog.setContentView(R.layout.fragment_settings_class_list);
        dialog.getWindow().setLayout(-1, -1);
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_class_list, container, false);
        unbinder = ButterKnife.bind(this, dialog);

        //init
        init();
        //init the list
        loadUpList();
        return dialog;
    }

    private void init() {
        //firebase
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //viewmodel
        unitsViewModel = ViewModelProviders.of(this).get(UnitsViewModel.class);

        //observers
        selectedUnitsObserver();
    }

    private void selectedUnitsObserver() {
        unitsViewModel.getLecTeachList()
                .observe(getActivity(), new Observer<List<LecTeach>>() {
                    @Override
                    public void onChanged(@Nullable List<LecTeach> lecTeaches) {
                        StringBuilder stringBuilder = new StringBuilder();
                        String s = "Selected : ";
                        stringBuilder.append(s);

                        for (LecTeach lecTeach : lecTeaches) {
                            stringBuilder.append(lecTeach.getUnitname() + ", ");
                        }

                    }
                });
    }


    private void loadUpList() {
        initList();

        String lecid = mAuth.getCurrentUser().getUid();

        Query query = mFirestore.collection(LECTEACHCOL)
                .whereEqualTo("lecid", lecid)
                .orderBy("semester", Query.Direction.ASCENDING)
                .orderBy("studyyear", Query.Direction.ASCENDING);


        FirestoreRecyclerOptions<LecTeach> options = new FirestoreRecyclerOptions.Builder<LecTeach>()
                .setQuery(query, LecTeach.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<LecTeach, UnitsViewHolder>(options) {

            @NonNull
            @Override
            public UnitsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_class, parent, false);

                return new UnitsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final UnitsViewHolder holder, int position, @NonNull LecTeach model) {

                holder.init(getActivity(), model, unitsViewModel);
                holder.setUpUi(model);
            }


            @Override
            public void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();

                Log.d(TAG, "onError: ", e);
            }

            @Override
            public void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    frgClassList.setVisibility(View.INVISIBLE);
                    noStudView.setVisibility(View.VISIBLE);
                } else {
                    frgClassList.setVisibility(View.VISIBLE);
                    noStudView.setVisibility(View.INVISIBLE);
                }
            }

        };


        adapter.startListening();
        adapter.notifyDataSetChanged();
        frgClassList.setAdapter(adapter);
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        frgClassList.setLayoutManager(linearLayoutManager);
        frgClassList.setHasFixedSize(true);
        frgClassList.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onStart() {

        if (mAuth.getCurrentUser() == null) {
            Log.e(TAG, "onStart: mAuth.getCurrentUser() = null");
        }

        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.frg_class_delete)
    public void onFrgClassDeleteClicked() {
        if ( unitsViewModel.getLecTeachList().getValue() == null || unitsViewModel.getLecTeachList().getValue().isEmpty()){
            Toast.makeText(getContext(), "Select a unit", Toast.LENGTH_SHORT).show();
            return;
        }
        if ( unitsViewModel.getLecTeachList().getValue().size() > 1){
            Toast.makeText(getContext(), "Select only one unit", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @OnClick(R.id.frg_class_dismiss)
    public void onFrgClassDismissClicked() {
        dismiss();
    }

}
