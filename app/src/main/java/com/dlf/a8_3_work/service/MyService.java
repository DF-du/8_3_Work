package com.dlf.a8_3_work.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dlf.a8_3_work.bean.Load_bean;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MyService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        PermissionsUtil.requestPermission(this, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadFile();
                    }
                }).start();
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {

            }
        }, Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private static final String TAG = "MyService";
    private void loadFile() {
//        http://cdn.banmi.com/banmiapp/apk/banmi_330.apk
        String path = Environment.getExternalStorageDirectory() + File.separator + "banmi_330.apk";
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            URL url = new URL("http://cdn.banmi.com/banmiapp/apk/banmi_330.apk");
            URLConnection connection = url.openConnection();
            int contentLength = connection.getContentLength();
            InputStream inputStream = connection.getInputStream();
            byte[] bs = new byte[1024];
            int length;
            int count = 0;
            while ((length = inputStream.read())!= -1){
                outputStream.write(bs,0,length);
                count += length;
                Load_bean loadBean = new Load_bean();
                loadBean.setContentLength(contentLength);
                loadBean.setLength(count);
                Log.d(TAG, "loadFile: ----------"+contentLength);
                EventBus.getDefault().post(loadBean);
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

