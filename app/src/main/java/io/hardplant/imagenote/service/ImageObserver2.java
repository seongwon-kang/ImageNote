package io.hardplant.imagenote.service;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;

import static android.content.ContentValues.TAG;

public class ImageObserver2 extends ContentObserver{

    public ImageObserver2(Handler handler) {
        super(handler);
        Log.i(TAG, "ImageObserver2: Registered");
    }

    @Override
    public void onChange(boolean selfChange, @Nullable Uri uri) {
        super.onChange(selfChange, uri);
        Log.i(TAG, "onChange: " + uri.getPath());
        Toast.makeText(null, "New Image", Toast.LENGTH_SHORT).show();
    }
}
