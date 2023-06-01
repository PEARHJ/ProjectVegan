package kr.ac.duksung.projectvegan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import kr.ac.duksung.projectvegan.R;
import kr.ac.duksung.projectvegan.fragment.HomeFeedFragment;
import kr.ac.duksung.projectvegan.model.UserProfile;

public class MypageActivity extends AppCompatActivity {

    private Button Btn_EditAccount;
    private TextView Tv_ID, Tv_VeganType, Tv_Allergy, Btn_Logout, Btn_DeleteAccount;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    String USER_ID, USER_VEGANTYPE, USER_ALLERGY;


//    @SuppressLint("WrongViewCast") // Btn_DeleteAccount때문에 생김 -> 아마 TextView를 Button으로 선언해서 그런듯
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Btn_EditAccount = findViewById(R.id.Btn_EditAccount);
        Btn_DeleteAccount = findViewById(R.id.Btn_DeleteAccount);
        Btn_Logout = findViewById(R.id.Btn_Logout);
        Tv_ID = findViewById(R.id.Tv_Mypage_UserId);
        Tv_VeganType = findViewById(R.id.Tv_Mypage_VeganType);
        Tv_Allergy = findViewById(R.id.Tv_Mypage_Allergy);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // 마이페이지 생성시 사용자 기본 정보 가져와 표시
        db.collection("USERS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<UserProfile> UserMypageProfile = new ArrayList<>();

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null) {

                                String uid = firebaseUser.getUid();

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());
//                                    UserMypageProfile.add(new UserProfile(
//                                            // userId, userEmail, userPassword, userVeganReason, userVeganType, userAllergy
//                                            documentSnapshot.getData().get("userId").toString(),
//                                            documentSnapshot.getData().get("userEmail").toString(),
//                                            documentSnapshot.getData().get("userPassword").toString(),
//                                            documentSnapshot.getData().get("userVeganReason").toString(),
//                                            documentSnapshot.getData().get("userVeganType").toString(),
//                                            documentSnapshot.getData().get("userAllergy").toString()));

                                    if (documentSnapshot.getId().equals(uid)) {
                                        USER_ID = documentSnapshot.getData().get("userId").toString();
                                        USER_VEGANTYPE = documentSnapshot.getData().get("userVeganType").toString();
                                        USER_ALLERGY = documentSnapshot.getData().get("userAllergy").toString();
                                        Tv_ID.setText(USER_ID);
                                        Tv_VeganType.setText(USER_VEGANTYPE);
                                        Tv_Allergy.setText(USER_ALLERGY);
//                                        Intent intent = new Intent(MypageActivity.this, MainActivity.class);
//                                        intent.putExtra("userId", USER_ID);
//                                        startActivity(intent);
                                    } else {
                                        Log.d("MyPage ERROR", Tv_ID.toString(), task.getException());
                                    }
                                }
                            }
                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });



        // 로그아웃 버튼 클릭시
        Btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(MypageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // 프로필 정보 수정 버튼 클릭시
        Btn_EditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName("Jane Q. User")
                        .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                        .build();

                firebaseUser.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("EditAccount", "User profile updated.");
                                }
                            }
                        });
            }
        });

        // 계정 삭제 버튼 클릭시
        Btn_DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseUser.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("AccountDelete", "User account deleted.");
                                }
                            }
                        });
            }
        });
    }
}