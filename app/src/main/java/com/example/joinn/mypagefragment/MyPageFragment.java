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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.example.joinn.communityfragment.RegisterFragment;
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
import java.util.ArrayList;
import java.util.List;


public class MyPageFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();

    PackageManager packageManager;
    private ImageView mProfileImageView;
    private TextView txtNickname, leveltxt;
    private DatabaseReference databaseRef;

    private ImageButton License;

    private ImageButton date;

    private ImageButton edit;

    // 현재 로그인한 사용자의 uid를 가져옵니다.
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private DatabaseReference usersRef;

    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;

    private String mNickname;
    private int mProfileImageResId;
    private Uri mImageUri;

    private ListView listView;
    private List<ListItem> items;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usersRef = FirebaseDatabase.getInstance().getReference().child("users");


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        String currentUserName = currentUser.getUid();

        items = new ArrayList<>();
        items.add(new ListItem(R.drawable.calendar, "카풀 일정", "등록된 카풀을 확인하세요"));
        items.add(new ListItem(R.drawable.history, "카풀 내역", "드라이버에 대한 평가 및 리뷰를 남겨주세요"));
        items.add(new ListItem(R.drawable.license, "드라이버 등록","드라이버가 되기 위해 인증하세요"));
        items.add(new ListItem(R.drawable.user2, "Text 4","Text 4"));
//        usersRef.child(currentUserName).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String nickname = snapshot.child("닉네임").getValue(String.class);
//                String imageURL = snapshot.child("photoUrl").getValue(String.class);
//                String level = snapshot.child("드라이버 레벨").getValue(String.class);
//
//                Glide.with(getContext()).load(imageURL).into(mProfileImageView);
//
//                txtNickname.setText(nickname);
//                leveltxt.setText(level);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
//        txtNickname = view.findViewById(R.id.txtNickname);
//        leveltxt = view.findViewById(R.id.leveltxt);
//
//        mProfileImageView = view.findViewById(R.id.imguser);
        packageManager = requireActivity().getPackageManager();

//        License = view.findViewById(R.id.license);
//        date = view.findViewById(R.id.date);
//        edit = view.findViewById(R.id.editProfile);

        listView = view.findViewById(R.id.listView);

        listView = view.findViewById(R.id.listView);

        CustomAdapter adapter = new CustomAdapter();
        listView.setAdapter(adapter);

//        ImageView logoutBtn = view.findViewById(R.id.logoutBtn);

        final Animation buttonClickAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.button_click_animation);

//        logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 구글 로그아웃 수행
//                signOutFromGoogle();
//            }
//        });


//        License.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                License.startAnimation(buttonClickAnimation);
//
//                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
//                usersRef.child(uid).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                        String position = snapshot.child("직위").getValue(String.class);
//
//
//                        if ("드라이버".equals(position)) { // 직위 "드라이버"인 경우만 추가
//
//                            Toast.makeText(getContext(), "드라이버 등록이 되어있습니다.", Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//
//                            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
//                            Fragment newFragment = new DriverRegistrationFragment();
//
//                            transaction.replace(R.id.container, newFragment);
//
//                            transaction.addToBackStack(null);
//                            transaction.commit();
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
//
//
//        date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                date.startAnimation(buttonClickAnimation);
//                Toast.makeText(getActivity(), "일정", Toast.LENGTH_LONG).show();
//
//
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
//                Fragment newFragment = new DateFragment();
//
//                transaction.replace(R.id.container, newFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });
//
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                edit.startAnimation(buttonClickAnimation);
//
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
//                Fragment newFragment = new EditProfileFragment();
//
//                transaction.replace(R.id.container, newFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });


        return view;

    }

//    private void signOutFromGoogle() {
//        // GoogleSignInClient 객체 생성
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        GoogleSignInClient signInClient = GoogleSignIn.getClient(requireContext(), gso);
//        // 로그아웃 수행
//        signInClient.signOut()
//                .addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Intent intent = new Intent(requireContext(), MainActivity.class);
//                        startActivity(intent);
//                        requireActivity().finish();  // 현재 액티비티 종료
//                    }
//                });
//    }



    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size(); // 아이템의 개수는 데이터 리스트의 크기와 동일합니다.
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item1, parent, false);
            }

            ImageView iconImageView = convertView.findViewById(R.id.icon);
            TextView itemTextView = convertView.findViewById(R.id.text);
            TextView itemTextView2 = convertView.findViewById(R.id.text2);

            ListItem item = items.get(position);
            iconImageView.setImageResource(item.iconResId);
            itemTextView.setText(item.text);
            itemTextView2.setText(item.text2);

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }
    }


}
