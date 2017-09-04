package edu.np.ece.ame_android_lecturer.Retrofit;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Created by MIYA on 31/08/17.
 */

public class ErrorInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);
        switch (response.code()){
            case 401:
                Log.e("TEST","Unauthorized error for: " +request.url());
        }

        return response;
    }
}