package com.example.joinn.mypagefragment;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.storage.FirebaseStorage;

import com.bumptech.glide.Glide;
import com.example.joinn.R;
import com.example.joinn.sign.ImageActivity;
import com.example.joinn.sign.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import android.content.DialogInterface;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


public class MyPageFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();

    PackageManager packageManager;
    private ImageView mProfileImageView;
    private TextView txtNickname, leveltxt;
    private DatabaseReference databaseRef;

    private ImageView License;

    private ImageView date;

    // 현재 로그인한 사용자의 uid를 가져옵니다.
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private String mNickname;
    private int mProfileImageResId;
    private Uri mImageUri;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        txtNickname = view.findViewById(R.id.txtNickname);
        leveltxt = view.findViewById(R.id.leveltxt);

        mProfileImageView = view.findViewById(R.id.imguser);
        packageManager = requireActivity().getPackageManager();

        License = view.findViewById(R.id.license);
        date = view.findViewById(R.id.date);
        Button logoutBtn = view.findViewById(R.id.logoutBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 구글 로그아웃 수행
                signOutFromGoogle();
            }
        });
        mProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePicker();
            }
        });


        // 파이어베이스의 realtime database를 참조합니다.
        databaseRef = FirebaseDatabase.getInstance().getReference();

        // realtime database에서 현재 사용자의 드라이버 레벨을 가져와서 leveltxt에 설정합니다.
        databaseRef.child("users").child(uid).child("드라이버 레벨").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String level = snapshot.getValue(String.class);
                leveltxt.setText(level);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 데이터 로드에 실패했을 경우 처리할 내용을 여기에 작성합니다.
            }
        });

        // 현재 사용자의 닉네임을 가져와서 txtNickname에 설정합니다.
        databaseRef.child("users").child(uid).child("닉네임").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nickname = snapshot.getValue(String.class);
                txtNickname.setText(nickname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 데이터 로드에 실패했을 경우 처리할 내용을 여기에 작성합니다.
            }
        });

        // 현재 사용자의 프로필 이미지를 가져와서 imguser에 설정합니다.
        databaseRef.child("users").child(uid).child("photoUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String photoUrl = snapshot.getValue(String.class);

                if (photoUrl != null) {
                    // Glide를 사용하여 이미지를 불러옵니다.
                    Glide.with(getContext()).load(photoUrl).into(mProfileImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 데이터 로드에 실패했을 경우 처리할 내용을 여기에 작성합니다.
            }
        });
        License.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "성공", Toast.LENGTH_LONG).show();

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment newFragment = new DriverRegistrationFragment();

                transaction.replace(R.id.container, newFragment);

                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"일정", Toast.LENGTH_LONG).show();

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment newFragment = new DateFragment();

                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;

    }

    private void signOutFromGoogle() {
        // GoogleSignInClient 객체 생성
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient signInClient = GoogleSignIn.getClient(requireContext(), gso);
        // 로그아웃 수행
        signInClient.signOut()
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();  // 현재 액티비티 종료
                    }
                });
    }

    private void showImagePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("이미지 선택");

        // 카메라로 사진 찍기
        builder.setPositiveButton("카메라", new DialogInterface.OnClickListener() {
            //다이얼로그의 버튼 중 "카메라" 버튼을 설정. 버튼 클릭 시, DialogInterface.OnClickListener의 onClick 메소드가 호출
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //카메라를 실행하기 위해 Intent를 생성,  MediaStore.ACTION_IMAGE_CAPTURE는 이미지를 캡처하기 위한 액션

                if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    //카메라 앱이 설치되어 있을 때, 카메라 앱을 실행하는 Intent를 실행
                    //getPackageManager()를 사용해 현재 앱의 PackageManager를 얻어와 Intent를 실행할 수 있는 Activity가 있는지 확인
                    if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                    //카메라 앱에서 촬영한 이미지를 반환받아 onActivityResult 메소드에서 처리
                }
            }
        });

        builder.setNegativeButton("갤러리", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImageIntent, REQUEST_IMAGE_PICK);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void reuploadImageToFirebase() {
        if (mProfileImageView.getDrawable() != null) {
            String fileName = uid + ".jpg";
            StorageReference storageRef = mStorage.getReference().child("users").child(fileName);
            mProfileImageView.setDrawingCacheEnabled(true);
            mProfileImageView.buildDrawingCache();
            Bitmap bitmap = mProfileImageView.getDrawingCache();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = storageRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                    downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("photoUrl").setValue(uri.toString());
                            Toast.makeText(getContext(), "이미지 업로드가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "이미지 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mProfileImageView.setImageBitmap(imageBitmap);
                reuploadImageToFirebase();
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri imageUri = data.getData();
                mProfileImageView.setImageURI(imageUri);
                reuploadImageToFirebase();
            }
        }
    }
}
