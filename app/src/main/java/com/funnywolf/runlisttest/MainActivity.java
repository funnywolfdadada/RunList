package com.funnywolf.runlisttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RunList.runOnBackground(new RunList.IRun<Integer, String>() {
                    @Override
                    public String run(Integer integer) {
                        Log.d(TAG, "run: " + Thread.currentThread().getName()
                                + ", " + integer);
                        return String.valueOf(integer + 1);
                    }
                }, 1)
                .runOnUiThread(new RunList.IRun<String, Integer>() {
                    @Override
                    public Integer run(String string) {
                        Log.d(TAG, "run: " + Thread.currentThread().getName()
                                + ", " + string);
                        return Integer.valueOf(string + 1);
                    }
                })
                .runOnBackground(new RunList.IRun<Integer, String>() {
                    @Override
                    public String run(Integer integer) {
                        Log.d(TAG, "run: " + Thread.currentThread().getName()
                                + ", " + integer);
                        return String.valueOf(integer + 1);
                    }
                })
                .runOnUiThread(new RunList.IRun<String, Integer>() {
                    @Override
                    public Integer run(String string) {
                        Log.d(TAG, "run: " + Thread.currentThread().getName()
                                + ", " + string);
                        return Integer.valueOf(string + 1);
                    }
                })
                .start();
    }
}
