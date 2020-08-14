package io.hardplant.imagenote.service;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.Nullable;

public class ImageObserver {

    private final Cursor mCursor;
    private final ContentObserver mObserver;
    private boolean mRunning = true;

    private class ObserverWithListener extends ContentObserver {
        private final OnChangeListener mListener;


        public ObserverWithListener(OnChangeListener listener)
        {
            super(new Handler());

            mListener = listener;
        }

        @Override
        public void onChange(boolean selfChange, @Nullable Uri uri) {
            super.onChange(selfChange, uri);
        }
    }
    public static ImageObserver getInstance(ContentResolver contentResolver, Uri uri, OnChangeListener listener)
    {
        Cursor c = contentResolver.query(uri, new String[] { "*" }, null, null, null);

//        if ((c = Dao.moveToFirst(c)) == null)
//        {
//            return null;
//        }

        return new ImageObserver(c, listener);
    }

    public ImageObserver(Cursor c, final OnChangeListener listener)
    {
        mCursor = c;
        mObserver = new ObserverWithListener(listener);
        mCursor.registerContentObserver(mObserver);
    }

    public void stop()
    {
        mCursor.unregisterContentObserver(mObserver);
//        Dao.closeCursor(mCursor);
        mRunning = false;
    }
    public interface OnChangeListener
    {
        public void onChange();
    }

}
