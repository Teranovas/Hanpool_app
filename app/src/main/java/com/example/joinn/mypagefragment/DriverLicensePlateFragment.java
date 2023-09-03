package com.example.joinn.mypagefragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.joinn.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class DriverLicensePlateFragment extends Fragment {

    private FirebaseAuth firebaseAuth;

    private Button nextBtn;

    private Context context;

    private EditText licensePlateText;

    private Button licenseplateBtn;

    private DatabaseReference databaseRef;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_license_plate, container, false);

        Button nextBtn = view.findViewById(R.id.nextBtn);

        Button licenseBtn = view.findViewById(R.id.licensePlateBtn);

        EditText licensePlateText = view.findViewById(R.id.licensePlateText);

        firebaseAuth = FirebaseAuth.getInstance(); //이 구문은 현재 Firebase 인증 서비스의 인스턴스를 가져옴.

        context = container.getContext();

        /** "차량번호 저장" 버튼을 누르면 차량 번호를 firebase 유저의 차량번호에 저장 */
        licenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String licensePlate = licensePlateText.getText().toString().trim();
                FirebaseUser user = firebaseAuth.getCurrentUser();  // 현재 로그인한 사용자 정보 firebaseAuth이용하여 가져옴.
                String uid = user.getUid();

                HashMap<Object, String> hashMap = new HashMap<>();
                // 이전 Activity에서 전달된 데이터를 HashMap 형태로 가져옴

                hashMap.put("uid", uid);
                hashMap.put("차량번호", licensePlate);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                // Firebase 데이터베이스 인스턴스를 가져옴.
                DatabaseReference reference = database.getReference("license");
                // DatabaseReference 객체를 가져와 "users"라는 레퍼런스를 가진 노드를 참조

                // Firebase 데이터베이스에 현재 사용자의 uid에 hashMap 저장
                reference.child(uid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "업로드 완료", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "업로드 실패", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        /** "다음" 버튼을 누르면 차량 번호를 입력하는 페이지로 넘어감 */
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment newFragment = new DriverCarImgFragment();

                transaction.replace(R.id.container, newFragment);

                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
