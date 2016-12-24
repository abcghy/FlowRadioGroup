package me.daolema.sakura.flowradiogroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.daolema.sakura.library.FlowRadioGroup;

public class MainActivity extends AppCompatActivity {

    FlowRadioGroup flowRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
