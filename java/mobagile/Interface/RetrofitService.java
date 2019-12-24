package com.example.jeanmaccury.mobagile.Interface;

import com.example.jeanmaccury.mobagile.POJOs.BodyWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Jean Maccury on 03.03.2018.
 */


public interface RetrofitService {

    @GET("projects.json")
    Call<BodyWrapper> getProjects(@Query("key") String key);

    @GET("issues.json")
    Call<BodyWrapper> getIssues(@Query("project_id") int id);

    @GET("projects/{id}/memberships.json")
    Call<BodyWrapper> getMembers(@Path("id") int id);

    @GET("time_entries.json")
    Call<BodyWrapper> getTimeEntries(@Query("project_id") int id);

    @POST("time_entries.json")
    Call<BodyWrapper> postTimeEntry(@Header("Content-Type") String content_type,
                                    @Body BodyWrapper entry,
                                    @Query("key") String key);
    @PUT("issues/{id}.json")
    Call<Void> updateDoneRatio(@Path("id") int id,
                                 @Header("Content-Type") String content_type,
                                 @Query("key") String key,
                                 @Body BodyWrapper issue);
}
