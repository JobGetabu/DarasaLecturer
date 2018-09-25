package com.job.darasalecturer.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.job.darasalecturer.R;
import com.job.darasalecturer.util.AppStatus;
import com.job.darasalecturer.util.DoSnack;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.job.darasalecturer.util.Constants.LECUSERCOL;

public class AccountSetupActivity extends AppCompatActivity {

    @BindView(R.id.setup_toolbar)
    Toolbar setupToolbar;
    @BindView(R.id.setup_firstname)
    TextInputLayout setupFirstname;
    @BindView(R.id.setup_lastname)
    TextInputLayout setupLastname;
    @BindView(R.id.setup_school)
    TextInputLayout setupSchool;
    @BindView(R.id.setup_department)
    TextInputLayout setupDepartment;
    @BindView(R.id.setup_btn)
    TextView setupBtn;

    private DoSnack doSnack;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);
        ButterKnife.bind(this);

        setSupportActionBar(setupToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        doSnack = new DoSnack(this, AccountSetupActivity.this);
    }

    @OnClick(R.id.setup_btn)
    public void onViewClicked() {

        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {

            doSnack.showSnackbar("You're offline", "Retry", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onViewClicked();
                }
            });

            return;
        }

        if (validate()) {

            final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));
            pDialog.setTitleText("Just a moment...");
            pDialog.setCancelable(false);
            pDialog.show();

            String fname = setupFirstname.getEditText().getText().toString();
            String lname = setupLastname.getEditText().getText().toString();
            String school = setupSchool.getEditText().getText().toString();
            String dept = setupDepartment.getEditText().getText().toString();

            Map<String, Object> lecMap = new HashMap<>();
            lecMap.put("firstname", fname);
            lecMap.put("lastname", lname);
            lecMap.put("school", school);
            lecMap.put("department", dept);

            // Set the value of 'Users'
            DocumentReference usersRef = mFirestore.collection(LECUSERCOL).document(mAuth.getCurrentUser().getUid());

            usersRef.update(lecMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pDialog.dismissWithAnimation();
                            sendToSetClass();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pDialog.dismiss();
                    doSnack.errorPrompt("Oops...", e.getMessage());
                }
            });
        }
    }

    private void sendToSetClass() {

        Intent aIntent = new Intent(this, AddClassActivity.class);
        aIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(aIntent);
        finish();
    }

    private boolean validate() {
        boolean valid = true;

        String fname = setupFirstname.getEditText().getText().toString();
        String lname = setupLastname.getEditText().getText().toString();
        String school = setupSchool.getEditText().getText().toString();
        String dept = setupDepartment.getEditText().getText().toString();

        if (fname.isEmpty()) {
            setupFirstname.setError("enter name");
            valid = false;
        } else {
            setupFirstname.setError(null);
        }

        if (lname.isEmpty()) {
            setupLastname.setError("enter name");
            valid = false;
        } else {
            setupLastname.setError(null);
        }

        if (school.isEmpty()) {
            setupSchool.setError("enter school");
            valid = false;
        } else {
            setupSchool.setError(null);
        }

        if (dept.isEmpty()) {
            setupDepartment.setError("enter dept");
            valid = false;
        } else {
            setupDepartment.setError(null);
        }

        return valid;
    }
}
