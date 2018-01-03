package com.example.joice.retrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    String TAG = " response ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            uploadVideoToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        saveUserInfoToLocalhost();
//        getUserInfoFromLocalhost();
//        rxjavaObservables();
//        multiRequest();
    }

    private void saveUserInfoToLocalhost() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> insert = apiInterface.addUserInfoToDatabase(new Contacts("boss", 22));
        insert.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, Thread.currentThread().getName());
                Log.d(TAG, response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });

    }


    private void getUserInfoFromLocalhost() {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Contacts>> data = apiInterface.getUserInfo();
        data.enqueue(new Callback<List<Contacts>>() {
            @Override
            public void onResponse(Call<List<Contacts>> call, Response<List<Contacts>> response) {
                List<Contacts> contacts = response.body();
                for (Contacts c : contacts) {
                    Log.d(TAG, "onResponse: " + c.getName() + "  " + c.getAge());
                }
            }

            @Override
            public void onFailure(Call<List<Contacts>> call, Throwable t) {
                Log.d(TAG, "onResponseError: " + t);
            }
        });
    }

    private void multiRequest() {

        Observable.just(ApiClient.getApiClient().create(ApiInterface.class)).subscribeOn(Schedulers.computation())
                .flatMap(s -> {
                    Observable<List<Contacts>> getUserInfo
                            = s.getUserInfo1().subscribeOn(Schedulers.io());

                    Observable<String> storeUserInfo
                            = s.addUserInfoToDatabase1(new Contacts("joice", 23)).subscribeOn(Schedulers.io());

                    return Observable.merge(getUserInfo, storeUserInfo);
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResults, this::handleError);

    }

    private void handleError(Throwable throwable) {
        Log.d("error", throwable.toString());
    }

    private void handleResults(Object o) {
        Log.d("response", o.toString());
    }

    void rxjavaObservables() {

        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    okhttp3.Call response = okHttpClient.newCall(new Request.Builder().url("http://10.0.2.2/getUser.php").build());
                    okhttp3.Response response1 = response.execute();
                    if (response1.isSuccessful()) {
                        e.onNext(response1.body());
                    }
                } catch (Exception ex) {
                    e.onError(ex);
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());
        observable.safeSubscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, "onNext: " + o);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onC");
            }
        });
    }


    private void uploadVideoToServer() throws IOException {
//todo has some bugs in audio.php file not tested
        RequestBody videoBody = RequestBody.create(MediaType.parse("audio/*"), String.valueOf(getAssets().open("audio.mp3")));
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", "audio.mp3", videoBody);

        AudioInterface vInterface = ApiClient.getApiClient().create(AudioInterface.class);

        Call<ResultObject> serverCom = vInterface.uploadVideoToServer(vFile);
        serverCom.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject result = response.body();
                Log.d(TAG, "Result " + result.getSuccess());

            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Log.d(TAG, "Error message " + t.getMessage());
            }
        });
    }
}