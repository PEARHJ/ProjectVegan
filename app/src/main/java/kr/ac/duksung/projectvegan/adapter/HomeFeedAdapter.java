package kr.ac.duksung.projectvegan.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import kr.ac.duksung.projectvegan.R;
import kr.ac.duksung.projectvegan.activity.CommentActivity;
import kr.ac.duksung.projectvegan.activity.EditFeedActivity;
import kr.ac.duksung.projectvegan.model.FeedInfo;
import kr.ac.duksung.projectvegan.model.UserProfile;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FeedInfo> FeedDataset;
    private String FeedId, FeedPublisher, FeedTitle, FeedContent, FeedUri;
    private Date FeedCreatedAt;

    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public Button Btn_HomeFeedComment, Btn_HomeFeedEtc;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;

            db = FirebaseFirestore.getInstance();

            // 클릭된 view 파악
//            int pos = getAdapterPosition();

            // 피드에서 댓글 아이콘 클릭시
            Btn_HomeFeedComment = view.findViewById(R.id.Btn_HomeFeedComment);
            Btn_HomeFeedComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        // 클릭한 view 알아내고 (pos) 해당 view의 FeedId값을 가져온다.
                        FeedId = FeedDataset.get(pos).getPostId();
                        Log.d("DOCUMENTID_Send", FeedId);
                        // CommentActivity로 FeedId 값 전달 -> 받아서 클릭한 Feed에 해당하는 댓글들만 보여준다.
                        Intent intent = new Intent(context, CommentActivity.class);
                        intent.putExtra("POSTSDocumentId", FeedId);
                        context.startActivity(intent);
                    }
                }
            });

            // 피드에서 more 버튼 클릭시
            Btn_HomeFeedEtc = view.findViewById(R.id.Btn_HomeFeedEtc);
            Btn_HomeFeedEtc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(), view);

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        FeedPublisher = FeedDataset.get(pos).getPublisher();
                        FeedId = FeedDataset.get(pos).getPostId();
                        if (firebaseUser.getUid().equals(FeedPublisher)) {
                            popupMenu.getMenuInflater().inflate(R.menu.myfeed_menu, popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    switch (menuItem.getItemId()) {
                                        case R.id.feed_edit:
                                            FeedId = FeedDataset.get(pos).getPostId();
                                            FeedTitle = FeedDataset.get(pos).getTitle();
                                            FeedContent = FeedDataset.get(pos).getContent();
                                            FeedCreatedAt = FeedDataset.get(pos).getCreatedAt();
                                            FeedPublisher = FeedDataset.get(pos).getPublisher();
                                            FeedUri = FeedDataset.get(pos).getUri();
                                            Log.d("EditInfo_Send", "Success");
                                            Intent intent = new Intent(context, EditFeedActivity.class);
                                            intent.putExtra("EditFeedId", FeedId);
                                            intent.putExtra("EditFeedTitle", FeedTitle);
                                            intent.putExtra("EditFeedContent", FeedContent);
                                            intent.putExtra("EditFeedUri", FeedUri);
                                            context.startActivity(intent);
                                            return true;
                                        case R.id.feed_delete:
                                            db.collection("POSTS").document(FeedId)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Log.d("Post Delete Success", "Success");
                                                        }
                                                    });
                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            });
                            popupMenu.show();
                        } else {
                            popupMenu.getMenuInflater().inflate(R.menu.feed_menu, popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    switch (menuItem.getItemId()) {
                                        case R.id.feed_report:

                                            return true;
                                        case R.id.feed_block:
                                            FeedPublisher = FeedDataset.get(pos).getPublisher();


                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            });
                            popupMenu.show();
                        }
                    }
                }
            });
        }
    }

    public HomeFeedAdapter(Context context, ArrayList<FeedInfo> FeedData) {
        FeedDataset = FeedData;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homefeed, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // USERS 컬렉션으로부터 정보 가져온다.
        db.collection("USERS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (firebaseUser != null) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("FeedAdapter Success", documentSnapshot.getId() + " => " + documentSnapshot.getData());

                                    if (documentSnapshot.getId().equals(FeedDataset.get(holder.getAdapterPosition()).getPublisher())) {
                                        TextView FeedPublisher = holder.cardView.findViewById(R.id.Tv_HomeFeed_Publisher);
                                        FeedPublisher.setText(documentSnapshot.getData().get("userId").toString());
                                    }
                                }
                            }
                        }
                    }
                });

        TextView FeedTitle = holder.cardView.findViewById(R.id.Tv_HomeFeed_Title);
        FeedTitle.setText(FeedDataset.get(position).getTitle());

        TextView FeedContent = holder.cardView.findViewById(R.id.Tv_HomeFeed_Content);
        FeedContent.setText(FeedDataset.get(position).getContent());

        TextView FeedCreatedAt = holder.cardView.findViewById(R.id.Tv_HomeFeed_CreatedAt);
        FeedCreatedAt.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(FeedDataset.get(position).getCreatedAt()));

        ImageView FeedImage = holder.cardView.findViewById(R.id.Iv_HomeFeed_Image);
        Glide.with(holder.cardView)
                .load(FeedDataset.get(position).getUri())
                .into(FeedImage);

    }

    @Override
    public int getItemCount() {
        return FeedDataset.size();
    }
}
