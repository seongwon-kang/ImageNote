package io.hardplant.imagenote.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;

import java.io.File;
import java.util.Date;

import io.hardplant.imagenote.R;

//https://stackoverflow.com/questions/40025585/android-get-recently-added-media-in-android-device-similarly-to-file-manager

public class MediaTrackerService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // images
        getContentResolver().registerContentObserver(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true, new ContentObserver(new Handler()) {
                    @Override
                    public void onChange(boolean selfChange) {
                        super.onChange(selfChange);

                        Media media = readFromMediaStore(
                                getApplicationContext(),
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        File file = new File(media.file.getPath());
                        Date fileData = new Date(file.lastModified());

                        if (System.currentTimeMillis() - fileData.getTime() > 3000) {

                        } else {

                            if (!media.file.getPath().contains("WhatsApp")) {
                                String saved = media.file.getName();

                                // deduce bitmap before using
                                Bitmap bitmap = BitmapFactory
                                        .decodeFile(media.file.getPath());

                                Intent intent = new Intent();
                                intent.setDataAndType(Uri.fromFile(media.file),
                                        "image/*");
                                PendingIntent pendingIntent = PendingIntent
                                        .getActivity(getApplicationContext(),
                                                0, intent, 0);

                                Notification n = new Notification.Builder(
                                        getApplicationContext())
                                        .setStyle(
                                                new Notification.BigPictureStyle()
                                                        .bigPicture(bitmap))
                                        .setContentTitle(
                                                "New Image File For You")
                                        .setContentText("File Name :" + saved)
                                        .setSmallIcon(R.drawable.ic_launcher_background)
                                        .setContentIntent(pendingIntent)
                                        .setAutoCancel(true).build();

                                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                                notificationManager.notify(0, n);
                            }
                        }
                    }

                    @Override
                    public void onChange(boolean selfChange, Uri uri) {
                        super.onChange(selfChange, uri);
                    }
                });
        return START_STICKY;
    }

    private Media readFromMediaStore(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null,
                null, "date_added DESC");
        Media media = null;
        if (cursor.moveToNext()) {
            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            String filePath = cursor.getString(dataColumn);
            int mimeTypeColumn = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE);
            String mimeType = cursor.getString(mimeTypeColumn);
            media = new Media(new File(filePath), mimeType);
        }
        cursor.close();
        return media;
    }

    private class Media {
        private File file;

        private String type;

        public Media(File file, String type) {
            this.file = file;
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public File getFile() {
            return file;
        }
    }
}
