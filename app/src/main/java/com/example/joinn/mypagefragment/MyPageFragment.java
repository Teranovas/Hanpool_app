package com.example.joinn.mypagefragment;


import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
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

    private ImageButton edit;




    private DatabaseReference usersRef;

    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;


    private TextView email;

    private RecyclerView listView;
    private List<ListItem> items;

    private TextView spotTxt;

    private TextView scoreTxt;
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
        items.add(new ListItem(R.drawable.license, "드라이버 등록", "드라이버가 되기 위해 인증하세요"));
        items.add(new ListItem(R.drawable.logout, "로그 아웃", "회원 정보를 닫고 로그인창으로 넘어갑니다"));
        usersRef.child(currentUserName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nickname = snapshot.child("닉네임").getValue(String.class);
                String imageURL = snapshot.child("photoUrl").getValue(String.class);
                String level = snapshot.child("드라이버 레벨").getValue(String.class);
                String spottxt = snapshot.child("직위").getValue(String.class);
                String score = snapshot.child("평점").getValue(String.class);


                String userEmail = currentUser.getEmail();
                Glide.with(getContext()).load(imageURL).into(mProfileImageView);

                txtNickname.setText(nickname);
                leveltxt.setText(level);
                spotTxt.setText(spottxt);

                email.setText(userEmail);
                scoreTxt.setText(score);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        txtNickname = view.findViewById(R.id.txtNickname);
        leveltxt = view.findViewById(R.id.leveltxt);

        mProfileImageView = view.findViewById(R.id.imguser);
        packageManager = requireActivity().getPackageManager();

        edit = view.findViewById(R.id.edit);

        listView = view.findViewById(R.id.listView);

        listView = view.findViewById(R.id.listView);


        email = view.findViewById(R.id.email);
        spotTxt = view.findViewById(R.id.spotTxt);

        scoreTxt = view.findViewById(R.id.scoreTxt);

        CustomRecyclerViewAdapter adapter = new CustomRecyclerViewAdapter(items);

        final Animation buttonClickAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.button_click_animation);
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClicked(int position, ListItem item) {
                switch (position) {
                    case 0:
                        // 카풀 일정 관련 동작

                        Toast.makeText(getActivity(), "일정", Toast.LENGTH_LONG).show();


                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                        Fragment newFragment = new DateFragment();

                        transaction.replace(R.id.container, newFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case 1:
                        // 카풀 내역 관련 동작
                        Toast.makeText(getActivity(), "내역", Toast.LENGTH_LONG).show();


                        FragmentTransaction transaction1 = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction1.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                        Fragment newFragment1 = new ReviewFragment();

                        transaction1.replace(R.id.container, newFragment1);
                        transaction1.addToBackStack(null);
                        transaction1.commit();
                        break;
                    case 2:
                        // 드라이버 등록 관련 동작
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                        usersRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String position = snapshot.child("직위").getValue(String.class);


                                if ("드라이버".equals(position)) { // 직위 "드라이버"인 경우만 추가

                                    Toast.makeText(getContext(), "드라이버 등록이 되어있습니다.", Toast.LENGTH_SHORT).show();

                                } else {
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                                    Fragment newFragment = new DriverRegistrationFragment();

                                    transaction.replace(R.id.container, newFragment);

                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        break;
                    case 3:
                        // 구글 로그아웃 수행
                        signOutFromGoogle();
                        break;
                }
            }
        });
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(adapter);



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.startAnimation(buttonClickAnimation);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                Fragment newFragment = new EditProfileFragment();

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

    public interface ItemClickListener {
        void onItemClicked(int position, ListItem item);
    }


    public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {

        private List<ListItem> items;
        private ItemClickListener itemClickListener;

        public CustomRecyclerViewAdapter(List<ListItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ListItem item = items.get(position);
            holder.iconImageView.setImageResource(item.iconResId);
            holder.itemTextView.setText(item.text);
            holder.itemTextView2.setText(item.text2);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void setItemClickListener(ItemClickListener listener) {
            this.itemClickListener = listener;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView iconImageView;
            TextView itemTextView, itemTextView2;

            ViewHolder(View itemView) {
                super(itemView);
                iconImageView = itemView.findViewById(R.id.icon);
                itemTextView = itemView.findViewById(R.id.text);
                itemTextView2 = itemView.findViewById(R.id.text2);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickListener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                itemClickListener.onItemClicked(position, items.get(position));
                            }
                        }

                    }
                });
            }
        }
    }

}
