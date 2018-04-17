package com.onehookinc.onehooklibraryandroid.sample.common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.onehookinc.onehooklibraryandroid.R;

public class BaseActivity extends AppCompatActivity {

    public void startActivityAsSheet(final Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.anim_sliding_up, R.anim.anim_none);
    }

    public void startActivityFadeIn(final Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }

    public void startActivityAsStack(final Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter_sliding_left, R.anim.anim_exit_sliding_left);
    }

    public void startActivityForResultAsSheet(final Intent intent, final int requestCode) {
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.anim_sliding_up, R.anim.anim_none);
    }

    public void startActivityForResultAsStack(final Intent intent, final int requestCode) {
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.anim_enter_sliding_left, R.anim.anim_exit_sliding_left);
    }

    public void startActivityForResultNoAnimation(final Intent intent, final int requestCode) {
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.anim_none, R.anim.anim_none);
    }

    public void finishActivityAsSheet() {
        super.finish();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_sliding_down);
    }

    public void finishActivityAsStack() {
        super.finish();
        overridePendingTransition(R.anim.anim_enter_sliding_right, R.anim.anim_exit_sliding_right);
    }

    public void finishActivityFadeout() {
        super.finish();
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }

    public void finishActivityWithoutAnimation() {
        super.finish();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_none);
    }
}
