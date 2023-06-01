package kr.ac.duksung.projectvegan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import kr.ac.duksung.projectvegan.R;
import kr.ac.duksung.projectvegan.model.UserProfile;

public class Register4Activity extends AppCompatActivity {

    // Cb_memil Cb_mil Cb_daedu Cb_hodu Cb_peanut Cb_peach Cb_tomato Cb_poultry Cb_milk
    // Cb_shrimp Cb_mackerel Cb_mussel Cb_abalone Cb_oyster Cb_shellfish Cb_crab Cb_squid Cb_sulfurous

    // 변수 선언
    private CheckBox Cb_memil, Cb_mil, Cb_daedu, Cb_hodu, Cb_peanut, Cb_peach, Cb_tomato, Cb_poultry, Cb_milk,
            Cb_shrimp, Cb_mackerel, Cb_mussel, Cb_abalone, Cb_oyster, Cb_shellfish, Cb_crab, Cb_squid, Cb_sulfurous;
    private Button Btn_RegisterFinish;
    private TextView Tv_SelectedAllergy;
    private int count = 0;
    private String userId, userEmail, userPassword, userVeganReason, userVeganType, userAllergy;

    // Firebase 인증 인스턴스 선언
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4);

        // 인증 인스턴스 초기화
        firebaseAuth = FirebaseAuth.getInstance();

        // 변수 초기화
        Cb_memil = findViewById(R.id.Cb_memil);
        Cb_mil = findViewById(R.id.Cb_mil);
        Cb_daedu = findViewById(R.id.Cb_daedu);
        Cb_hodu = findViewById(R.id.Cb_hodu);
        Cb_peanut = findViewById(R.id.Cb_peanut);
        Cb_peach = findViewById(R.id.Cb_peach);
        Cb_tomato = findViewById(R.id.Cb_tomato);
        Cb_poultry = findViewById(R.id.Cb_poultry);
        Cb_milk = findViewById(R.id.Cb_milk);
        Cb_shrimp = findViewById(R.id.Cb_shrimp);
        Cb_mackerel = findViewById(R.id.Cb_mackerel);
        Cb_mussel = findViewById(R.id.Cb_mussel);
        Cb_abalone = findViewById(R.id.Cb_abalone);
        Cb_oyster = findViewById(R.id.Cb_oyster);
        Cb_shellfish = findViewById(R.id.Cb_shellfish);
        Cb_crab = findViewById(R.id.Cb_crab);
        Cb_squid = findViewById(R.id.Cb_squid);
        Cb_sulfurous = findViewById(R.id.Cb_sulfurous);

        // 클릭 이벤트 구현 -> 효과는 나중에 주기
        Cb_memil.setOnClickListener(CbClickListener);
        Cb_mil.setOnClickListener(CbClickListener);
        Cb_daedu.setOnClickListener(CbClickListener);
        Cb_hodu.setOnClickListener(CbClickListener);
        Cb_peanut.setOnClickListener(CbClickListener);
        Cb_peach.setOnClickListener(CbClickListener);
        Cb_tomato.setOnClickListener(CbClickListener);
        Cb_poultry.setOnClickListener(CbClickListener);
        Cb_milk.setOnClickListener(CbClickListener);
        Cb_shrimp.setOnClickListener(CbClickListener);
        Cb_mackerel.setOnClickListener(CbClickListener);
        Cb_mussel.setOnClickListener(CbClickListener);
        Cb_abalone.setOnClickListener(CbClickListener);
        Cb_oyster.setOnClickListener(CbClickListener);
        Cb_shellfish.setOnClickListener(CbClickListener);
        Cb_crab.setOnClickListener(CbClickListener);
        Cb_squid.setOnClickListener(CbClickListener);
        Cb_sulfurous.setOnClickListener(CbClickListener);

        Tv_SelectedAllergy = findViewById(R.id.Tv_SelectedAllergy);

        // 앞선 Activity에서 보낸 정보 받아서 변수 초기화
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userEmail = intent.getStringExtra("userEmail");
        userPassword = intent.getStringExtra("userPassword");
        userVeganReason = intent.getStringExtra("userVeganReason");
        userVeganType = intent.getStringExtra("userVeganType");

        // 회원가입 버튼 클릭시
        Btn_RegisterFinish = findViewById(R.id.Btn_RegisterFinish);
        Btn_RegisterFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 선택 안한경우 text 바꿔서 저장
                if (count == 0) {
                    Tv_SelectedAllergy.append(" 없음");
                    userAllergy = Tv_SelectedAllergy.getText().toString();
                } else {
                    userAllergy = Tv_SelectedAllergy.getText().toString();
                }
                Log.d("Register", userAllergy);
                // 함수 선언
                updateUserProfile(userId, userEmail, userPassword, userVeganReason, userVeganType, userAllergy);
                Intent intent3 = new Intent(Register4Activity.this, MainActivity.class);
                startActivity(intent3);
            }
        });
    }

    // db에 회원정보 저장하는 함수
    // Firebase Firestore에 회원가입 단계의 모든 정보를 한번에 저장한다.
    private void updateUserProfile(String userId, String userEmail, String userPassword, String userVeganReason, String userVeganType, String userAllergy) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        UserProfile userProfile = new UserProfile(userId, userEmail, userPassword, userVeganReason, userVeganType, userAllergy);

        if (firebaseUser != null){
            db.collection("USERS").document(firebaseUser.getUid()).set(userProfile)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "회원정보 등록 성공", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "회원정보 등록 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
////                .setDisplayName(userId)
//                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
//                .build();
//
//        user.updateProfile(profileUpdates)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("UpdateProfile", "User profile updated.");
//                        }
//                    }
//                });

    }

    // CheckBox 클릭 리스너 구현
    View.OnClickListener CbClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean checked = ((CheckBox) view).isChecked();

            switch (view.getId()) {
                case R.id.Cb_memil:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "메밀", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("메밀 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_mil:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "밀", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("밀 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_daedu:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "대두", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("대두 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_hodu:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "호두", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("호두 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_peanut:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "땅콩", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("땅콩 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_peach:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "복숭아", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("복숭아 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_tomato:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "토마토", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("토마토 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_poultry:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "가금류", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("가금류 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_milk:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "우유", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("우유 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_shrimp:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "새우", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("새우 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_mackerel:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "고등어", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("고등어 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_mussel:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "홍합", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("홍합 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_abalone:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "전복", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("전복 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_oyster:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "굴", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("굴 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_shellfish:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "조개류", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("조개류 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_crab:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "게", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("게 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_squid:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "오징어", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("오징어 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.Cb_sulfurous:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "아황산", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("아황산 ");
                        count++;
                    } else {

                    }
                    break;
            }
        }
    };
}