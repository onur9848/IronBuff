package com.senerunosoft.ironbuff.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.onesignal.OneSignal;
import com.senerunosoft.ironbuff.MainMenuFragment.MyProfileFragment;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.ActivityMainMenuBinding;
import com.senerunosoft.ironbuff.table.UserTable;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainMenuActivity extends AppCompatActivity {

    private static final String ONESIGNAL_APP_ID ="004e619a-7f27-48ba-ad39-c9f83195008a";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainMenuBinding binding;
    TextView headerEmail, headerNameSurname;
    ImageView headerImg;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FragmentManager manager;
    List<UserTable> userTables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE,OneSignal.LOG_LEVEL.NONE);

        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        OneSignal.promptForPushNotifications();

        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;
        View headerView = navigationView.getHeaderView(0);

        headerEmail = headerView.findViewById(R.id.header_email);
        headerNameSurname = headerView.findViewById(R.id.header_namesurname);
        headerImg = headerView.findViewById(R.id.header_profil_image);

        onChangeFireStore();

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.myProfileFragment, R.id.mainMenuFragment, R.id.trainingProgramFragment,R.id.statisticFragment, R.id.messageFragment, R.id.settingsFragment, R.id.adminFragment, R.id.logout_drawer)
                .setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        signOutClick(navigationView);


        userTables = new ArrayList<>();
        manager = getSupportFragmentManager();
    }


    private void signOutClick(NavigationView navigationView) {
        navigationView.getMenu().findItem(R.id.logout_drawer).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_content_main);

        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }


    private void onChangeFireStore() {
        firestore.collection("userTable").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    getNavData();
                }


            }
        });
    }

    private void getNavData() {

//

        firestore.collection("userTable").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    if (value.exists()) {
                        UserTable table = value.toObject(UserTable.class);
                        Picasso.get().load(table.getImageUrl()).into(headerImg);
                        headerNameSurname.setText(table.getNameSurname());
                        headerEmail.setText(table.getE_mail());
                        if (table.isAdmin()) {
                            binding.navigationView.getMenu().findItem(R.id.adminFragment).setVisible(true);
                            binding.navigationView.getMenu().findItem(R.id.mainMenuFragment).setChecked(true);

                        }

                    }
                }
            }
        });

    }


}


