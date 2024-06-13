package com.minemonitor.connection.request;

import java.util.function.Consumer;

public class Callback<T> implements ICallback<T> {

    private final Runnable runnable;
    private final Consumer<Exception> exceptionConsumer;

    public Callback(Runnable runnable, Consumer<Exception> exceptionConsumer) {
        this.runnable = runnable;
        this.exceptionConsumer = exceptionConsumer;
    }

    @Override
    public void onSucces(T t) {
        runnable.run();
    }

    @Override
    public void onError(Exception exception) {
        exceptionConsumer.accept(exception);
    }
}
