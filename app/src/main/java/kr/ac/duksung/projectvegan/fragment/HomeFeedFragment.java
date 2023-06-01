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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import kr.ac.duksung.projectvegan.R;
import kr.ac.duksung.projectvegan.adapter.HomeFeedAdapter;
import kr.ac.duksung.projectvegan.model.FeedInfo;

public class HomeFeedFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db;

    public HomeFeedFragment() {
        // Required empty public constructor
    }

    public static HomeFeedFragment newInstance(String param1, String param2) {
        HomeFeedFragment fragment = new HomeFeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        View view = inflater.inflate(R.layout.fragment_home_feed, container, false);

        db = FirebaseFirestore.getInstance();

        // POSTS 컬렉션으로부터 정보 얻어오기
        db.collection("POSTS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<FeedInfo> FeedList = new ArrayList<>();

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d("Feed Success", documentSnapshot.getId() + " ==> " + documentSnapshot.getData());
                                FeedList.add(new FeedInfo(
                                        documentSnapshot.getData().get("title").toString(),
                                        documentSnapshot.getData().get("content").toString(),
                                        documentSnapshot.getData().get("publisher").toString(),
                                        documentSnapshot.getId(),
                                        documentSnapshot.getData().get("uri").toString(),
                                        new Date(documentSnapshot.getDate("createdAt").getTime())
                                ));
                            }
                            RecyclerView recyclerView = view.findViewById(R.id.Rv_HomeFeed);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                            RecyclerView.Adapter mAdapter = new HomeFeedAdapter(getActivity(), FeedList);
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            Log.d("Feed Error", "Error getting documents", task.getException());
                        }
                    }
                });
        return view;
    }
}