package com.example.baedalpot.ui.home;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baedalpot.ui.home.child.GroupFragment;
import com.example.baedalpot.ui.home.child.MypageFragment;
import com.example.baedalpot.R;
import com.example.baedalpot.ui.home.child.SerchFragment;
import com.example.baedalpot.ui.home.child.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NaviActivity extends AppCompatActivity {
    final String TAG = this.getClass().getSimpleName();

    LinearLayout home_ly;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);
        init(); //객체 정의
        SettingListener(); //리스너 등록

        //맨 처음 시작할 탭 설정
        bottomNavigationView.setSelectedItemId(R.id.tab_serch);
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
                case R.id.tab_serch: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new SerchFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_group: {
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