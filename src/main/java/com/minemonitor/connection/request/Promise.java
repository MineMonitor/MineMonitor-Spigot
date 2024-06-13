package com.minemonitor.connection.request;

import java.util.function.Consumer;

public class Promise<T> implements IPromise<T> {

    private Consumer<Exception> errorCallback;
    private Consumer<T> successCallback;
    private T result;
    private Exception error;

    @Override
    public void whenFinished(Consumer<T> successCallback) {
        this.successCallback = successCallback;
        if (result != null) {
            successCallback.accept(result);
        }
    }

    @Override
    public void onError(Exception e) {
        if (errorCallback != null) {
            errorCallback.accept(e);
        }
    }

    public void setErrorCallback(Consumer<Exception> errorCallback) {
        this.errorCallback = errorCallback;
        if (error != null) {
            errorCallback.accept(error);
        }
    }

    public void setResult(T result) {
        this.result = result;
        if (successCallback != null) {
            successCallback.accept(result);
        }
    }

    public void setError(Exception error) {
        this.error = error;
        if (errorCallback != null) {
            errorCallback.accept(error);
        }
    }


}
