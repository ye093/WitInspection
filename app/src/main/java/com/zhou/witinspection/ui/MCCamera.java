package com.zhou.witinspection.ui;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v4.content.ContextCompat;
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
import java.util.Locale;
import java.util.Map;

import io.dcloud.common.adapter.util.PermissionUtil;

/**
 * WX自定义Camera
 */
@Component
public class MCCamera extends WXComponent<CameraView> {


    public static final String SUCCEED = "success";
    public static final String ERRORDESC = "errorDesc";

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
            }

            @Override
            public void layoutBefore() {
            }

            @Override
            public void layoutAfter(float width, float height) {
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
        CameraView cameraView = new CameraView(context);
//        cameraView.setKeepScreenOn(true);
        cameraView.setFacing(CameraKit.Constants.FACING_BACK);
        cameraView.setFlash(CameraKit.Constants.FLASH_AUTO);
        cameraView.setFocus(CameraKit.Constants.FOCUS_CONTINUOUS);
        cameraView.setPermissions(CameraKit.Constants.PERMISSIONS_PICTURE);
        cameraView.setCropOutput(true);
        cameraView.setJpegQuality(95);
        cameraView.setMethod(CameraKit.Constants.METHOD_STILL);
        return cameraView;
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
     * 相机重启
     */
    @JSMethod
    public void restartCamera(final JSCallback jsCallback) {
        CameraView cameraView = getHostView();
        if (cameraView != null) {
            cameraView.stop();
            cameraView.start();
        }
        Map<String, Object> params = new HashMap<>();
        params.put(SUCCEED, true);
        jsCallback.invoke(params);
    }

    @Override
    public void onActivityPause() {
        CameraView cameraKitView = getHostView();
        if (cameraKitView != null && cameraKitView.isStarted()) {
            cameraKitView.stop();
        }
        super.onActivityPause();
    }

    @Override
    public void onActivityResume() {
        final CameraView cameraKitView = getHostView();
        if (cameraKitView != null) {
            cameraKitView.start();
        }
        super.onActivityResume();
    }

    /**
     * 打开摄像头
     */
    @JSMethod
    public void startCamera(final JSCallback jsCallback) {
        final CameraView cameraKitView = getHostView();
        if (hasPermissions(getContext(), Manifest.permission.CAMERA)) {
            if (cameraKitView != null) {
                cameraKitView.start();
            }
            Map<String, Object> params = new HashMap<>();
            params.put(SUCCEED, true);
            jsCallback.invoke(params);
        } else {
            PermissionUtil.requestSystemPermissions((Activity) getContext(),
                    new String[]{Manifest.permission.CAMERA},
                    1352335, new PermissionUtil.Request() {
                        public void onGranted(String permission) {
                            if (permission.equals("CAMERA")) {
                                if (cameraKitView != null && !cameraKitView.isStarted()) {
                                    cameraKitView.start();
                                }
                            }

                        }

                        public void onDenied(String permission) {
                            if (permission.equals("CAMERA")) {
                                Map<String, Object> params = new HashMap<>();
                                params.put(SUCCEED, false);
                                params.put(ERRORDESC, "打开照相机需要拍照权限");
                                jsCallback.invoke(params);
                            }

                        }
                    });
        }
    }

    /**
     * 拍照 结果回调名称 onCaptureImage
     */
    @JSMethod
    public void captureImage(final JSCallback jsCallback) {
        // 假如没打开摄像头，先打开
        final CameraView cameraView = getHostView();
        if (cameraView == null) {
            Map<String, Object> result = new HashMap<>();
            result.put(SUCCEED, false);
            result.put(ERRORDESC, "相机初始化失败");
            jsCallback.invoke(result);
            return;
        }
        if (cameraView.isStarted()) {
            cameraView.captureImage(new CameraKitEventCallback<CameraKitImage>() {
                @Override
                public void callback(CameraKitImage cameraKitImage) {
                    if (cameraKitImage == null) {
                        Map<String, Object> result = new HashMap<>();
                        result.put(SUCCEED, false);
                        result.put(ERRORDESC, "系统异常，请重新检查拍照权限");
                        jsCallback.invoke(result);
                        return;
                    }
                    byte[] capturedImage = cameraKitImage.getJpeg();
                    if (capturedImage == null) {
                        cameraView.setJpegQuality(80);
                        Map<String, Object> result = new HashMap<>();
                        result.put(SUCCEED, false);
                        result.put(ERRORDESC, "图片过大，请重试");
                        jsCallback.invoke(result);
                        return;
                    }

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
                    if (!cameraView.isStarted())
                        cameraView.start();
                }
            });
        } else {
            if (hasPermissions(getContext(), Manifest.permission.CAMERA)) {
                cameraView.stop();
                cameraView.start();
                Map<String, Object> params = new HashMap<>();
                params.put(SUCCEED, false);
                params.put(ERRORDESC, "相机已打开，请再次点击拍照");
                jsCallback.invoke(params);
            } else {
                final CameraView cameraKitView = getHostView();
                PermissionUtil.requestSystemPermissions((Activity) getContext(),
                        new String[]{Manifest.permission.CAMERA},
                        1352335, new PermissionUtil.Request() {
                            public void onGranted(String permission) {
                                if (permission.equals("CAMERA")) {
                                    if (cameraKitView != null && !cameraKitView.isStarted()) {
                                        cameraKitView.start();
                                    }
                                }

                            }

                            public void onDenied(String permission) {
                                if (permission.equals("CAMERA")) {
                                    Map<String, Object> params = new HashMap<>();
                                    params.put(SUCCEED, false);
                                    params.put(ERRORDESC, "打开照相机需要拍照权限");
                                    jsCallback.invoke(params);
                                }

                            }
                        });
            }
        }
    }

    @Override
    public void recycled() {
        CameraView cameraKitView = getHostView();
        if (cameraKitView != null) {
            cameraKitView.stop();
        }
        super.recycled();
    }

    @Override
    protected void onFinishLayout() {
        super.onFinishLayout();
        final CameraView cameraKitView = getHostView();
        if (hasPermissions(getContext(), Manifest.permission.CAMERA)) {
            if (cameraKitView != null && !cameraKitView.isStarted()) {
                cameraKitView.start();
            }
        } else {
            PermissionUtil.requestSystemPermissions((Activity) getContext(),
                    new String[]{Manifest.permission.CAMERA},
                    1352335, new PermissionUtil.Request() {
                        public void onGranted(String permission) {
                            if (permission.equals("CAMERA")) {
                                if (cameraKitView != null && !cameraKitView.isStarted()) {
                                    cameraKitView.start();
                                }
                            }

                        }

                        public void onDenied(String permission) {
                            //todo
                        }
                    });
        }
    }

    @Override
    public void destroy() {
        CameraView cameraKitView = getHostView();
        if (cameraKitView != null && cameraKitView.isStarted()) {
            cameraKitView.stop();
        }
        super.destroy();
    }

    /**
     * 检查权限
     */
    private boolean hasPermissions(@NonNull Context context,
                                   @Size(min = 1) @NonNull String... perms) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.w("MCCamera", "hasPermissions: API version < M, returning true by default");
            return true;
        }
        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(context, perm)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


}
