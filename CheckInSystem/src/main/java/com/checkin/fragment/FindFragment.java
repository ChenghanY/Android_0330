package com.checkin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.checkin.LeaveApplyActivity;
import com.checkin.SignActivity;
import com.checkin.R;
import com.checkin.TripApplyActivity;


public class FindFragment extends Fragment {

    protected Button signButton;
    protected Button leaveApplyButton;
    protected Button tripApplyButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        // 千万别改错了 container不要删错了
        return inflater.inflate(R.layout.fragment_find,container,false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        signButton= (Button) getView().findViewById(R.id.findSign);
        leaveApplyButton=(Button)getView().findViewById(R.id.findLeaveApply);
        tripApplyButton=(Button)getView().findViewById(R.id.findTripApply);

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign = new Intent(getActivity(), SignActivity.class);
                startActivity(sign);
            }
        });
        leaveApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent leaveApplyIntent = new Intent(getActivity(), LeaveApplyActivity.class);
                startActivity(leaveApplyIntent);
            }
        });
        tripApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tripApplyIntent = new Intent(getActivity(), TripApplyActivity.class);
                startActivity(tripApplyIntent);
            }
        });

    }
}
