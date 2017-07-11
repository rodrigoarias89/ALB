package com.alabarra.api;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import ar.com.alabarra.clientsdk.ALaBarraMobileAPIClient;
import ar.com.alabarra.clientsdk.model.SignUpInputModel;
import ar.com.alabarra.clientsdk.model.SignUpOutputModel;

/**
 * Created by rodrigoarias on 7/6/17.
 */

public class ApiHelper {

    private final static String TAG = "ApiHelper";
    private static ApiHelper mInstance;

    private ALaBarraMobileAPIClient mClient;

    public static void init(AWSCredentialsProvider provider) {
        mInstance = new ApiHelper(provider);
    }

    public static ApiHelper getInstance() {
        return mInstance;
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
        return factory;
    }

    public void signUpOrIn(String email, String name, String username, OnApiResponse<SignUpOutputModel> handler) {

        final SignUpInputModel inputModel = new SignUpInputModel();
        inputModel.setEmail(email);
        inputModel.setName(name);
        inputModel.setUsername(username);

        new AsyncBancarApiTask(handler).execute(new Callable() {
            @Override
            public Object call() {
                return mClient.apiMobileV1SignupPost(inputModel);
            }
        });
    }

    /*
    *
    * Utils
    *
     */

    private class AsyncBancarApiTask extends AsyncTask<Callable, Void, Boolean> {

        OnApiResponse handler;

        public AsyncBancarApiTask(OnApiResponse handler) {
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

    public interface OnApiResponse<T> {

        void onSuccess(T response);

        void onFailed(Exception exception);

    }

}
