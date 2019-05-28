package edwy.lugo.youtube.api;

import edwy.lugo.youtube.helper.YoutubeConfig;
import edwy.lugo.youtube.model.Resultado;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface YoutubeService {

//    @GET("search/{param1}")
//    recuperarVideos(@Path("param1") String param1, String param2);

    /*

    https://www.googleapis.com/youtube/v3/
    search
    ?part=snippet
    &order=date
    &maxResults=20
    &key=AIzaSyAlro4euDdCZqELSEXQPZMP7hN_CWiCks8
    &channelId=UCVHFbqXqoYvEWM1Ddxl0QDg
    &q=desenvolvimento+android


    https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&maxResults=20&key=AIzaSyAlro4euDdCZqELSEXQPZMP7hN_CWiCks8&channelId=UCVHFbqXqoYvEWM1Ddxl0QDg

    */
    @GET("search")
    Call<Resultado> recuperarVideos(
                    @Query("part")          String part,
                    @Query("order")         String order,
                    @Query("maxResults")    String maxResults,
                    @Query("key")           String key,
                    @Query("channelId")     String channelId,
                    @Query("q")     String q
    );



}
