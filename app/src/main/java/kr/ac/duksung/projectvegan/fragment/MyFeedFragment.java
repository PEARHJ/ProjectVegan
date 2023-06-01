package kr.ac.duksung.projectvegan.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;

import kr.ac.duksung.projectvegan.R;
import kr.ac.duksung.projectvegan.adapter.MyFeedAdapter;
import kr.ac.duksung.projectvegan.model.FeedInfo;
import kr.ac.duksung.projectvegan.model.UserProfile;

public class MyFeedFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    TextView Tv_MyFeed_UerId, Tv_MyFeed_VeganType, Tv_MyFeed_Allergy, Tv_Notice;
    ImageView Iv_MyFeedProfile;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore db;

    public MyFeedFragment() {
        // Required empty public constructor
    }

    public static MyFeedFragment newInstance(String param1, String param2) {
        MyFeedFragment fragment = new MyFeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static MyFeedFragment newInstance() {
        return new MyFeedFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_feed, container, false);

        Tv_MyFeed_UerId = view.findViewById(R.id.Tv_MyFeed_UerId);
        Tv_MyFeed_VeganType = view.findViewById(R.id.Tv_MyFeed_VeganType);
        Tv_MyFeed_Allergy = view.findViewById(R.id.Tv_MyFeed_Allergy);
        Tv_Notice = view.findViewById(R.id.Tv_Notice);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        db.collection("USERS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (firebaseUser != null) {
                                String Uid = firebaseUser.getUid();

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("MyFeed Success", documentSnapshot.getId() + " ==> " + documentSnapshot.getData());

                                    if (documentSnapshot.getId().equals(Uid)) {
                                        Tv_MyFeed_UerId.setText(documentSnapshot.getData().get("userId").toString());
                                        Tv_MyFeed_VeganType.setText(documentSnapshot.getData().get("userVeganType").toString());
                                        Tv_MyFeed_Allergy.setText(documentSnapshot.getData().get("userAllergy").toString());
                                    }
                                }
                            } else {
                                Log.d("MyFeed error", "Error");
                            }
                        }
                    }
                });

        db.collection("POSTS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<FeedInfo> MyFeedList = new ArrayList<>();

                            if (firebaseUser != null) {

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("MyFeed Success", documentSnapshot.getId() + " ==> " + documentSnapshot.getData());

                                    MyFeedList.add(new FeedInfo(
                                            documentSnapshot.getData().get("title").toString(),
                                            documentSnapshot.getData().get("content").toString(),
                                            documentSnapshot.getData().get("publisher").toString(),
                                            documentSnapshot.getId(),
                                            documentSnapshot.getData().get("uri").toString(),
                                            new Date(documentSnapshot.getDate("createdAt").getTime())));

                                }

                                Tv_Notice.setVisibility(View.GONE);

                                RecyclerView recyclerView = view.findViewById(R.id.Rv_MyFeed);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                                RecyclerView.Adapter mAdapter = new MyFeedAdapter(getActivity(), MyFeedList);
                                recyclerView.setAdapter(mAdapter);
                            }

                            Tv_Notice.setVisibility(View.VISIBLE);

                        } else {
                            Log.d("MyFeed error", "Error");
                        }
                    }
                });

        return view;
    }
}