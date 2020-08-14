package io.hardplant.imagenote.service;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.FileObserver;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.hardplant.imagenote.MainActivity;
import io.hardplant.imagenote.R;

public class ClassifierService extends JobIntentService {
    public static final String CHANNEL_ID = "notification";
    private static final String TAG = "ClassifierService";
    private static final int NOTIFICATION_ID = 39;
    private static String path = "";
    private static FileObserver fileObserver;


    public static void enqueueWork(Context context, Intent intent) {
        Log.i(TAG, "enqueueWork: RUNNING");

        //path = intent.getExtras().getString("PATH");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);

        mNotifyBuilder
                .setOngoing(true)
                .setContentTitle("ImageNote")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText("Detect");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(NOTIFICATION_ID, mNotifyBuilder.build());

        File dir = new File(path);
        if (!dir.isDirectory()) {
            Log.e(TAG, "onCreate: " + dir.getAbsolutePath());
            //throw new RuntimeException("Not a directory");
        } else {
            fileObserver = new FileObserver(dir) {
                @Override
                public void onEvent(int i, @Nullable String s) {
                    switch(i) {
                        case (FileObserver.CREATE):
                            moveFile(s);
                            break;
                    }
                }
            };
            fileObserver.startWatching();
        }

    }

    private void moveFile(String s) {
        File file = new File(s);
        try {
            Files.move(Paths.get(file.getAbsolutePath()), getTargetDir(s));
        } catch (IOException e) {

        }
    }

    private Path getTargetDir(String s) {

        return Paths.get(new File(s).getAbsolutePath());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }

    @Override
    public void onDestroy() {
        fileObserver.stopWatching();
        fileObserver = null;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.cancel(NOTIFICATION_ID);

        super.onDestroy();
    }
}
