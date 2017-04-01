package com.asdf.myapplication;

import android.animation.Animator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.asdf.myapplication.scaning.ScanView;

public class MainActivity extends AppCompatActivity {
    private ScanView scanView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanView = (ScanView) findViewById(R.id.scanview);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanView.startScanAnim();
            }
        });
        findViewById(R.id.scaning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanView.startScanMatchingAnim();
            }
        });
        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanView.startScanEndAnim(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        Toast.makeText(MainActivity.this,"结束动画开始",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Toast.makeText(MainActivity.this,"结束动画结束",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanView.resetView();
            }
        });
    }
}
