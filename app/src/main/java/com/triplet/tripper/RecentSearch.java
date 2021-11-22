package com.triplet.tripper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Parcel;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


@SuppressLint("ParcelCreator")
public class RecentSearch implements SearchSuggestion {
    public RecentSearch(String word) {
        this.word = word;
    }

    String word;
    @Override
    public String getBody() {
        return word;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
    }
}
