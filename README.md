# OkHttpCookies
Simple OkHttp Cookie Interceptor, can handle response and get cookies from response headers then when new request is triggered cookie headers are added automatically.

## Prerequisites
First, dependency must be added to build.gradle file.
```groovy
implementation 'nurisezgin.com.cookies:okhttpcookies:1.0.0'
```

## How To Use
* Build CookieInterceptor
```java
    CookieInterceptor cookieInterceptor = new CookieInterceptorBuilder(context).build();
```
* Add To Your OkHttpClient
```java
    OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(cookieInterceptor)
                .build();
```

## Authors
* **Nuri SEZGIN**-[Email](acnnurisezgin@gmail.com)

## Licence
Copyright 2018 Nuri SEZGIN

Apache License 2.0