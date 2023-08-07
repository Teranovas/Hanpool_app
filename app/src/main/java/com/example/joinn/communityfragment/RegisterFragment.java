package com.example.joinn.communityfragment;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.joinn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterFragment extends Fragment {

    private EditText titleEditText;
    private EditText contentEditText;
    private Button submitButton;

    private DatabaseReference postsRef;
    private StorageReference storageRef;
    private DatabaseReference usersRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postsRef = FirebaseDatabase.getInstance().getReference().child("posts");
        storageRef = FirebaseStorage.getInstance().getReference();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        titleEditText = view.findViewById(R.id.title_et);
        contentEditText = view.findViewById(R.id.content_et1);

        submitButton = view.findViewById(R.id.reg_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String content = contentEditText.getText().toString();

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(getContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    String postId = postsRef.push().getKey();

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String writer = snapshot.child("닉네임").getValue(String.class);
                                String imageUrl = snapshot.child("photoUrl").getValue(String.class);

                                // 이미지 업로드 로직 추가
                                // 이미지 업로드를 위한 파일 경로 생성
                                StorageReference imageRef = storageRef.child("images/" + postId + ".jpg");
                                // 이미지를 업로드하고 업로드한 파일의 다운로드 URL 가져오기
                                // 이 부분은 실제로 이미지 파일을 업로드하고 URL을 가져오는 방식에 맞게 수정해야 합니다.
                                // 여기서는 가상의 URL("https://example.com/image.jpg")로 가정합니다.
                                // String imageUrl = "https://example.com/image.jpg";

                                long timestamp = System.currentTimeMillis();
                                Post post = new Post(postId, title, content, writer, imageUrl, timestamp);
                                postsRef.child(postId).setValue(post);
                                Toast.makeText(getContext(), "게시물이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                // CommunityFragment로 이동
                                CommunityFragment communityFragment = new CommunityFragment();
                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                transaction.replace(R.id.container, communityFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "Failed to read writer value.", error.toException());
                        }
                    });
                }
            }
        });


        return view;
    }
}