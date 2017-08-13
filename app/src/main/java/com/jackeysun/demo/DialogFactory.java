package com.jackeysun.demo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by jackey on 2017/8/12.
 */

public class DialogFactory extends Dialog {

    public DialogFactory(@NonNull Context context, String title, @LayoutRes int layoutId, View view) {
        this(context, R.style.CustomDialog, title, layoutId, view);
    }

    public DialogFactory(@NonNull Context context, @StyleRes int themeResId, String title, @LayoutRes int layoutId, View view) {
        super(context, themeResId);
        if (layoutId != -1) {
            this.setContentView(layoutId);
        } else if (view != null) {
            this.setContentView(view);
        }
//        init();
    }


    private void init() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (wm.getDefaultDisplay().getWidth() * 0.7);
        lp.height = (int) (wm.getDefaultDisplay().getHeight() * 0.3);
        dialogWindow.setAttributes(lp);

    }

    @Override
    public void show() {
        if (this != null && !this.isShowing()) {
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if (this != null && this.isShowing()) {
            super.dismiss();
        }
    }

    public static class Builder {

        private Context context;
        private String title;
        private
        @LayoutRes
        int layoutId = -1;
        private View view;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setView(@LayoutRes int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public Builder setView(View view) {
            this.view = view;
            return this;
        }

        public DialogFactory create() {
            DialogFactory dialog = new DialogFactory(context, title, layoutId, view);
            return dialog;
        }

    }
}
