package com.sungshin.labsoon.cmr.NaverService;

import com.sungshin.labsoon.cmr.Main.MovieDataResult;
import com.sungshin.labsoon.cmr.Security.SecurityDataSet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by user on 2017-05-04.
 */

public interface NaverService {
    @Headers({"X-Naver-Client-Id :" + SecurityDataSet.NaverServiceClientID,
            "X-Naver-Client-Secret :" + SecurityDataSet.NaverServiceClientSecret})
    @GET("movie.json")
    Call<MovieDataResult> getMovieDataResult(@Query("query") String query, @Query("start") int start, @Query("display") int display, @Query("yearto") int yearto, @Query("yearfrom") int yearfrom,
                                             @Query("genre") String genre1);
}
