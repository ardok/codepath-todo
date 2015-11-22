package com.codepath.simpletodo.helper;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

public class FabToast {
    public static void show(Activity activity, String text) {
        show(activity, text, Toast.LENGTH_SHORT);
    }

    public static void show(Activity activity, String text, int length) {
        Toast toast = Toast.makeText(activity, text, length);
        toast.setGravity(Gravity.BOTTOM|Gravity.START, 0, 40);
        toast.show();
    }
}
