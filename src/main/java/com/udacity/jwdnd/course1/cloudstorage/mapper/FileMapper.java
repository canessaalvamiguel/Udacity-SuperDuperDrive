package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    List<File> getFiles(Integer userid);

    @Select("SELECT * FROM FILES WHERE filename = #{filename} AND userid = #{userid}")
    File getFileByFileName(String filename, Integer userid);

    @Insert("INSERT INTO FILES(filename, contenttype, filesize, userid) VALUES (#{filename}, #{contenttype}, #{filesize}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Select("SELECT * FROM FILES WHERE userid = #{userid} AND fileid = #{fileid}")
    File getFile(Integer userid, Integer fileid);

    @Delete("DELETE FROM FILES WHERE userid = #{userid} AND fileid = #{fileid}")
    int deleteFile(Integer userid, Integer fileid);
}
