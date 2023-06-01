package kr.ac.duksung.projectvegan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import kr.ac.duksung.projectvegan.R;
import kr.ac.duksung.projectvegan.adapter.CommentAdapter;
import kr.ac.duksung.projectvegan.model.CommentInfo;

public class CommentActivity extends AppCompatActivity {

    // 객체 선언
    private Button Btn_UploadComment;
    private EditText Et_Comment;
    private String FeedId;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // 객체 초기화
        Btn_UploadComment = findViewById(R.id.Btn_UploadComment);
        Et_Comment = findViewById(R.id.Et_Comment);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // 댓글 등록 버튼 클릭시
        Btn_UploadComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadComment();
                finish();
            }
        });

        // 해당 activity 실행 시, COMMENTS 콜렉션에서 db 읽어오기
        // FeedId를 받아서 해당하는 댓글들만 가져온다.
        // HomeFeedAdapter에서 FeedId값 받아오기
        Intent intent = getIntent();
        FeedId = intent.getStringExtra("POSTSDocumentId");
        Log.d("DOCUMENTID_Receive", FeedId);

        // COMMENTS 컬렉션으로부터 정보 받아오기
        db.collection("POSTS/" + FeedId + "/COMMENTS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<CommentInfo> commentList = new ArrayList<>();

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d("Comment Success", documentSnapshot.getId() + "=>" + documentSnapshot.getData());
                                commentList.add(new CommentInfo(
                                        documentSnapshot.getData().get("comment").toString(),
                                        documentSnapshot.getData().get("publisher").toString(),
                                        documentSnapshot.getId(),
                                        new Date(documentSnapshot.getDate("createdAt").getTime())
                                ));
                            }
                            RecyclerView recyclerView = findViewById(R.id.Rv_Comment);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));

                            RecyclerView.Adapter cAdapter = new CommentAdapter(commentList);
                            recyclerView.setAdapter(cAdapter);
                        }
                    }
                });
    }

    // 댓글 등록 함수 실행
    private void uploadComment() {
        final String comment = Et_Comment.getText().toString();

        if (comment.length() > 0) {

            CommentInfo commentInfo = new CommentInfo(comment, firebaseUser.getUid(), FeedId, new Date());

            CollectionReference collectionReference = db.collection("POSTS").document(FeedId).collection("COMMENTS");
            collectionReference
                    .add(commentInfo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("COMMENTSUPLOAD Success", FeedId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("COMMENTSUPLOAD Failure", FeedId);
                        }
                    });
        } else {
            Toast.makeText(CommentActivity.this, "댓글 내용을 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}