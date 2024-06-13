package com.minemonitor.connection.request;

import java.util.function.Consumer;

public interface IPromise<T> {

    void whenFinished(Consumer<T> succesCallback);
    void onError(Exception e);
}
