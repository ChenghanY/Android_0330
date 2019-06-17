package com.checkin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.checkin.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class TripApplyActivity extends Activity {

    protected EditText startTime;
    protected Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_apply_activity);
        submitButton = findViewById(R.id.submit);
        startTime = findViewById(R.id.startTime);
        startTime.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘
        startTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    Calendar c = Calendar.getInstance();
                    new DatePickerDialog(TripApplyActivity.this, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // TODO Auto-generated method stub
                            startTime.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

                }
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(TripApplyActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        startTime.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(TripApplyActivity.this, "提交成功", Toast.LENGTH_SHORT );
                toast.setGravity(Gravity.TOP, 0, 50);
                toast.show();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // 停顿2秒后执行的动作
                        finish();
                    }
                }, 2 * 1000);
            }
        });
    }
}
