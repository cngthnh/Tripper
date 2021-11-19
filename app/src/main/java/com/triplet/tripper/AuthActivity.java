package com.triplet.tripper;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.triplet.tripper.databinding.ActivityAuthBinding;
import com.triplet.tripper.databinding.AuthLayoutBinding;
import com.triplet.tripper.databinding.SigninLayoutBinding;
import com.triplet.tripper.databinding.SignupLayoutBinding;
import com.triplet.tripper.models.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityAuthBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        prepareAuthLayout();

        setContentView(view);
    }

    private void removePrevLayouts() {
        int count = binding.authFrame.getChildCount() - 1;
        for (int i = count; i > 0; i--) {
            binding.authFrame.removeViewAt(i);
        }
    }

    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private void bindValidations(AwesomeValidation validation) {
        validation.addValidation(AuthActivity.this, R.id.email, android.util.Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        validation.addValidation(AuthActivity.this, R.id.password, "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[\\w!@#$%^&*?~()-]{8,}$", R.string.invalid_password);
        validation.addValidation(AuthActivity.this, R.id.passwordConf, R.id.password, R.string.invalid_password_conf);
    }

    private void prepareAuthLayout() {
        removePrevLayouts();
        AuthLayoutBinding authLayoutBinding = AuthLayoutBinding.inflate(getLayoutInflater(), binding.authFrame, true);
        authLayoutBinding.gotoSignIn.setOnClickListener(this);
        authLayoutBinding.gotoSignUp.setOnClickListener(this);
    }

    private void prepareSignUpLayout() {
        removePrevLayouts();
        SignupLayoutBinding signupLayoutBinding = SignupLayoutBinding.inflate(getLayoutInflater(), binding.authFrame, true);
        signupLayoutBinding.signUp.setOnClickListener(this);
        signupLayoutBinding.anotherAuth.setOnClickListener(this);
    }

    private void prepareSignInLayout() {
        removePrevLayouts();
        SigninLayoutBinding signinLayoutBinding = SigninLayoutBinding.inflate(getLayoutInflater(), binding.authFrame, true);
        signinLayoutBinding.signIn.setOnClickListener(this);
        signinLayoutBinding.anotherAuth.setOnClickListener(this);
    }

    private void createUser() {

        String email = ((EditText) binding.authFrame.findViewById(R.id.email)).getText().toString();
        String name = ((EditText) binding.authFrame.findViewById(R.id.name)).getText().toString();
        String hash = sha256(((EditText) binding.authFrame.findViewById(R.id.password)).getText().toString());

        mAuth.createUserWithEmailAndPassword(email, hash)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(name, email);

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(mAuth.getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(AuthActivity.this, "Không thể tạo tài khoản", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(AuthActivity.this, "Tài khoản đã được đăng ký hoặc thông tin đăng ký không hợp lệ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.gotoSignIn)
            prepareSignInLayout();
        else if (viewId == R.id.gotoSignUp)
            prepareSignUpLayout();
        else if (viewId == R.id.anotherAuth)
            prepareAuthLayout();
        else if (viewId == R.id.signUp) {
            AwesomeValidation validation = new AwesomeValidation(BASIC);
            bindValidations(validation);
            if (validation.validate()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.authFrame.findViewById(R.id.signUp).setEnabled(false);
                binding.authFrame.findViewById(R.id.anotherAuth).setEnabled(false);
                createUser();
            }
        }
        else if (viewId == R.id.signIn) {
            AwesomeValidation validation = new AwesomeValidation(BASIC);
            bindValidations(validation);
            if (validation.validate()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.authFrame.findViewById(R.id.signIn).setEnabled(false);
                binding.authFrame.findViewById(R.id.anotherAuth).setEnabled(false);
                signIn();
            }
        }
    }

    private void signIn() {
        String email = ((EditText) binding.authFrame.findViewById(R.id.email)).getText().toString();
        String hash = sha256(((EditText) binding.authFrame.findViewById(R.id.password)).getText().toString());

        mAuth.signInWithEmailAndPassword(email, hash).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(AuthActivity.this, "Thông tin đăng nhập không chính xác", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}