package com.epro.fastdevtem.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.epro.fastdevtem.BuildConfig

import com.epro.fastdevtem.R
import com.epro.fastdevtem.constant.BASE_URL
import com.epro.fastdevtem.util.CommonConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.zlw.devlib.utils.getStatusHeight
import com.zlw.devlib.utils.setTranslucentState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //测试设置透镜状态栏的功能
        setTranslucentState(this)
        statu_view.layoutParams.height = getStatusHeight(this)//获取状态栏的高度
        statu_view.requestLayout()

        //测试对话框能不能用
        //        LoadingUtils.INSTANCE.createLoadingDialog(this, "正在加载中...");
        //        new DialogManager.Builder(this)
        //                .setContentView(R.layout.dialog_loading)
        //                .setText(R.id.tipTextView, "正在加载中...")
        //                .build()
        //                .showDialog();
    }

    var count = 0

    fun jump(view: View) {
        //        startActivity(new Intent(this,Main2Activity.class));
//        Toast.makeText(this,"被点击了",Toast.LENGTH_SHORT).show()
//        Logger.e("x " + getScreenSize(this).x + " y = " + getScreenSize(this).y)

        Log.e("MainActivity", BASE_URL)
        Logger.e("nihao", "asdfas")

        Logger.e("$BASE_URL $count++ ${BuildConfig.DEBUG}")
    }
}
