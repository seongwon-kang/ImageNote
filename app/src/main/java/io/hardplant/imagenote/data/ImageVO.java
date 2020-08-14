package io.hardplant.imagenote.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ImageVO {
    @PrimaryKey
    public int imageId;

    @ColumnInfo(name="image_name")
    public String name;
}
