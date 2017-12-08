package zj.playretrofit;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String TAG = "HardPlayReTro";

    //以此URL为例
    String URL = "https://api.douban.com/v2/movie/top250?start=0&count=10"; //豆瓣接口
    RequestApi retrofitPostApi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 初始化,得到retrofitPostApi的接口实例
         */
        init();

        /**
         * post
         */
        findViewById(R.id.btn_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "请求发起...post");
                retrofitPostApi.postDoubanData("0", "5").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ResponseData>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.e(TAG, "onSubscribe before request");
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(ResponseData responseData) {
                        Log.d(TAG, "flowAble value  = " + responseData.toString());
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });

        /**
         * get
         */
        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "请求发起...get & query");
                retrofitPostApi.getDoubadata("0", "5").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseData value) {
                        Log.d(TAG, "RX+RETRO ：value == " + value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });


        /**
         * get singlePath
         */
        findViewById(R.id.btn_single_path).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "请求发起...path");
                String url = "v2/book/1003078";
                retrofitPostApi.getPathDoubadataRx(url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(ResponseData value) {
                        Log.d(TAG, "RX+RETRO ：value == " + value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });
    }

    /**
     * 得到retrofitPostApi的接口实例
     */
    private void init() {
        //ps:这里采用的是Java的动态代理模式，得到请求接口对象
        retrofitPostApi = createRetrofit().create(RequestApi.class);
    }

    /**
     * 创建retrofit
     *
     * @return
     */
    @NonNull
    private Retrofit createRetrofit() {
        String BASE_URL = "https://api.douban.com/"; //豆瓣接口
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(9, TimeUnit.SECONDS);

        return new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(BASE_URL)//baseurl设置
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())//增加返回值为Gson的支持（返回实体类）
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//这个adapter很重要，专门找了方案来解决这个问题
                .build();
    }

}
