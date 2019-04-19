package com.zhou.witinspection.ui;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.dom.CSSConstants;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.layout.MeasureMode;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * WX自定义Camera
 */
@Component(lazyload = false)
public class MCCamera extends WXComponent<CameraView> implements EasyPermissions.PermissionCallbacks {

    public static final String SUCCEED = "success";
    public static final String ERRORDESC = "errorDesc";
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 0x33;

    private final WXSDKInstance instance;

    public MCCamera(final WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
        setContentBoxMeasurement(new ContentBoxMeasurement() {
            @Override
            public void measureInternal(float width, float height, int widthMeasureMode, int heightMeasureMode) {
                if (CSSConstants.isUndefined(width) || widthMeasureMode == MeasureMode.UNSPECIFIED) {
                    width = 0;
                }
                if (CSSConstants.isUndefined(height) || heightMeasureMode == MeasureMode.UNSPECIFIED) {
                    height = 0;
                }
                mMeasureWidth = width;
                mMeasureHeight = height;
                Log.d("yejy", String.format("h = %f, w = %f", mMeasureHeight, mMeasureWidth));
            }

            @Override
            public void layoutBefore() {

            }

            @Override
            public void layoutAfter(float width, float height) {

                Log.d("yejy", String.format("h1 = %f, w1 = %f", height, width));
            }
        });

        this.instance = instance;
    }

    /**
     * 向js发送通知
     *
     * @param event  事件名称，weex默认事件名称格式为"onXXX",比如OnPullDown
     * @param params 需要发送的一些额外数据，比如click时，view大小，点击坐标等等。
     */
    private void callJS(String event, Map<String, Object> params) {
        if (event != null) {
            instance.fireEvent(getRef(), event, params);
        }
    }

    @Override
    protected CameraView initComponentHostView(@NonNull Context context) {
        Log.d("yejy", "执行了initComponentHostView");
        CameraView cameraView = new CameraView(context);
        cameraView.setKeepScreenOn(true);
        cameraView.setFacing(CameraKit.Constants.FACING_BACK);
        cameraView.setFlash(CameraKit.Constants.FLASH_AUTO);
        cameraView.setFocus(CameraKit.Constants.FOCUS_CONTINUOUS);
        return cameraView;
    }

    @Override
    protected void setHostLayoutParams(CameraView host, int width, int height,
                                       int left, int right, int top, int bottom) {
        super.setHostLayoutParams(host, width, height, left, right, top, bottom);

        Log.d("yejy", String.format("width = %d, height = %d, left = %d, " +
                "right = %d, top = %d, bottom = %d", width, height, left, right, top, bottom));
    }

    /**
     * 关闭摄像头
     */
    @JSMethod
    public void stopCamera(final JSCallback jsCallback) {
        Log.d("yejy", "执行了stopCamera");
        CameraView cameraView = getHostView();
        if (cameraView != null) {
            cameraView.stop();
        }
        Map<String, Object> params = new HashMap<>();
        params.put(SUCCEED, true);
        jsCallback.invoke(params);
    }

    /**
     * 打开摄像头
     */
    @JSMethod
    public void startCamera(final JSCallback jsCallback) {
        Log.d("yejy", "执行了startCamera");
        CameraView cameraView = getHostView();
        if (cameraView != null) {
            cameraView.start();
        }
        Map<String, Object> params = new HashMap<>();
        params.put(SUCCEED, true);
        jsCallback.invoke(params);
    }

    /**
     * 拍照 结果回调名称 onCaptureImage
     */
    @JSMethod
    public void captureImage(final JSCallback jsCallback) {
        Log.d("yejy", "执行了captureImage");
        CameraView cameraView = getHostView();
        if (cameraView != null && cameraView.isStarted()) {
            cameraView.captureImage(new CameraKitEventCallback<CameraKitImage>() {
                @Override
                public void callback(CameraKitImage cameraKitImage) {
                    byte[] capturedImage = cameraKitImage.getJpeg();
                    File savedPhoto = null;
                    String fileName = String.format(Locale.CHINA, "mv%d.jpg", System.currentTimeMillis());
                    String dir = "move_check";
                    File parDir = Environment.getExternalStorageDirectory();
                    if (parDir.exists()) {
                        File secDir = new File(parDir, dir);
                        boolean createResult = true;
                        if (!secDir.exists() || !secDir.isDirectory()) {
                            createResult = secDir.mkdir();
                        }
                        if (createResult) {
                            // 创建文件
                            savedPhoto = new File(secDir, fileName);
                        } else {
                            savedPhoto = new File(fileName);
                        }
                    }
                    if (savedPhoto == null) {
                        Map<String, Object> result = new HashMap<>();
                        result.put(SUCCEED, false);
                        result.put(ERRORDESC, "Image does not have the correct src");
                        jsCallback.invoke(result);
                        return;
                    }

                    FileOutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(savedPhoto.getPath());
                        outputStream.write(capturedImage);
                        outputStream.flush();
                        Map<String, Object> result = new HashMap<>();
                        result.put(SUCCEED, true);
                        result.put("path", savedPhoto.getAbsolutePath());
                        jsCallback.invoke(result);
                    } catch (java.io.IOException e) {
                        Map<String, Object> result = new HashMap<>();
                        result.put(SUCCEED, false);
                        result.put(ERRORDESC, e.getMessage());
                        jsCallback.invoke(result);
                    } finally {
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }


    @Override
    public void onActivityResume() {
        super.onActivityResume();
        Log.d("yejy", "执行了onActivityResume");
        requestCodeQRCodePermissions();
    }

    @Override
    public void recycled() {
        Log.d("yejy", "执行了recycled");
        CameraView cameraKitView = getHostView();
        if (cameraKitView != null) {
            cameraKitView.stop();
        }
        super.recycled();
    }

    @Override
    protected void onFinishLayout() {
        super.onFinishLayout();
        Log.d("yejy", "执行了onFinishLayout");
        requestCodeQRCodePermissions();
    }

    @Override
    public void destroy() {
        Log.d("yejy", "执行了destroy");
        CameraView cameraKitView = getHostView();
        if (cameraKitView != null) {
            cameraKitView.stop();
        }
        super.destroy();
    }

    @AfterPermissionGranted(CAMERA_PERMISSION_REQUEST_CODE)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(getContext(), perms)) {
            if (getContext() instanceof Activity) {
                EasyPermissions.requestPermissions((Activity) getContext(),
                        "随手拍需要打开相机和读写的权限", CAMERA_PERMISSION_REQUEST_CODE, perms);
            }
        } else {
            CameraView cameraKitView = getHostView();
            if (cameraKitView != null) {
                cameraKitView.start();
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
