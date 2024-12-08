package com.example.bicycles.ViewModels;

public interface Callback<T> {
    void onResponse(T data);
    void onFailure(Throwable throwable);
}
