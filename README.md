# PlayRetrofitRxJava
RxJava结合Retrofit

# 0.前言
本文将演示RxJava2结合Retrofit2来进行网络请求。
不清楚Retrofit2怎么用的童鞋可以看这篇博客：[Retrofit各种姿势请求](http://blog.csdn.net/user11223344abc/article/details/78679098)，如果明白怎么使用的哥们则可以不用，不明白的哥们还是建议看下，因为本文是对比着这篇来写的。

还是用万能的豆瓣接口来进行测试：
String URL = “https://api.douban.com/v2/movie/top250?start=0&count=10“; //豆瓣接口

# 1.Retrofit在结合了RxJava之后，哪里变化了？
### 1.1 变化一，RequestApi变化了:
![](https://wx3.sinaimg.cn/mw1024/0061ejqJgy1fm14hz8hu1j3107078t9h.jpg)

### 1.2 变化二，请求时的回调也变了，这里我就列举一个来做对比:

**Rx&Retro：**
```java
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
```

**onlyRetro：**
```java

      findViewById(R.id.btn_request_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "请求发起...post");
                doCall(PostApi.postDoubanData("0", "10"));//传入district的值，得到Call对象
            }
        });

    /**
     * 执行并显示“请求接口”
     *
     * @param call
     */
    private void doCall(Call<ResponseBean> call) {
        //call#enqueue请求网络
        if (!call.isExecuted()) {
            call.enqueue(new Callback<ResponseBean>() {

                @Override
                public void onResponse(Call<ResponseBean> call, Response<ResponseBean> response) {
                    Log.d(TAG, response.body() == null ? "null" : response.body().toString());
                    PublicUtils.isMainThread();
                }

                @Override
                public void onFailure(Call<ResponseBean> call, Throwable t) {
                    Log.d(TAG, "失败" + t.toString());
                    PublicUtils.isMainThread();
                }
            });
        }
    }
```

# 3.Demo下载：
[项目地址](https://github.com/zj614android/PlayRetrofitRxJava)
