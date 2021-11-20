package com.triplet.tripper;

import android.animation.LayoutTransition;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.triplet.tripper.MapsFragment;
import com.triplet.tripper.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    int currentPosition = 0;
    FirebaseAuth mAuth;

    private boolean loadFragment(Fragment fragment, int newPosition) {
        if(fragment != null) {

            if(currentPosition > newPosition) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right )
                        .replace(R.id.frame, fragment).commit();

            }

            if(currentPosition < newPosition) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.frame, fragment).commit();

            }

            currentPosition = newPosition;
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        mAuth = FirebaseAuth.getInstance();

        View view = binding.getRoot();
        setContentView(view);

        binding.bottomNavigation.setBackground(null);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.frame, new MapsFragment()).commit();


        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int nextPosition = 0;

                int itemId = item.getItemId();
                if (itemId == R.id.menu_history) {
                    selectedFragment = null;
                    nextPosition = 1;
                } else if (itemId == R.id.menu_profile) {
                    selectedFragment = new ProfileFragment();
                    nextPosition = 2;
                } else if (itemId == R.id.menu_map) {
                    nextPosition = 0;
                    selectedFragment = new MapsFragment();
                }

                return loadFragment(selectedFragment, nextPosition);
            }

        });

    }


}