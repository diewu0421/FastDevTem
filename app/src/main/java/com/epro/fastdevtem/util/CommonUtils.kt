package com.zlw.devlib.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.os.PowerManager
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import java.util.ArrayList

/**
 * 工具类
 * @author Zenglw
 * @version 1.0.1
 * @createTime 2017/8/18
 * @description 这是一个工具类,以后有什么静态的方法都可以往这个文件里添加
 */

/**
 * 获取手机的Cpu架构
 * @return 返回设备的Cpu架构信息
 */
fun getABI() = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) Build.SUPPORTED_ABIS[0] else Build.CPU_ABI

/**
 * 获取一个Activity的根布局
 * @param activity 需要获取根布局的Activity
 * @return 返回activity的根布局对象View
 */
fun getActivityRootView(activity:Activity)= (activity.findViewById(android.R.id.content) as ViewGroup).getChildAt(0)

/**
 * 获取屏幕的宽高
 */
fun getScreenSize(context: Context) = Point(context.resources.displayMetrics.widthPixels, context.resources.displayMetrics.heightPixels)

/**
 * 设置状态栏为透明的
 */
fun setTranslucentState(activity: Activity) {
    //以上代码基本解决适配各种版本全透明状态栏（如导航栏有需求可以再加导航栏） ，且状态栏的颜色会随着Toolbar的颜色改变而改变
    activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    //这个属性4.4算是全透明（有的机子是过渡形式的透明），5.0就是半透明了 我的模拟器、真机都是半透明，
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT//calculateStatusColor(Color.WHITE, (int) alphaValue)
    }
}

/**
 * 判断是不是锁屏状态
 *
 * @param context 上下文
 * @return true:锁屏 false：解锁
 */
fun isLock(context: Context): Boolean {
    val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val isScreenOn = pm.isScreenOn//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
    //        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
    //        boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
    return !isScreenOn
}

/**
 * 权限:<uses-permission android:name="android.permission.GET_TASKS"></uses-permission>
 * 判断当前界面是否是桌面 ,先获取桌面应用的程序包名,然后判断当前显示活动包名是否包含在内
 */
fun isHome(context: Context): Boolean {
    //		ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
    //		List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
    //		List<String> strs = getHomes(context);
    //		if(strs != null && strs.size() > 0){
    //			return strs.contains(rti.get(0).topActivity.getPackageName());
    //		}else{
    //			return false;
    //		}

    val mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val rti = mActivityManager.getRunningTasks(1)
    return getHomes(context).contains(rti[0].topActivity.packageName)
}

/**
 * 获得属于桌面的应用的应用包名称
 *
 * @return 返回包含所有桌面应用的包名的字符串列表
 */
fun getHomes(context: Context): List<String> {
    val names = ArrayList<String>()
    val packageManager = context.packageManager
    //属性
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    val resolveInfo = packageManager.queryIntentActivities(intent,
            PackageManager.MATCH_DEFAULT_ONLY)
    for (ri in resolveInfo) {
        names.add(ri.activityInfo.packageName)
    }
    return names
}

/**
 * 获取状态栏的高度
 */
fun getStatusHeight(context: Context):Int {
    var result = 0
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = context.resources.getDimensionPixelSize(resourceId)
    }
    return result
}


