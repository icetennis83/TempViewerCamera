package com.example.tempviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.net.HttpHeaders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainAC";
    /* access modifiers changed from: private */
    public AlphaAnimation Dash = new AlphaAnimation(1.0f, 0.5f);
    Integer PointInt;
    long START_TIME_IN_MILLIS = 0;
    Button b1;
    Button b2;
    Button buttontree;
    CountDownTimer cdt;
    /* access modifiers changed from: private */
    public Handler customHandler = new Handler();
    String dastatus;
    FirebaseFirestore fStore;
    TextView idname;
    TextView idtext1;
    TextView idtext2;
    TextView idtext3;
    TextView idtext4;
    TextView idtext5;
    TextView idtext6;
    TextView idtext7;
    CountDownTimer mCountDownTimer;
    long mTimeLeftInMillis = 0;
    Integer minutes;
    Integer numberInt;
    String numberString;
    String powerBoard;
    String pumpmainstring;
    Integer secound;
    TextView showPoint;
    /* access modifiers changed from: private */
    public long startTime = 0;
    String strTime;
    long timeInMilliseconds = 0;
    long timeSwapBuff = 0;
    Integer timemain = Integer.valueOf(0);
    Integer timemain2;
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            MainActivity activity_home = MainActivity.this;
            activity_home.updatedTime = activity_home.timeSwapBuff + timeInMilliseconds;
            int secs = (int) (timeInMilliseconds / 1000);
            int mins = secs / 60;
            int secs2 = secs % 60;
            int hours = mins / 60;
            int mins2 = mins % 60;
            StringBuilder sb = new StringBuilder();
            sb.append("");
            String str = "%02d";
            sb.append(String.format(str, new Object[]{Integer.valueOf(hours)}));
            String str2 = ":";
            sb.append(str2);
            sb.append(String.format(str, new Object[]{Integer.valueOf(mins2)}));
            sb.append(str2);
            sb.append(String.format(str, new Object[]{Integer.valueOf(secs2)}));
            idtext4.setText(sb.toString());
            customHandler.postDelayed(this, 1000);
        }
    };
    long updatedTime = 0;
    String userID;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        this.fStore = FirebaseFirestore.getInstance();
        this.idname = (TextView) findViewById(R.id.idname);
        this.idtext2 = (TextView) findViewById(R.id.text2);
        //this.idtext4 = (TextView) findViewById(R.id.text4);
        this.idtext5 = (TextView) findViewById(R.id.text5);
        this.idtext6 = (TextView) findViewById(R.id.text6);
        //this.idtext7 = (TextView) findViewById(R.id.text7);
        //this.showPoint = (TextView) findViewById(R.id.showPoint);
        //this.buttontree = (Button) findViewById(R.id.buttonpumponandoff);
        //Updatestat();

        //DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("temperture");
        DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference("temperture");
        this.idtext6.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date()));
        DatabaseReference pumpstastus1 = myRef2.child("pumpMain").child("pump1");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                String temp = String.valueOf(map.get("Temperature"));
                String humid = String.valueOf(map.get("Humidity"));
                //String status2 = String.valueOf(map.get("STATUS"));
                //dastatus = status;
                idname.setText(temp);
                idname.startAnimation(Dash);
                idtext2.setText(humid);
                idtext2.startAnimation(Dash);
                //updaterealtime();
                //powerBoard = status2;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
        pumpstastus1.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            public void onCancelled(DatabaseError databaseError) {
                Log.w("file", "Failed to read value.", databaseError.toException());
            }
        });

//        this.buttontree.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                openwater();
//            }
//        });
    }

    /* access modifiers changed from: private */
    public void updaterealtime() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put("Timestampsec", Long.valueOf(this.mTimeLeftInMillis));
        myRef.updateChildren(updateStatus);
    }

    /* access modifiers changed from: private */
    public void openwater() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> updateStatus = new HashMap<>();
        if (this.dastatus.equals("ON")) {
            this.buttontree.setText("กดเพื่อปิดน้ำ");
            PauseTimer();
            resetTimer();
            Stopwater();
            StopTimer();

            updateStatus.put("Timestampsec",5000);
            myRef.updateChildren(updateStatus);


        } else if (dastatus.equals("OFF")){
            //this.buttontree.setText("กดเพื่อเปิดน้ำ");
            //Startwater();
            //StartTimer();
            //plustimer();

        }
        myRef.updateChildren(updateStatus);
        String str = "Point";
        this.fStore.collection("users").document(this.userID).update(str, (Object) Integer.valueOf(this.PointInt.intValue() + 10), new Object[0]);
    }

    /* access modifiers changed from: private */
    public void startTimer2() {
        CountDownTimer r0 = new CountDownTimer(this.mTimeLeftInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                Startwater();
                StartTimer();
                plustimer();
            }

            public void onFinish() {
                Toast.makeText(getBaseContext(), "Bello", Toast.LENGTH_SHORT).show();
                //buttontree.setText("กดเพื่อเปิดน้ำ");
                mTimeLeftInMillis = 0;
                StopTimer();
                Stopwater();
            }
        };
        this.mCountDownTimer = r0.start();
    }

    /* access modifiers changed from: private */
    public void Startwater() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put("statuspump", "ON");
        myRef.updateChildren(updateStatus);
        this.buttontree.setText("กดเพื่อปิดน้ำ");
    }

    /* access modifiers changed from: private */
    public void Stopwater() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put("statuspump", "OFF");
        myRef.updateChildren(updateStatus);
        //this.buttontree.setText("กดเพื่อเปิดน้ำ");
    }

    /* access modifiers changed from: private */
    public void updateCountDownText() {
        this.minutes = Integer.valueOf(((int) (this.mTimeLeftInMillis / 1000)) / 60);
        this.secound = Integer.valueOf(((int) (this.mTimeLeftInMillis / 1000)) % 60);
        this.idtext7.setText(String.format(Locale.getDefault(), "%02d:%02d", new Object[]{this.minutes, this.secound}));
    }

    /* access modifiers changed from: private */
    public void PauseTimer() {
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void resetTimer() {
        this.mTimeLeftInMillis = 0;
        updateCountDownText();
    }


    public void StartTimer() {
        this.startTime = SystemClock.uptimeMillis();
        this.customHandler.postDelayed(this.updateTimerThread, 0);
    }

    public void StopTimer() {
        this.timeSwapBuff += this.timeInMilliseconds;
        this.customHandler.removeCallbacks(this.updateTimerThread);
    }

    private void Updatestat() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = database.getReference("STATUS");
        DatabaseReference myRef3 = database.getReference("statusnumber");
        myRef2.setValue("off");
        myRef3.setValue(Integer.valueOf(1000));
        //this.buttontree.setVisibility(View.INVISIBLE);
    }

    public void plustimer() {
        CountDownTimer r0 = new CountDownTimer((long) this.timemain.intValue(), 10) {
            public void onTick(long millisUntilFinished) {
                strTime = String.format("%.2f", new Object[]{Double.valueOf(((double) millisUntilFinished) / 1000.0d)});
            }

            public void onFinish() {
            }
        };
        this.cdt = r0.start();
    }

    public void cancelTimer() {
        CountDownTimer countDownTimer = this.cdt;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
