package zj.playretrofit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestApi {

    String URL = "https://api.douban.com/v2/movie/top250?start=0&count=10"; //豆瓣接口

    /**
     * 结合rxJava
     */
    @GET("{url}")
    Observable<ResponseData> getPathDoubadataRx(@Path("url") String url);

    @GET("v2/movie/top250")
    Observable<ResponseData> getDoubadata(@Query("start") String start, @Query("count") String count);

    @FormUrlEncoded
    @POST("v2/movie/top250")
    Flowable<ResponseData> postDoubanData(@Field("start") String start, @Field("count") String count);

}
