package com.rm.freedrawsample;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.rm.rmfreedraw.RMFreeDrawVIew;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener {

    private RMFreeDrawVIew mFreeDrawView;
    private Button mBtnRandomColor, mBtnUndo, mBtnRedo, mBtnClearAll;

    @ColorInt
    private int mCurrentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFreeDrawView = (RMFreeDrawVIew) findViewById(R.id.free_draw_view);
        mBtnRandomColor = (Button) findViewById(R.id.btn_color);
        mBtnUndo = (Button) findViewById(R.id.btn_undo);
        mBtnRedo = (Button) findViewById(R.id.btn_redo);
        mBtnClearAll = (Button) findViewById(R.id.btn_clear_all);

        mBtnRandomColor.setOnClickListener(this);
        mBtnUndo.setOnClickListener(this);
        mBtnRedo.setOnClickListener(this);
        mBtnClearAll.setOnClickListener(this);

        changeColor();
    }

    private void changeColor() {
        mCurrentColor = ColorHelper.getRandomMaterialColor(this);

        mBtnRandomColor.setBackgroundColor(mCurrentColor);
        mBtnUndo.setBackgroundColor(mCurrentColor);
        mBtnRedo.setBackgroundColor(mCurrentColor);
        mBtnClearAll.setBackgroundColor(mCurrentColor);

        mFreeDrawView.setPaintColor(mCurrentColor);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == mBtnRandomColor.getId()) {
            changeColor();
        }

        if (id == mBtnUndo.getId()) {
            mFreeDrawView.undoLast();
        }

        if (id == mBtnRedo.getId()) {
            mFreeDrawView.redoLast();
        }

        if (id == mBtnClearAll.getId()) {
            mFreeDrawView.clearAll();
        }
    }
}
