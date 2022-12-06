package com.example.baedalpot.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baedalpot.ui.auth.LoginActivity;
import com.example.baedalpot.ui.home.child.BlankFragment;
import com.example.baedalpot.ui.home.child.GroupFragment;
import com.example.baedalpot.ui.home.child.MypageFragment;
import com.example.baedalpot.R;
import com.example.baedalpot.ui.home.child.SerchFragment;
import com.example.baedalpot.ui.home.child.SettingFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NaviActivity extends AppCompatActivity {
    final String TAG = this.getClass().getSimpleName();
    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    LinearLayout home_ly;
    BottomNavigationView bottomNavigationView;

    //Button btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);
        init(); //객체 정의
        SettingListener(); //리스너 등록

        //맨 처음 시작할 탭 설정
        bottomNavigationView.setSelectedItemId(R.id.tab_serch);

       // btnLogout = (Button) findViewById(R.id.btnLogout);

        /*btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(NaviActivity.this, LoginActivity.class));
            }
        });
        */
    }

    private void init() {
        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void SettingListener() {
        //선택 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new TabSelectedListener());
    }

    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.tab_group: {
                    db.child("UserAccount").child(auth.getUid()).child("group").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            String s = task.getResult().getValue(String.class);
                            if(s == null){
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.home_ly, new BlankFragment())
                                        .commit();
                            }else{
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.home_ly, new SerchFragment())
                                        .commit();
                            }

                        }
                    });

                    return true;
                }
                case R.id.tab_serch: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new GroupFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_home: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new MypageFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_setting: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new SettingFragment())
                            .commit();
                    return true;
                }
            }

            return false;
        }
    }

}