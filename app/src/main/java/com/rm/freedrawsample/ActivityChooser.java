package com.rm.freedrawsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Riccardo on 01/12/16.
 */

public class ActivityChooser extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnDraw, mBtnScrollable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chooser);

        mBtnDraw = (Button) findViewById(R.id.draw_sample);
        mBtnScrollable = (Button) findViewById(R.id.scrollable_sample);

        mBtnDraw.setOnClickListener(this);
        mBtnScrollable.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == mBtnDraw.getId()) {
            startActivity(new Intent(this, ActivityDraw.class));
        }

        if (id == mBtnScrollable.getId()) {
            startActivity(new Intent(this, ActivityScrollable.class));
        }
    }
}
