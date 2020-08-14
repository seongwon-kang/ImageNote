package io.hardplant.imagenote.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDao {

    @Query("SELECT * FROM imagevo")
    List<ImageVO> getAll();

    @Insert
    void insertAll(ImageVO... imageVOS);

    @Delete
    void delete(ImageVO imageVO);

}
