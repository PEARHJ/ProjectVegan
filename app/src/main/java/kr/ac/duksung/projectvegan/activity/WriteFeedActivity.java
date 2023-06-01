package kr.ac.duksung.projectvegan.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.ac.duksung.projectvegan.R;
import kr.ac.duksung.projectvegan.fragment.HomeFeedFragment;
import kr.ac.duksung.projectvegan.model.FeedInfo;

public class WriteFeedActivity extends AppCompatActivity {

    final int CAMERA = 100;
    final int GALLERY = 101;
    int imgFrom; // 이미지 어디서 가져왔는지 -> 카메라, 갤러리
    String imagePath = "";
    SimpleDateFormat imageDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
    File imageFile = null; // 카메라 선택 시 새로 생성하는 파일 객체
    Uri imageUri = null;

    private Button Btn_UploadFeed;
    private EditText Et_Feed_Title, Et_Feed_Contents;
    private ImageView Iv_Add_FeedPhoto;
    private Dialog dialog;

    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore db;


    private Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_feed);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        Et_Feed_Title = findViewById(R.id.Et_Feed_Title);
        Et_Feed_Contents = findViewById(R.id.Et_Feed_Contents);
        Iv_Add_FeedPhoto = findViewById(R.id.Iv_Add_FeedPhoto);
        Btn_UploadFeed = findViewById(R.id.Btn_UploadFeed);

        // 이미지 추가 버튼 클릭시
        Iv_Add_FeedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 팝업 창 띄우기
                dialog = new Dialog(WriteFeedActivity.this);
                dialog.setContentView(R.layout.dialog);
                dialog.show();

                // 팝업창 -> 카메라 선택시
                Button Btn_Camera = dialog.findViewById(R.id.Btn_Camera);
                Btn_Camera.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // 팝업창 내리기
                        dialog.dismiss();
                        // 권한설정
                        boolean hasCamPerm = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                        if (!hasCamPerm) {
                            ActivityCompat.requestPermissions(WriteFeedActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        if (intent.resolveActivity(getPackageManager()) != null) {
                            try {
                                imageFile = createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (imageFile != null) {
                                Uri imageUri = FileProvider.getUriForFile(getApplicationContext(),
                                        "kr.ac.duksung.projectvegan.fileprovider", imageFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, CAMERA);
                            }
                        }
                    }
                });

                // 팝업창 -> 갤러리 선택시
                Button Btn_Gallery = dialog.findViewById(R.id.Btn_Gallery);
                Btn_Gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 팝업창 내리기
                        dialog.dismiss();
                        // 권한 설정
                        boolean hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                        if (!hasWritePerm) {  // 권한 없을 시  권한설정 요청
                            ActivityCompat.requestPermissions(WriteFeedActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                        intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        intent.setType("image/*");
                        startActivityForResult(intent, GALLERY);
                    }
                });

            }
        });

        // 공유 버튼 클릭시
        Btn_UploadFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // imgFrom >= 100 -> 이미지가 카메라 혹은 갤러리에서 가져온것이 맞고, imagePath.length() > 0 -> 이미지 경로 값이 존재할때
                // 즉, 카메라나 갤러리로 부터 가져온 이미지가 존재할때
                if (imagePath.length() > 0 && imgFrom >= 100) {
                    // 게시물 업로드 함수 실행
                    uploader();
                    Toast.makeText(WriteFeedActivity.this, "게시물이 업로드 되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) { // 결과 있을 경우
            if (requestCode == GALLERY) { // 갤러리 선택한 경우
                imageUri = data.getData(); // 이미지 Uri 정보
                imagePath = data.getDataString(); // 이미지 위치 경로 정보
            } // 카메라 선택한 경우는 아래 createImageFile에서 imageFile 생성해주기 때문에 여기선 불필요
            if (imagePath.length() > 0) { // 저장한 파일 경로를 Glide 사용해 Iv에 세팅
                Glide.with(this)
                        .load(imagePath)
                        .into(Iv_Add_FeedPhoto);
                imgFrom = requestCode;
            }
        }
    }

    // 카메라랑 연결되는 이미지 파일 생성
    File createImageFile() throws IOException {
        String timeStamp = imageDate.format(new Date());
        String fileName = "IMAGE_" + timeStamp; // 이미지 파일 명
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(fileName, ".jpg", storageDir); // 파일 생성
        imagePath = file.getAbsolutePath(); // 파일 절대경로 저장
        return file;
    }

    // 게시물 공유 버튼 클릭시 발생하는 이벤트
    void uploader() {
        UploadTask uploadTask = null; // 파일 업로드하는 객체
        switch (imgFrom) {
            case GALLERY: // 갤러리에서 가져온 경우
                String timeStamp = imageDate.format(new Date());
                String imageFileName = "IMAGE_" + timeStamp + "_.png"; // 새로운 파일명 생성 후
                storageReference = firebaseStorage.getReference().child("post").child(imageFileName); // reference에 경로 세팅
                uploadTask = storageReference.putFile(imageUri); // 업로드할 파일과 위치 설정
                break;

                case CAMERA: // 카메라에서 가져온 경우 createImage에서 생성한 이미지 우일명 이용
                    storageReference = firebaseStorage.getReference().child("post").child(imageFile.getName()); // reference에 경로 세팅
                    uploadTask = storageReference.putFile(Uri.fromFile(imageFile));
                    break;

        }

        // 파일 업로드
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Upload", "success");
                downloadUri(); // 업로드 성공시 업로드한 파일 Uri 다운받기
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Upload", "Failure");
            }
        });
    }

    // 지정한 경로 (reference)에 대한 uri 다운
    void downloadUri() {
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("DownloadUri", "success");
                Log.d("DownloadUri", "uri = " + uri);
                postFeed(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DownloadUri", "Failure");
            }
        });
    }

    private void postFeed(Uri uri) {
        final String title = Et_Feed_Title.getText().toString();
        final String contents = Et_Feed_Contents.getText().toString();

        if (title.length() > 0 && contents.length() > 0) {

            DocumentReference documentReference = db.collection("POSTS").document();

            FeedInfo feedInfo = new FeedInfo(title, contents, firebaseUser.getUid(), documentReference.getId(), uri.toString(), new Date());

            documentReference.set(feedInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        } else {
            Toast.makeText(this, "게시글 내용을 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}