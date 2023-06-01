package kr.ac.duksung.projectvegan.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import kr.ac.duksung.projectvegan.R;
import kr.ac.duksung.projectvegan.activity.CommentActivity;
import kr.ac.duksung.projectvegan.model.FeedInfo;
import kr.ac.duksung.projectvegan.model.UserProfile;

public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FeedInfo> MyFeedDataset;
    private String MyFeedId;

    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;

    ImageView Iv_MyFeed_Image;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public Button Btn_MyFeedComment;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;

            Btn_MyFeedComment = view.findViewById(R.id.Btn_item_MyFeedComment);
            Btn_MyFeedComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        MyFeedId = MyFeedDataset.get(pos).getPostId();
                        Log.d("MyFeedDOCUMENTID_Send", MyFeedId);
                        Intent intent = new Intent(context, CommentActivity.class);
                        intent.putExtra("MyPOSTSDocumentId", MyFeedId);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public MyFeedAdapter(Context context, ArrayList<FeedInfo> MyFeedData) {
        MyFeedDataset = MyFeedData;
        this.context = context;
    }

    @NonNull
    @Override
    public MyFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myfeed, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CardView cardView = holder.cardView;

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Iv_MyFeed_Image = cardView.findViewById(R.id.Iv_MyFeed_item_Image);


        db.collection("USERS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (firebaseUser != null) {

                                String Uid = firebaseUser.getUid();
                                String User = MyFeedDataset.get(holder.getAdapterPosition()).getPublisher();

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("MyFeedAdapter Success", documentSnapshot.getId() + " => " + documentSnapshot.getData());

                                    if (documentSnapshot.getId().equals(User)) {
                                        if ( documentSnapshot.getId().equals(Uid)) {

                                            cardView.setVisibility(View.VISIBLE);
                                            ViewGroup.LayoutParams params = cardView.getLayoutParams();
                                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                            cardView.setLayoutParams(params);

                                            TextView titleTextView = cardView.findViewById(R.id.Tv_MyFeed_item_Title);
                                            titleTextView.setText(MyFeedDataset.get(holder.getAdapterPosition()).getTitle());

                                            TextView createdAtTextView = cardView.findViewById(R.id.Tv_MyFeed_item_CreatedAt);
                                            createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(MyFeedDataset.get(holder.getAdapterPosition()).getCreatedAt()));

                                            TextView contentsTextView =cardView.findViewById(R.id.Tv_MyFeed_item_Contents);
                                            contentsTextView.setText(MyFeedDataset.get(holder.getAdapterPosition()).getContent());

                                            String url = MyFeedDataset.get(holder.getAdapterPosition()).getUri();

                                            Glide.with(cardView).load(url).override(800, 800).into(Iv_MyFeed_Image);

                                        } else {
                                            cardView.setVisibility(View.GONE);
                                            ViewGroup.LayoutParams params = cardView.getLayoutParams();
                                            params.height = 0;
                                            params.width = 0;
                                            cardView.setLayoutParams(params);
                                        }
                                    }
                                }
                                Log.d("Error", "아니면 여기서?");
                            }
                            Log.d("Error", "여기서 안됨");
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return MyFeedDataset.size();
    }
}
