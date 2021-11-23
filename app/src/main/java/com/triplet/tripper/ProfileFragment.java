package com.triplet.tripper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.triplet.tripper.databinding.FragmentProfileBinding;
import com.triplet.tripper.models.User;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    FragmentProfileBinding binding;
    User currentUser;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        
        getUserData();
        prepareListener();

        return view;
    }

    private void prepareListener() {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fab.setImageResource(R.drawable.ic_animation);
        fab.setBackgroundTintList(AppCompatResources.getColorStateList(getContext(), R.color.light_rose));
        binding.editAge.setOnClickListener(this);
        binding.editAddress.setOnClickListener(this);
        binding.editFavPlace.setOnClickListener(this);
        binding.editName.setOnClickListener(this);
        binding.signOut.setOnClickListener(this);
    }

    private void getUserData() {
        mDatabase.getReference("users").child(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    try {
                        currentUser = task.getResult().getValue(User.class);
                        updateUI();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Không thể lấy thông tin cá nhân", Toast.LENGTH_LONG).show();
                        currentUser = new User();
                    }
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }

    private void writeUserData() {
        mDatabase.getReference("users").child(mAuth.getUid())
                .setValue(currentUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateUI();
                        } else {
                            Log.d("firebase", "Không thể cập nhật dữ liệu lên giao diện");
                        }
                    }
                });
    }

    private void updateUI() {
        binding.age.setText(String.valueOf(currentUser.getAge()));
        binding.name.setText(currentUser.getName());
        binding.email.setText(currentUser.getEmail());
        binding.address.setText(currentUser.getAddress());
        binding.favPlace.setText(currentUser.getFavPlace());
        binding.animationView.setPattern(currentUser.getPetType());
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.fab) {
            currentUser.setPetType(binding.animationView.nextPattern());
            binding.animationView.setPattern(currentUser.getPetType());
            writeUserData();
        }
        else if (viewId == R.id.editAge) {

            if (binding.age.isFocusable()) {
                try {
                    currentUser.setAge(Integer.parseInt(binding.age.getText().toString()));
                }
                catch (Exception e) {
                    binding.age.setFocusable(false);
                    binding.age.setFocusableInTouchMode(false);
                    binding.editAge.setImageResource(R.drawable.ic_edit);
                    binding.age.setText(currentUser.getAge());
                    return;
                }
                binding.age.setFocusable(false);
                binding.age.setFocusableInTouchMode(false);
                binding.editAge.setImageResource(R.drawable.ic_edit);
                writeUserData();
            } else {
                binding.editAge.setImageResource(R.drawable.ic_done);
                binding.age.setFocusable(true);
                binding.age.setFocusableInTouchMode(true);
                binding.age.requestFocus();
            }

        }
        else if (viewId == R.id.editAddress) {

            if (binding.address.isFocusable()) {
                currentUser.setAddress(binding.address.getText().toString());
                binding.address.setFocusable(false);
                binding.address.setFocusableInTouchMode(false);
                binding.editAddress.setImageResource(R.drawable.ic_edit);
                writeUserData();
            } else {
                binding.editAddress.setImageResource(R.drawable.ic_done);
                binding.address.setFocusable(true);
                binding.address.setFocusableInTouchMode(true);
                binding.address.requestFocus();
            }

        }
        else if (viewId == R.id.editFavPlace) {

            if (binding.favPlace.isFocusable()) {
                currentUser.setFavPlace(binding.favPlace.getText().toString());
                binding.favPlace.setFocusable(false);
                binding.favPlace.setFocusableInTouchMode(false);
                binding.editFavPlace.setImageResource(R.drawable.ic_edit);
                writeUserData();
            } else {
                binding.editFavPlace.setImageResource(R.drawable.ic_done);
                binding.favPlace.setFocusable(true);
                binding.favPlace.setFocusableInTouchMode(true);
                binding.favPlace.requestFocus();
            }
        }
        else if (viewId == R.id.editName) {

            if (binding.name.isFocusable()) {
                currentUser.setName(binding.name.getText().toString());
                binding.name.setFocusable(false);
                binding.name.setFocusableInTouchMode(false);
                binding.editName.setImageResource(R.drawable.ic_edit);
                writeUserData();
            } else {
                binding.editName.setImageResource(R.drawable.ic_done);
                binding.name.setFocusable(true);
                binding.name.setFocusableInTouchMode(true);
                binding.name.requestFocus();
            }
        }
        else if (viewId == R.id.signOut) {
            mAuth.signOut();
            startActivity(new Intent(getActivity(), AuthActivity.class));
            getActivity().finish();
        }
    }

}