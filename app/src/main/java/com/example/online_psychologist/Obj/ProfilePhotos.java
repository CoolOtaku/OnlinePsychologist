package com.example.online_psychologist.Obj;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProfilePhotos {
    @SerializedName("ok")
    private boolean ok;
    @SerializedName("result")
    public Result result;

    public ProfilePhotos(boolean ok, Result result) {
        this.ok = ok;
        this.result = result;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

public class Result{
    @SerializedName("total_count")
    private int total_count;
    @SerializedName("photos")
    public List<List<Photos>> photos;

    public Result(int total_count, List<List<Photos>> photos) {
        this.total_count = total_count;
        this.photos = photos;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public List<List<Photos>> getPhotos() {
        return photos;
    }

    public void setPhotos(List<List<Photos>> photos) {
        this.photos = photos;
    }
}
public class Photos{
    @SerializedName("file_id")
    private String file_id;
    @SerializedName("file_size")
    private long file_size;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;

    public Photos(String file_id, long file_size, int width, int height) {
        this.file_id = file_id;
        this.file_size = file_size;
        this.width = width;
        this.height = height;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public long getFile_size() {
        return file_size;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

public class UserImageURL{
    @SerializedName("ok")
    private boolean ok;
    @SerializedName("result")
    public UserImageURLResult result;

    public UserImageURL(boolean ok, UserImageURLResult result) {
        this.ok = ok;
        this.result = result;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public UserImageURLResult getResult() {
        return result;
    }

    public void setResult(UserImageURLResult result) {
        this.result = result;
    }
}
public class UserImageURLResult{
    @SerializedName("file_id")
    private String file_id;
    @SerializedName("file_unique_id")
    private String file_unique_id;
    @SerializedName("file_size")
    private long file_size;
    @SerializedName("file_path")
    private String file_path;

    public UserImageURLResult(String file_id, String file_unique_id, long file_size, String file_path) {
        this.file_id = file_id;
        this.file_unique_id = file_unique_id;
        this.file_size = file_size;
        this.file_path = file_path;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getFile_unique_id() {
        return file_unique_id;
    }

    public void setFile_unique_id(String file_unique_id) {
        this.file_unique_id = file_unique_id;
    }

    public long getFile_size() {
        return file_size;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
}
}

