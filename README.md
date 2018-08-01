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

```
Copyright 2018 Nuri SEZGİN

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```