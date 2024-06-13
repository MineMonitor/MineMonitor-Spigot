package com.minemonitor.connection.request;

public interface ICallback<T> {

    void onSucces(T t);
    void onError(Exception exception);
}
