package com.senerunosoft.ironbuff.activity;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.ActivityMainMenuBinding;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class MainMenuActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainMenuBinding binding;
    TextView headerEmail, headerNameSurname;
    ImageView headerImg;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        getNavData();

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.myProfileFragment, R.id.mainMenuFragment, R.id.trainingProgramFragment, R.id.messageFragment, R.id.settingsFragment, R.id.logout_drawer)
                .setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_content_main);


        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    private void getNavData() {

//        QuerySnapshot snapshots = firestore.collection("userTable").whereEqualTo("E-mail", auth.getCurrentUser().getEmail()).get().getResult();
        firestore.collection("userTable").whereEqualTo("E-mail", auth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()){
                        String email = (String) doc.getData().get("E-mail");
                        String namesurname = (String) doc.getData().get("NameSurname");
                        String url = doc.getData().get("imageUrl").toString();
                        Picasso.get().load(url).into(headerImg);
                        headerNameSurname.setText(namesurname);
                        headerEmail.setText(email);



                    }
                }
            }
        });
    }
}


