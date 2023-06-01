package kr.ac.duksung.projectvegan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import kr.ac.duksung.projectvegan.R;

public class Register3Activity extends AppCompatActivity {

    // Radio_Vegan Radio_Lacto Radio_Ovo Radio_LactoOvo Radio_Pollo Radio_Pesco Radio_etc

    // 변수 선언 (RadioButton은 선언 안해도 된다. RadioGruop만 잘 해주자)
    private RadioGroup Rg_veganType;
    private Button Btn_Register3To4;
    private TextView Tv_SelectedVeganType;
    private String userId, userEmail, userPassword, userVeganReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        // 변수 초기화
        Rg_veganType = findViewById(R.id.Rg_veganType);

        Tv_SelectedVeganType = findViewById(R.id.Tv_SelectedVeganType);

        // 앞선 Activity에서 보낸 정보 받아 변수에 초기화하기
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userEmail = intent.getStringExtra("userEmail");
        userPassword = intent.getStringExtra("userPassword");
        userVeganReason = intent.getStringExtra("userVeganReason");

        // 회원가입 버튼 클릭시
        Btn_Register3To4 = findViewById(R.id.Btn_Register3To4);
        Btn_Register3To4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userVeganType = Tv_SelectedVeganType.getText().toString();

                // 선택 안한경우
                if (userVeganType.equals("")) {
                    Toast.makeText(Register3Activity.this, "선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    // 선택 한 경우 petExtra를 이용해 정보 보내기 -> 회원가입 마지막 단계에서 합치기 위함
                    Log.d("Register", userVeganType);
                    Intent intent2 = new Intent(Register3Activity.this, Register4Activity.class);
                    intent2.putExtra("userId", userId);
                    intent2.putExtra("userEmail", userEmail);
                    intent2.putExtra("userPassword", userPassword);
                    intent2.putExtra("userVeganReason", userVeganReason);
                    intent2.putExtra("userVeganType", userVeganType);
                    startActivity(intent2);
                }
            }
        });

        // RadioButton 리스너 작성
        Rg_veganType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.Radio_Vegan:
                        Tv_SelectedVeganType.setText("비건");
                        break;
                    case R.id.Radio_Lacto:
                        Tv_SelectedVeganType.setText("락토");
                        break;
                    case R.id.Radio_Ovo:
                        Tv_SelectedVeganType.setText("오보");
                        break;
                    case R.id.Radio_LactoOvo:
                        Tv_SelectedVeganType.setText("락토오보");
                        break;
                    case R.id.Radio_Pesco:
                        Tv_SelectedVeganType.setText("페스코");
                        break;
                    case R.id.Radio_Pollo:
                        Tv_SelectedVeganType.setText("폴로");
                        break;
                    case R.id.Radio_etc:
                        Tv_SelectedVeganType.setText("지향없음");
                        break;
                };
            }
        });

    }
}