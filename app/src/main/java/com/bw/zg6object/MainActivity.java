package com.bw.zg6object;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;


public class MainActivity extends AppCompatActivity {

    private long lruSize = Runtime.getRuntime().totalMemory() / 8;
    private LruCache<Object, Object> cache = new LruCache<>((int) lruSize);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //来自一个萌新的提交

    }
}