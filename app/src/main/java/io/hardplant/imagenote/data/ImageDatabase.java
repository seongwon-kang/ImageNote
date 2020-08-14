package io.hardplant.imagenote.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ImageVO.class}, version = 1)
public abstract class ImageDatabase extends RoomDatabase {
    public abstract ImageDao imageDao();
}
