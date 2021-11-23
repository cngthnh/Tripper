package com.triplet.tripper;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.triplet.tripper.databinding.ActivityAuthBinding;
import com.triplet.tripper.databinding.AuthLayoutBinding;
import com.triplet.tripper.databinding.SigninLayoutBinding;
import com.triplet.tripper.databinding.SignupLayoutBinding;
import com.triplet.tripper.models.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityAuthBinding binding;
    FirebaseAuth mAuth;
    CallbackManager mCallbackManager;
    GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> intentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();
        registerGoogleSignInResult();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



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
        authLayoutBinding.fbSignIn.setOnClickListener(this);
        authLayoutBinding.googleSignIn.setOnClickListener(this);
        fadeOut(binding.title);
        binding.title.setText("Xin chào!");
        fadeIn(binding.title);
    }

    private void fadeIn(View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setInterpolator(new DecelerateInterpolator()).setDuration(500).setListener(null);
    }

    private void fadeOut(View view) {
        view.animate()
                .alpha(0f)
                .setDuration(500)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });
    }

    private void prepareSignUpLayout() {
        removePrevLayouts();
        SignupLayoutBinding signupLayoutBinding = SignupLayoutBinding.inflate(getLayoutInflater(), binding.authFrame, true);
        signupLayoutBinding.signUp.setOnClickListener(this);
        signupLayoutBinding.anotherAuth.setOnClickListener(this);
        fadeOut(binding.title);
        binding.title.setText("Đăng ký");
        fadeIn(binding.title);
    }

    private void prepareSignInLayout() {
        removePrevLayouts();
        SigninLayoutBinding signinLayoutBinding = SigninLayoutBinding.inflate(getLayoutInflater(), binding.authFrame, true);
        signinLayoutBinding.signIn.setOnClickListener(this);
        signinLayoutBinding.anotherAuth.setOnClickListener(this);
        fadeOut(binding.title);
        binding.title.setText("Đăng nhập");
        fadeIn(binding.title);
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
                            createUserInfoIfNotExists(user);
                            Toast.makeText(AuthActivity.this, "Đăng ký tài khoản thành công", Toast.LENGTH_LONG).show();

                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(AuthActivity.this, "Tài khoản đã được đăng ký hoặc thông tin đăng ký không hợp lệ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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
        else if (viewId == R.id.fbSignIn) {
            binding.progressBar.setVisibility(View.VISIBLE);
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(AuthActivity.this, "Có lỗi xảy ra khi đăng nhập với Facebook", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(AuthActivity.this, "Có lỗi xảy ra khi đăng nhập với Facebook", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (viewId == R.id.googleSignIn) {
            binding.progressBar.setVisibility(View.VISIBLE);
            handleGoogleSignIn();
        }
    }

    private void registerGoogleSignInResult() {
        intentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                            mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                        User user = new User(firebaseUser.getDisplayName(), firebaseUser.getEmail());
                                        Toast.makeText(AuthActivity.this, "Xác thực với Google thành công", Toast.LENGTH_LONG).show();
                                        createUserInfoIfNotExists(user);
                                    }
                                    else {
                                        binding.progressBar.setVisibility(View.GONE);
                                        Toast.makeText(AuthActivity.this, "Có lỗi xảy ra khi đăng nhập với Google", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        catch (ApiException e) {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(AuthActivity.this, "Có lỗi xảy ra khi đăng nhập với Google", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createUserInfoIfNotExists(User user) {

        FirebaseDatabase.getInstance().getReference("users")
                .child(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.getResult().getValue() != null) {
                            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            createUserInfo(user);
                        }
                    }
                });
    }

    private void createUserInfo(User user) {
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
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(AuthActivity.this, "Không thể thêm thông tin cá nhân vào tài khoản", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void handleGoogleSignIn() {

        Intent intent = mGoogleSignInClient.getSignInIntent();
        intentLauncher.launch(intent);
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            User user = new User(firebaseUser.getDisplayName(), firebaseUser.getEmail());
                            Toast.makeText(AuthActivity.this, "Xác thực với Facebook thành công", Toast.LENGTH_LONG).show();
                            createUserInfoIfNotExists(user);

                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(AuthActivity.this, "Có lỗi xảy ra khi đăng nhập bằng Facebook",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                    binding.progressBar.setVisibility(View.GONE);
                    binding.authFrame.findViewById(R.id.signIn).setEnabled(true);
                    binding.authFrame.findViewById(R.id.anotherAuth).setEnabled(true);
                    Toast.makeText(AuthActivity.this, "Thông tin đăng nhập không chính xác", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}