package kr.ac.duksung.projectvegan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import kr.ac.duksung.projectvegan.R;

public class EditFeedActivity extends AppCompatActivity {

    private EditText Et_EditFeed_Title, Et_EditFeed_Contents;
    private ImageView Iv_EditFeedPhoto;
    private Button Btn_EditFeed;
    private String FeedId, FeedPublisher, FeedTitle, FeedContent, FeedUri;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_feed);

        db = FirebaseFirestore.getInstance();

        Et_EditFeed_Title = findViewById(R.id.Et_EditFeed_Title);
        Et_EditFeed_Contents = findViewById(R.id.Et_EditFeed_Contents);
        Iv_EditFeedPhoto = findViewById(R.id.Iv_EditFeedPhoto);

        Intent intent = getIntent();
        FeedId = intent.getStringExtra("EditFeedId");
        FeedTitle = intent.getStringExtra("EditFeedTitle");
        FeedContent = intent.getStringExtra("EditFeedContent");
        FeedUri = intent.getStringExtra("EditFeedUri");

        Et_EditFeed_Title.setText(FeedTitle);
        Et_EditFeed_Contents.setText(FeedContent);
        Glide.with(this)
                .load(FeedUri)
                .into(Iv_EditFeedPhoto);


        Btn_EditFeed = findViewById(R.id.Btn_EditFeed);
        Btn_EditFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Title = Et_EditFeed_Title.getText().toString();
                String Content = Et_EditFeed_Contents.getText().toString();

                db.collection("POSTS").document(FeedId)
                        .update("title", Title,
                                "content", Content)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("Edit_Feed_Success", "Success");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Edit_Feed_Failure", "Failure");
                            }
                        });
            }
        });



    }
}