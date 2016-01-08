package com.rex.wepeiyang.interactor;

import com.google.gson.JsonElement;
import com.rex.wepeiyang.api.ApiClient;
import com.rex.wepeiyang.ui.feedback.OnFeedbackCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 16/1/8.
 */
public class FeedbackInteractorImpl implements FeedbackInteractor {
    @Override
    public void feedback(String ua, String content, String email, final OnFeedbackCallback onFeedbackCallback) {
        ApiClient.feedback(ua, content, email, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                onFeedbackCallback.onSuccess();
            }

            @Override
            public void failure(RetrofitError error) {
                onFeedbackCallback.onFailure(error);
            }
        });
    }
}
