package io.hardplant.imagenote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

import io.hardplant.imagenote.service.ClassifierService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createChannelID();
        runService();

        tryDownload();
    }

    private void runService() {
        Intent intent = new Intent(this, ClassifierService.class);

        startService(intent);

        ClassifierService.enqueueWork(this, intent);
    }

    private void createChannelID() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(ClassifierService.CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void tryDownload() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

                String targetUri = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

                File file = new File(targetUri);
                Log.i(TAG, "run: " + file.exists());
                Log.i(TAG, "run: " + file.listFiles().length);

                Log.e(TAG, "onCreate: " + targetUri );
                Uri uri = Uri.parse("http://www.example.com/myfile2.mp3");

                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle("My File");
                request.setDescription("Downloading");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationUri(
                        Uri.parse("file://"
                                + targetUri
                                + "/myfile2.mp3"));

                downloadmanager.enqueue(request);
                Log.e(TAG, "onCreate: File download started");
            }
        }, 500);
    }
}