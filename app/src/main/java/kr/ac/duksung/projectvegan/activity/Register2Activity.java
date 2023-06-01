package kr.ac.duksung.projectvegan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import kr.ac.duksung.projectvegan.R;
import kr.ac.duksung.projectvegan.model.UserProfile;

public class Register2Activity extends AppCompatActivity {

    // 변수 선언
    //Cb_Environment Cb_Animal Cb_Health Cb_Religion Cb_Etc
    private Button Btn_Register2To3;
    private CheckBox Cb_Environment, Cb_Animal, Cb_Health, Cb_Religion, Cb_Etc;
    private TextView Tv_SelectedVeganReason;
    private String userId, userEmail, userPassword;

    // Firebase 인증 인스턴스 선언
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        // FirebaseAuth 객체의 공유 인스턴스 초기화
        firebaseAuth = FirebaseAuth.getInstance();

        // 변수 초기화
        Cb_Environment = findViewById(R.id.Cb_Environment);
        Cb_Animal = findViewById(R.id.Cb_Animal);
        Cb_Health = findViewById(R.id.Cb_Health);
        Cb_Religion = findViewById(R.id.Cb_Religion);
        Cb_Etc = findViewById(R.id.Cb_Etc);

        // 클릭 이벤트 미리 주기 -> 이후에 Cb에 따라서 CbClickListener에서 효과 주기
        Cb_Environment.setOnClickListener(CbClickListener);
        Cb_Animal.setOnClickListener(CbClickListener);
        Cb_Health.setOnClickListener(CbClickListener);
        Cb_Religion.setOnClickListener(CbClickListener);
        Cb_Etc.setOnClickListener(CbClickListener);

        Tv_SelectedVeganReason = findViewById(R.id.Tv_SelectedVeganReason);

        // Register1Activity에서 보내는 putExtra값 받아서 변수 초기화해주기
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userEmail = intent.getStringExtra("userEmail");
        userPassword = intent.getStringExtra("userPassword");

        // 회원가입 버튼 클릭시
        Btn_Register2To3 = findViewById(R.id.Btn_Register2To3);
        Btn_Register2To3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userVeganReason = Tv_SelectedVeganReason.getText().toString();

                // Cb 선택안한경우
                if (userVeganReason.equals("")) {
                    Toast.makeText(Register2Activity.this, "선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    // Cb 선택했을 경우 -> intent를 이용해 다음 Activity로 정보 전달
                    Log.d("Register", userVeganReason);
                    Intent intent1 = new Intent(Register2Activity.this, Register3Activity.class);
                    intent1.putExtra("userId", userId);
                    intent1.putExtra("userEmail", userEmail);
                    intent1.putExtra("userPassword", userPassword);
                    intent1.putExtra("userVeganReason", userVeganReason);
                    startActivity(intent1);
                }
            }
        });
    }

    // 클릭 리스너 구현 -> Cb 경우에 따라 결과값 받기
    View.OnClickListener CbClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            boolean checked = ((CheckBox) view).isChecked();

            switch (view.getId()) {
                case R.id.Cb_Environment:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "환경", Toast.LENGTH_SHORT).show();
                        Tv_SelectedVeganReason.append(" 환경");
                    } else {
                        Tv_SelectedVeganReason.setText("");
                    }
                    break;
                case R.id.Cb_Animal:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "동물권", Toast.LENGTH_SHORT).show();
                        Tv_SelectedVeganReason.append(" 동물권");
                    } else {
                        Tv_SelectedVeganReason.setText("");
                    }
                    break;
                case R.id.Cb_Health:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "건강", Toast.LENGTH_SHORT).show();
                        Tv_SelectedVeganReason.append(" 건강");
                    } else {
                        Tv_SelectedVeganReason.setText("");
                    }
                    break;
                case R.id.Cb_Religion:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "종교", Toast.LENGTH_SHORT).show();
                        Tv_SelectedVeganReason.append(" 종교");
                    } else {
                        Tv_SelectedVeganReason.setText("");
                    }
                    break;
                case R.id.Cb_Etc:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "기타", Toast.LENGTH_SHORT).show();
                        Tv_SelectedVeganReason.append(" 기타");
                    } else {
                        Tv_SelectedVeganReason.setText("");
                    }
                    break;
                default:
            }

        }
    };
}