package com.zhou.witinspection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONArray;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.StandardFeature;
import io.dcloud.common.util.JSUtil;

public class QRCodePlugin extends StandardFeature {

    private static final int QRCODE_SCAN_REQUEST = 1122;

    @Override
    public void onStart(Context context, Bundle bundle, String[] strings) {
        super.onStart(context, bundle, strings);
    }

    public void qrCodeScan(final IWebview iWebview, JSONArray jsonArray) {
        final String callbackID = jsonArray.optString(0);

        final IApp _app = iWebview.obtainFrameView().obtainApp();
        _app.registerSysEventListener(new ISysEventListener() {
            @Override
            public boolean onExecute(SysEventType sysEventType, Object pArgs) {
                Object[] _args = (Object[]) pArgs;
                int requestCode = (Integer) _args[0];
                int resultCode = (Integer) _args[1];
                Intent data = (Intent) _args[2];
                if (sysEventType == SysEventType.onActivityResult) {
                    _app.unregisterSysEventListener(this, SysEventType.onActivityResult);
                    if (resultCode == Activity.RESULT_OK) {
                        if (requestCode == QRCODE_SCAN_REQUEST) {
                            String scanResult = data.getStringExtra("data");
                            JSONArray result = new JSONArray();
                            result.put(scanResult);
                            JSUtil.execCallback(iWebview, callbackID, result, JSUtil.OK, false);
                            return true;
                        }
                    }
                }
                JSUtil.execCallback(iWebview, callbackID, new JSONArray(), JSUtil.NO_RESULT, false);
                return false;
            }
        }, SysEventType.onActivityResult);

        // 启动扫描
        final Activity activity = iWebview.getActivity();
        activity.startActivityForResult(new Intent(activity, QRCodeScanActivity.class),
                QRCODE_SCAN_REQUEST);
    }
}
