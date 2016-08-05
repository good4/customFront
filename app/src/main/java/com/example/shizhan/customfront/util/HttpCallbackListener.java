package com.example.shizhan.customfront.util;

import com.example.shizhan.customfront.model.Custom;

import java.util.List;

/**
 * Created by shizhan on 16/7/25.
 */
public interface HttpCallbackListener {
    void onFinish(List<Custom> response);
    void onError(Exception e);
}
