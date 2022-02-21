package http_utils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtils {

    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();

    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public static void runAsync(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpUtils.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void runAsyncWithRequest(Request request, Callback callback) {

        Call call = HttpUtils.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void ShutDown(){
        HTTP_CLIENT.connectionPool().evictAll();
    }
}
