package com.example.online_psychologist.Intarfaces;

import com.example.online_psychologist.Obj.ProfilePhotos;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Server {
    @FormUrlEncoded
    @POST("sendMessage")
    Call<String> getThemesListData (@Field("chat_id") long chat_id, @Field("text") String text);
    @FormUrlEncoded
    @POST("getUserProfilePhotos")
    Call<ProfilePhotos> getUserProfilePhotos (@Field("user_id") long user_id, @Field("limit") int limit);
    @FormUrlEncoded
    @POST("getFile")
    Call<ProfilePhotos.UserImageURL> getFile (@Field("file_id") String file_id);
}
