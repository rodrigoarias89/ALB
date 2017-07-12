package com.alabarra.api;

/**
 * Created by rodrigoarias on 2/15/17.
 */

public interface BackgroundTaskListener<T> {

    void onSuccess(T object);

    void onFailed(Exception e);

}
