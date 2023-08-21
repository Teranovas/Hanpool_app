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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.joinn.R;
import com.example.joinn.mapfragment.AddSearchFragment;
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

    private Button afterButton;

    private Button beforeButton;

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
        afterButton = view.findViewById(R.id.after_Btn);
        beforeButton = view.findViewById(R.id.before_Btn);





        beforeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();


                if (title.isEmpty()) {
                    Toast.makeText(getContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    String postId = postsRef.push().getKey();

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    // 데이터 전달을 위한 Bundle 생성
                    Bundle bundle = new Bundle();
                    bundle.putString("postId", postId);
                    bundle.putString("userId", userId);
                    bundle.putString("title", title);

                    // 출발지 정보 전달
                    Fragment newFragment = new StartRegisterFragment();
                    newFragment.setArguments(bundle);

                    // 프래그먼트 전환
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });



        afterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();


                if (title.isEmpty()) {
                    Toast.makeText(getContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    String postId = postsRef.push().getKey();

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    // 데이터 전달을 위한 Bundle 생성
                    Bundle bundle = new Bundle();
                    bundle.putString("postId", postId);
                    bundle.putString("userId", userId);
                    bundle.putString("title", title);

                    // 출발지 정보 전달
                    Fragment newFragment = new ArriveRegisterFragment();
                    newFragment.setArguments(bundle);

                    // 프래그먼트 전환
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        return view;
    }
}