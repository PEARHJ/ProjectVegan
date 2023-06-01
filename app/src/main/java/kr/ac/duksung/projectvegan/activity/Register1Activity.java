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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import kr.ac.duksung.projectvegan.R;
import kr.ac.duksung.projectvegan.model.UserProfile;

public class Register1Activity extends AppCompatActivity {

    // 변수 선언
    private EditText Et_Register_Id, Et_Register_Email, Et_Register_Password, Et_Register_Passwordcheck;
    private Button Btn_Register1To2;

    // Firebase 인증 인스턴스 선언
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        // FirebaseAuth 객체의 공유 인스턴스 초기화
        firebaseAuth = FirebaseAuth.getInstance();

        // 변수 초기화
        Et_Register_Id = findViewById(R.id.Et_Register_Id);
        Et_Register_Email = findViewById(R.id.Et_Register_Email);
        Et_Register_Password = findViewById(R.id.Et_Register_Password);
        Et_Register_Passwordcheck = findViewById(R.id.Et_Register_Passwordcheck);

        Btn_Register1To2 = findViewById(R.id.Btn_Register1To2);
        Btn_Register1To2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Et에 현재 입력되어 있는 값을 가져온다.
                String userId = Et_Register_Id.getText().toString();
                String userEmail = Et_Register_Email.getText().toString();
                String userPassword = Et_Register_Password.getText().toString();
                String userPasswordcheck = Et_Register_Passwordcheck.getText().toString();

                // Et 양식 조건
                if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword) || TextUtils.isEmpty(userPasswordcheck)){
                    Toast.makeText(Register1Activity.this, "모든 양식을 채워주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    if (userPassword.length() < 6){
                        Toast.makeText(Register1Activity.this, "비밀번호는 최소 6자리 이상으로 해주세요!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (userPassword.equals(userPasswordcheck)) {
                            register(userEmail, userPassword);
                            Log.d("Register", userId);
                            // intent를 이용해 정보 전달 -> 마지막 회원가입 단계에서 한번에 db에 저장하기 위함
                            Intent intent = new Intent(Register1Activity.this, Register2Activity.class);
                            intent.putExtra("userId", userId);
                            intent.putExtra("userEmail", userEmail);
                            intent.putExtra("userPassword", userPassword);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Register1Activity.this, "비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    // 회원가입 정보 등록
    private void register (String userEmail, String userPassword) {

        // 신규 사용자의 이메일 주소와 비밀번호 createUserWithEmailAndPassword에 전달
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(Register1Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("REGISTER", "createUserWithEmail-success");
                        } else {
                            Log.d("REGISTER", "createUserWithEmail-failure", task.getException());
                            Toast.makeText(Register1Activity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}