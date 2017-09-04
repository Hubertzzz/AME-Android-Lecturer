package edu.np.ece.ame_android_lecturer.Retrofit;


import edu.np.ece.ame_android_lecturer.BuildConfig;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by MIYA on 31/08/17.
 */

public abstract class ServerCallBack<T> implements Callback<T> {

    private Call<T> call;

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        this.call = call;

        if (BuildConfig.DEBUG) {
            t.printStackTrace();
        }
    }

    public Call<T> getCall() {
        return call;
    }
}
