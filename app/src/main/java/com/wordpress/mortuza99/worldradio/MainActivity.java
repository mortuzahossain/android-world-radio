package com.wordpress.mortuza99.worldradio;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wordpress.mortuza99.worldradio.adapter.MyRecyclerAdapter;
import com.wordpress.mortuza99.worldradio.model.RadioChenels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> countryNames = new ArrayList<>();
    static List<RadioChenels> radioChenelsList = new ArrayList<>();
    final static String TAG = "ddd";

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    ListView countryList;

    DatabaseReference refRadioStations;

    RecyclerView recyclerView;
    MyRecyclerAdapter myRecyclerAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    public static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO : ADD LOADER UNTIL LOAD DATA IN RECYCLE VIEW

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    UserSignedIn();
                } else {
                    // User Signed Out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setLogo(R.drawable.ic_launcher_web)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.PhoneBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }

    private void UserSignedIn() {

        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.mainRecyclerView);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Fire Base Data Reading And Set in Navigation List
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        refRadioStations = database.child("RadioStations");
        countryList = findViewById(R.id.countryNames);

        addOnList();
        FetchData("Africa");
        // Initial Data Load
        countryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FetchData(countryNames.get(i));
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        myRecyclerAdapter = new MyRecyclerAdapter(this, radioChenelsList);
        recyclerView.setAdapter(myRecyclerAdapter);

        myRecyclerAdapter.setItemClickListener(new MyRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String name = radioChenelsList.get(position).getName();
                String imageUrl = radioChenelsList.get(position).getImage();
                String streamUrl = radioChenelsList.get(position).getUrl();

                startActivity(new Intent(getApplicationContext(), Player.class)
                        .putExtra("NAME", name)
                        .putExtra("IMAGE", imageUrl)
                        .putExtra("STREAMURL", streamUrl)
                );

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Sign in Canceled.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void FetchData(String s) {
        DatabaseReference single_item_ref = refRadioStations.child(s);
        single_item_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                radioChenelsList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    RadioChenels radioChenels = ds.getValue(RadioChenels.class);
                    radioChenelsList.add(new RadioChenels(radioChenels.getImage(),radioChenels.getName(),radioChenels.getUrl()));
                }
                myRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error during adding station");
            }
        });
    }

    private void addOnList() {
        countryNames.clear();
        refRadioStations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, countryNames);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    countryNames.add(ds.getKey());
                    countryList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error Loading Database.");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.menuLogout) {
            AuthUI.getInstance().signOut(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus, menu);
        return true;
    }

}
