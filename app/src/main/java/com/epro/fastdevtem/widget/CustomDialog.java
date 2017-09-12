package com.epro.fastdevtem.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 自定义窗口
 * @author Zenglw
 * @version [1.0,2017-6-12]
 */

public class CustomDialog extends Dialog {
    //    style引用style样式
    public CustomDialog(Context context, int width, int height, View layout, int style) {

        super(context, style);

        setContentView(layout);

        Window window = getWindow();

        WindowManager.LayoutParams params = window.getAttributes();

        params.gravity = Gravity.CENTER;

        window.setAttributes(params);
    }

}
