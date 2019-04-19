package com.zhou.witinspection;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXViewToImageUtil;

import java.util.HashMap;
import java.util.Map;

@Component(lazyload = false)
public class WeexComponent extends WXComponent<TextView> {
    public static final String SUCCEED = "success";
    public static final String ERRORDESC = "errorDesc";
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 0x2;

    float top, bottom, left, right;


    public WeexComponent(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
        top = parent.getCSSLayoutTop();
        bottom = parent.getCSSLayoutBottom();
        left = parent.getCSSLayoutLeft();
        right = parent.getCSSLayoutRight();
        float width = parent.getLayoutWidth();
        float height = parent.getLayoutHeight();
        if (top > 0) {
            if (bottom == 0 && height > 0) {
                bottom = top + height;
            }
        } else {
            if (bottom > 0 && height > 0) {
                top = bottom - height;
            } else {
                bottom = height;
            }
        }

        if (left > 0) {
            if (right == 0 && width > 0) {
                right = left + width;
            }
        } else {
            if (right > 0 && width > 0) {
                left = right - width;
            } else {
                right = width;
            }
        }
    }


    @Override
    protected TextView initComponentHostView(@NonNull Context context) {
        TextView textView = new TextView(context);
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    @WXComponentProp(name = "tel")
    public void setTel(String telNumber) {
        getHostView().setText("tel: " + telNumber);
    }


    @Override
    public void recycled() {
        //todo
        super.recycled();
    }

    @Override
    protected void onFinishLayout() {
        super.onFinishLayout();
        // todo
    }

    @Override
    public void destroy() {

        // todo
        super.destroy();
    }

    /**
     * Need permission {android.permission.WRITE_EXTERNAL_STORAGE}
     */
    @JSMethod(uiThread = false)
    public void save(final JSCallback saveStatuCallback) {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (getContext() instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) getContext(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            }
        }

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (saveStatuCallback != null) {
                Map<String, Object> result = new HashMap<>();
                result.put(SUCCEED, false);
                result.put(ERRORDESC,"Permission denied: android.permission.WRITE_EXTERNAL_STORAGE");
                saveStatuCallback.invoke(result);
            }
            return;
        }

//        if (mHost == null) {
//            if (saveStatuCallback != null) {
//                Map<String, Object> result = new HashMap<>();
//                result.put(SUCCEED, false);
//                result.put(ERRORDESC,"Image component not initialized");
//                saveStatuCallback.invoke(result);
//            }
//            return;
//        }
//
//        if (mSrc == null || mSrc.equals("")) {
//            if (saveStatuCallback != null) {
//                Map<String, Object> result = new HashMap<>();
//                result.put(SUCCEED, false);
//                result.put(ERRORDESC,"Image does not have the correct src");
//                saveStatuCallback.invoke(result);
//            }
//            return;
//        }

//        WXViewToImageUtil.generateImage(mHost, 0, 0xfff8f8f8, new WXViewToImageUtil.OnImageSavedCallback() {
//            @Override
//            public void onSaveSucceed(String path) {
//                if (saveStatuCallback != null) {
//                    Map<String, Object> result = new HashMap<>();
//                    result.put(SUCCEED, true);
//                    saveStatuCallback.invoke(result);
//                }
//            }
//
//            @Override
//            public void onSaveFailed(String errorDesc) {
//                if (saveStatuCallback != null) {
//                    Map<String, Object> result = new HashMap<>();
//                    result.put(SUCCEED, false);
//                    result.put(ERRORDESC,errorDesc);
//                    saveStatuCallback.invoke(result);
//                }
//            }
//        });
    }
}
