package com.me.codesniper;

import android.app.Activity;
import android.os.Bundle;

import mrcodesniper.me.apt_api.Test;

@Test
public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}
