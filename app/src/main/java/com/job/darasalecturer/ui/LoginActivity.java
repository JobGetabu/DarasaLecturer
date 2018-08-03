package com.job.darasalecturer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.job.darasalecturer.R;
import com.job.darasalecturer.util.AppStatus;
import com.job.darasalecturer.util.DoSnack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_toolbar)
    Toolbar loginToolbar;
    @BindView(R.id.login_input_email)
    TextInputLayout loginInputEmail;
    @BindView(R.id.login_input_password)
    TextInputLayout loginInputPassword;
    @BindView(R.id.login_btn)
    TextView loginBtn;

    private static final String TAG = "login";


    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private DoSnack doSnack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setSupportActionBar(loginToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        doSnack = new DoSnack(this,LoginActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            sendToMain();
        }
    }

    @OnClick(R.id.login_btn)
    public void onViewClicked() {

        if (! AppStatus.getInstance(getApplicationContext()).isOnline()) {

            doSnack.showSnackbar("You're offline", "Retry", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onViewClicked();
                }
            });

            return;
        }

        String email = loginInputEmail.getEditText().getText().toString();
        String password = loginInputPassword.getEditText().getText().toString();

        Log.d(TAG, "loginWithEmailPasswordClick: Email & password:" + email + "   " + password);

        if (validate()){

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                //login successful

                            }
                        }
                    });
        }
    }

    private void sendToMain() {

        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        String email = loginInputEmail.getEditText().getText().toString();
        String password = loginInputPassword.getEditText().getText().toString();

        if (email.isEmpty() || isEmailValid(email)) {
            loginInputEmail.setError("enter valid email");
            valid = false;
        } else {
            loginInputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            loginInputPassword.setError("at least 6 characters");
            valid = false;
        } else {
            loginInputPassword.setError(null);
        }

        return valid;
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
