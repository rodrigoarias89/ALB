package com.alabarra.api;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import ar.com.alabarra.clientsdk.ALaBarraMobileAPIClient;
import ar.com.alabarra.clientsdk.model.SignUpInputModel;

/**
 * Created by rodrigoarias on 7/6/17.
 */

public class ApiHelper {

    private final static String TAG = "ApiHelper";

    private ALaBarraMobileAPIClient mClient;

    public static ApiHelper init(AWSCredentialsProvider provider) {
        return new ApiHelper(provider);
    }

    private ApiHelper(AWSCredentialsProvider provider) {
        ApiClientFactory factory = getClientFactory().credentialsProvider(provider);
        mClient = factory.build(ALaBarraMobileAPIClient.class);
    }

    private ApiClientFactory getClientFactory() {
        ApiClientFactory factory = new ApiClientFactory();
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        //If a Lambda function is called after a long time, it takes significantly longer.
        //With the default configuration, it produce an error.
        clientConfiguration.setSocketTimeout(60000);
        factory.clientConfiguration(clientConfiguration);
//        factory.credentialsProvider()
        return factory;
    }

    public void test() {

        new AsyncBancarApiTask(new OnBancarApiResponse() {
            @Override
            public void onSuccess(Object response) {
                Log.i("TAG", response.toString());
            }

            @Override
            public void onFailed(Exception exception) {
                Log.e("TAG", exception.getLocalizedMessage(), exception);
            }
        }).execute(new Callable() {
            @Override
            public Object call() {
                SignUpInputModel inputModel = new SignUpInputModel();
                inputModel.setEmail("test@test.com");
                inputModel.setName("Test");
                inputModel.setUsername("test");
                return mClient.apiMobileV1SignupPost(inputModel);
            }
        });

    }

    private class AsyncBancarApiTask extends AsyncTask<Callable, Void, Boolean> {

        OnBancarApiResponse handler;

        public AsyncBancarApiTask(OnBancarApiResponse handler) {
            this.handler = handler;
        }

        @Override
        protected Boolean doInBackground(Callable... callables) {
            Log.i(TAG, "Calling API in background");
            try {
                handler.onSuccess(callables[0].call());
            } catch (Exception e) {
                Log.e(TAG, "Error on ApiHelper", e);
                handler.onFailed(e);
            }

            return true;
        }
    }

    interface Callable<T> {

        T call();

    }

    public interface OnBancarApiResponse<T> {

        void onSuccess(T response);

        void onFailed(Exception exception);

    }

}
