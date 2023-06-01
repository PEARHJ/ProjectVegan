package kr.ac.duksung.projectvegan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import kr.ac.duksung.projectvegan.R;

public class LoginActivity extends AppCompatActivity {

    // 변수 선언
    private EditText Et_Login_Email, Et_Login_Password;
    private Button Btn_Login, Btn_KakaoLogin, Btn_Register, Btn_NoRegister;

    // Firebase 인증 인스턴스 선언
    private FirebaseAuth firebaseAuth;

    // 뒤로가기 버튼 눌렀을 경우
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // 로그인 여부 확인 -> 되어있으면 바로 메인페이지 열어주기
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // FirebaseAuth 객체의 공유 인스턴스 초기화
        firebaseAuth = FirebaseAuth.getInstance();

        // 변수 초기화
        Et_Login_Email = findViewById(R.id.Et_Login_Email);
        Et_Login_Password = findViewById(R.id.Et_Login_Password);

        Btn_Login = findViewById(R.id.Btn_Login);
        Btn_KakaoLogin = findViewById(R.id.Btn_Login_Kakao);
        Btn_NoRegister = findViewById(R.id.Btn_NoRegister);
        Btn_Register = findViewById(R.id.Btn_Register);

        // 로그인 버튼 클릭시
        Btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 문자열에 Et에 현재 입력되어 있는 값을 가져와 담기
                String userEmail = Et_Login_Email.getText().toString().trim();
                String userPassword = Et_Login_Password.getText().toString().trim();

                // Et 조건 설정 -> signInWithEmailAndPassword 이용해 로그인
                if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(LoginActivity.this, "모두 작성해 주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                        Log.d("Login", "로그인 성공");
                                    } else {
                                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                        Log.d("Login", "로그인 실패");
                                    }
                                }
                            });
                }
            }
        });

        // 회원가입 버튼 클릭시
        Btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Register1Activity.class);
                startActivity(intent);
            }
        });
    }
}