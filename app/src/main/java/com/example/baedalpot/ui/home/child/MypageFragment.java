package com.example.baedalpot.ui.home.child;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.baedalpot.R;
import com.example.baedalpot.model.User;
import com.example.baedalpot.ui.auth.LoginActivity;
import com.example.baedalpot.ui.home.NaviActivity;
import com.example.baedalpot.ui.writepost.WritePostActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MypageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MypageFragment extends Fragment implements View.OnClickListener {
    Button logout;
    Button delUser;
    TextView tvUser_name, tvEmail, tvAccNum, tvReport_stack;



    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MypageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MypageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MypageFragment newInstance(String param1, String param2) {
        MypageFragment fragment = new MypageFragment();
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("UserAccount").child(currentUser.getUid());
        reference.addValueEventListener
                (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            tvUser_name.setText("아이디: " + user.getId());
                            Log.e("이름",user.id);
                            tvEmail.setText("이메일: " + user.email);
                            Log.e("이름",user.email);
                            tvAccNum.setText("계좌번호: " + user.accNum);
                            Log.e("이름",user.accNum);
                            tvReport_stack.setText("신고 스택:" + user.numReport);
                            Log.e("이름", String.valueOf(user.numReport));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_mypage, container, false);

        tvUser_name = myView.findViewById(R.id.user_name);
        tvEmail = myView.findViewById(R.id.user_email);
        tvAccNum = myView.findViewById(R.id.account_number);
        tvReport_stack = myView.findViewById(R.id.report_stack);

        logout = (Button) myView.findViewById(R.id.btnLogout);
        logout.setOnClickListener(this);

        delUser = (Button) myView.findViewById(R.id.delUser);
        delUser.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(requireContext(), LoginActivity.class));
                break;
            case R.id.delUser: //삭제구문 수정 필요
                currentUser.delete(); //authentication 에서만 삭제, realtime database에서는 삭제 x
                startActivity(new Intent(requireContext(), LoginActivity.class));
                break;
        }


    }




}