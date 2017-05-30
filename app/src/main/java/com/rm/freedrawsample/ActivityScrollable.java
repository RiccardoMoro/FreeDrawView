package com.rm.freedrawsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.rm.freedrawview.FreeDrawView;

/**
 * Created by Riccardo on 01/12/16.
 */

public class ActivityScrollable extends AppCompatActivity {

    private FreeDrawView mDrawSignature, mDrawSmile, mDrawSad, mDrawLeaf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrollable);

        mDrawSignature = (FreeDrawView) findViewById(R.id.draw_signature);
        mDrawSmile = (FreeDrawView) findViewById(R.id.draw_smile);
        mDrawSad = (FreeDrawView) findViewById(R.id.draw_sad);
        mDrawLeaf = (FreeDrawView) findViewById(R.id.draw_leaf);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main_lite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        if (item.getItemId() == R.id.menu_github) {
            IntentHelper.openUrl(this, getString(R.string.github_url));
        }

        if (item.getItemId() == R.id.menu_delete) {
            mDrawSignature.clearDrawAndHistory();
            mDrawSmile.clearDrawAndHistory();
            mDrawSad.clearDrawAndHistory();
            mDrawLeaf.clearDrawAndHistory();
        }

        return super.onOptionsItemSelected(item);
    }
}
