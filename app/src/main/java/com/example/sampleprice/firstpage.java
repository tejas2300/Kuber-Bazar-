package com.example.sampleprice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import static java.lang.Thread.sleep;

public class firstpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        this.setTitle("Kuber Bazar");

        //play Animation
        ImageView img = findViewById(R.id.img2);
        img.setImageResource(R.drawable.ppp);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myanim);
        img.setAnimation(anim);
        openActivity1();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
    private void openActivity1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(firstpage.this,MainActivity.class);
                startActivity(intent);
                firstpage.this.finish();
            }
        }).start();
    }

}