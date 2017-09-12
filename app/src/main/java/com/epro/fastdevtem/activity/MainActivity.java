package com.epro.fastdevtem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.epro.fastdevtem.R;
import com.epro.fastdevtem.util.DialogManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        LoadingUtils.INSTANCE.createLoadingDialog(this, "正在加载中...");
        new DialogManager.Builder(this)
                .setContentView(R.layout.dialog_loading)
                .setText(R.id.tipTextView, "正在加载中...")
                .build()
                .showDialog();
    }

    public void jump(View view){
        startActivity(new Intent(this,Main2Activity.class));
    }
}
