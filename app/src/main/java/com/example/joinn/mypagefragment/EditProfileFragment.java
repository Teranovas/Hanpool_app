package com.example.joinn.mypagefragment;



import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.joinn.R;
import com.example.joinn.communityfragment.RegisterFragment;
import com.example.joinn.sign.ImageActivity;
import com.example.joinn.sign.NicknameActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class EditProfileFragment extends Fragment {

    private ImageView ImgUser;

    private Button editBtn;

    private TextInputEditText NickText;

    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;

    private DatabaseReference usersRef;


    private FirebaseStorage mStorage;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int REQUEST_IMAGE_PICK = 2;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        mStorage = FirebaseStorage.getInstance();


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        String currentUserName = currentUser.getUid();
        usersRef.child(currentUserName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nickname = snapshot.child("닉네임").getValue(String.class);
                String imageURL = snapshot.child("photoUrl").getValue(String.class);

                Glide.with(getContext())
                        .load(imageURL)
                        .into(ImgUser);

                NickText.setText(nickname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        ImgUser = view.findViewById(R.id.imguser);
        editBtn = view.findViewById(R.id.editBtn);
        NickText = view.findViewById(R.id.nickArea);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        String currentUserName = currentUser.getUid();

        ImgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showImagePicker();

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }



            }
        });



        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newNickname = NickText.getText().toString().trim();
                DatabaseReference nicknameReference = FirebaseDatabase.getInstance().getReference("nicknames");
                String currentUserName = currentUser.getUid();

                // 새 닉네임과 현재 사용자의 닉네임 가져오기
                usersRef.child(currentUserName).child("닉네임").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String currentNickname = dataSnapshot.getValue(String.class);

                        if (dataSnapshot.hasChild(newNickname)) {

                            if (newNickname.equals(currentNickname)) {
                                // RegisterFragment로 전환
                                uploadImageToFirebase();
                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                transaction.replace(R.id.container, new MyPageFragment());
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }

                            else{
                                // 중복된 닉네임이 이미 존재하는 경우
                                Toast.makeText(getContext(), "이미 사용 중인 닉네임입니다.", Toast.LENGTH_SHORT).show();
                            }

                        }

                       else {
                            // 새 닉네임이 다를 경우, 기존 닉네임 삭제 후 새 닉네임 저장
                            nicknameReference.child(currentNickname).removeValue();
                            usersRef.child(currentUserName).child("닉네임").setValue(newNickname);
                            nicknameReference.child(newNickname).setValue(true);

                            uploadImageToFirebase();

                            // MyPageFragment로 전환
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.replace(R.id.container, new MyPageFragment());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // 에러 처리
                    }
                });
            }
        });




        usersRef.child(currentUserName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nickname = snapshot.child("닉네임").getValue(String.class);
                String imageURL = snapshot.child("photoUrl").getValue(String.class);

                Glide.with(getContext())
                        .load(imageURL)
                        .into(ImgUser);

                NickText.setText(nickname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용되었을 때 처리할 내용
            }
        }
    }

    private void showImagePicker() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());  //대화상자 객체를 생성하기 위한 빌더를 생성.
        builder.setTitle("이미지 선택");

        // 카메라로 사진 찍기
        builder.setPositiveButton("카메라", new DialogInterface.OnClickListener() {
            //다이얼로그의 버튼 중 "카메라" 버튼을 설정. 버튼 클릭 시, DialogInterface.OnClickListener의 onClick 메소드가 호출
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //카메라를 실행하기 위해 Intent를 생성,  MediaStore.ACTION_IMAGE_CAPTURE는 이미지를 캡처하기 위한 액션

                Context context1 = requireActivity();

                if(context1 != null){
                    PackageManager packageManager = context1.getPackageManager();

                    if (takePictureIntent.resolveActivity(packageManager) != null) {

                        //카메라 앱이 설치되어 있을 때, 카메라 앱을 실행하는 Intent를 실행
                        //getPackageManager()를 사용해 현재 앱의 PackageManager를 얻어와 Intent를 실행할 수 있는 Activity가 있는지 확인

                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        //카메라 앱에서 촬영한 이미지를 반환받아 onActivityResult 메소드에서 처리
                    }
                }


            }
        });
        builder.setNegativeButton("갤러리", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                //Intent.ACTION_PICK은 사용자가 이미지 선택 앱에서 이미지를 선택할 수 있도록 활성화
                //MediaStore.Images.Media.EXTERNAL_CONTENT_URI는 기기의 외부 저장소에 저장된 이미지를 나타내는 URI
                startActivityForResult(pickImageIntent, REQUEST_IMAGE_PICK);
                //startActivityForResult 메서드를 호출하여 액티비티를 시작하고 REQUEST_IMAGE_PICK 상수로 식별된 요청 코드를 전달
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // onActivityResult 내용
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode : 요청을 구분하기 위한 코드. startActivityForResult() 메서드를 호출할 때 인자로 전달한 코드와 같은 값이 됨.
        //resultCode : 요청에 대한 결과 코드. setResult() 메서드를 호출할 때 전달한 값이 됨.
        //data : 요청에 대한 결과 데이터

        // 카메라로 찍은 사진 결과 처리
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            //REQUEST_IMAGE_CAPTURE는 카메라로 찍은 사진 요청 코드를 의미. RESULT_OK는 액티비티가 성공적으로 종료되었음을 의미
            Bundle extras = data.getExtras(); //카메라로 찍은 사진의 정보를 Bundle 형태로 반환
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImgUser.setImageBitmap(imageBitmap);
            //Bundle에서 "data"라는 이름의 정보를 추출해 Bitmap 형태로 변환. 이렇게 변환된 이미지는 mProfileImageView에 표시
        }

        // 갤러리에서 선택한 사진 결과 처리
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData(); //갤러리에서 선택한 사진의 Uri를 가져옴.

            try { // 예외처리
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);
                //Uri로부터 Bitmap 객체를 생성
                //MediaStore.Images.Media.getBitmap() 메서드는 Uri에서 이미지를 로드하기 위해 사용
                ImgUser.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void uploadImageToFirebase(){
        if (ImgUser.getDrawable() != null) {

            String fileName = user.getUid() + ".jpg";
            // Firebase Storage에 업로드할 파일 이름을 생성

            StorageReference storageRef = mStorage.getReference().child("users").child(fileName);
            // Firebase Storage에 업로드할 파일 경로를 생성

            ImgUser.setDrawingCacheEnabled(true);
            ImgUser.buildDrawingCache();
            Bitmap bitmap = ImgUser.getDrawingCache();


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            // 이미지를 JPEG 포맷으로 압축

            UploadTask uploadTask = storageRef.putBytes(data);
            // Firebase Storage에 파일을 업로드
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // 업로드가 성공하면 이미지 다운로드 URL을 가져와서 저장
                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                    downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Firebase Database에 이미지 다운로드 URL을 저장
                            FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("photoUrl").setValue(uri.toString());
                            // 이미지 업로드 완료 메시지를 표시
                            Toast.makeText(getContext(), "이미지 업로드가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // 이미지 업로드 실패 메시지를 표시
                    Toast.makeText(getContext(), "이미지 업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // 이미지가 선택되지 않았을 때 메시지를 표시
            Toast.makeText(getContext(), "이미지를 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
    }


}