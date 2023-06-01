package kr.ac.duksung.projectvegan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import kr.ac.duksung.projectvegan.R;
import kr.ac.duksung.projectvegan.fragment.HomeFeedFragment;
import kr.ac.duksung.projectvegan.fragment.MyFeedFragment;

public class MainActivity extends AppCompatActivity {

    // 변수 선언
    private FloatingActionButton Btn_AddFeed;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private Fragment Fm_HomeFeed, Fm_MyFeed;

    // Firebase Authentication 인스턴스 선언
    private FirebaseAuth firebaseAuth;

    // toolbar 메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // toolbar 메뉴 선택이벤트
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
//                Toast.makeText(getApplicationContext(), "검색창 클릭됨", Toast.LENGTH_SHORT).show();
//                getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,fragment_search).commitAllowingStateLoss();
                return true;
            case R.id.action_settings:
//                Toast.makeText(getApplicationContext(), "마이페이지 클릭됨", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                startActivity(intent);
                return true;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 변수 초기화
        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        Btn_AddFeed = findViewById(R.id.Btn_AddFeed);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Fm_HomeFeed = new HomeFeedFragment();
        Fm_MyFeed = new MyFeedFragment();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 초기 메인 프래그먼트 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, Fm_HomeFeed).commitAllowingStateLoss();

        // 바텀 네비게이션 리스너
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.HomeFeed:
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,Fm_HomeFeed).commitAllowingStateLoss();
                        return true;
                    case R.id.Map:
//                        Intent intent = new Intent(MainActivity.this, MapActivity.class);
//                        startActivity(intent);
                        return true;
                    case R.id.Ocr:
//                        Intent ocrIntent = new Intent(MainActivity.this, OcrActivity.class);
//                        startActivity(ocrIntent);
                        return true;
                    case R.id.Bookmark:
//                        Intent bookmarkIntent = new Intent(MainActivity.this, BookmarkActivity.class);
//                        startActivity(bookmarkIntent);
                        return true;
                    case R.id.Mypage:
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,Fm_MyFeed).commitAllowingStateLoss();
                        return true;
                }
                return false;
            }
        });

        // 게시물 등록 버튼 클릭시
        Btn_AddFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WriteFeedActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}