package com.dlf.a8_3_work;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dlf.a8_3_work.databinding.ActivityMainBinding;
import com.dlf.a8_3_work.fragment.DownLoadFragment;
import com.dlf.a8_3_work.fragment.UpLoadFragment;
import com.dlf.a8_3_work.util.FileUtil;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding root;
    private UpLoadFragment upLoadFragment;
    private DownLoadFragment downLoadFragment;
    Context context;
    private ImageView headerView;
    public static final int RC_CHOOSE_PHOTO = 2;
    public static final int RC_TAKE_PHOTO = 1;
    private String mTempPhotoPath;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        root = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        initView();
        initData();
    }

    private void initData() {
        upLoadFragment = new UpLoadFragment();
        downLoadFragment = new DownLoadFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(root.frame.getId(), upLoadFragment);
        transaction.add(root.frame.getId(), downLoadFragment);
        transaction.commit();
        getCommit();
        getSupportFragmentManager().beginTransaction().show(upLoadFragment).commit();
        root.nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.upLoad:
                        root.tvToolbar.setText("上传");
                        getCommit();
                        getSupportFragmentManager().beginTransaction().show(upLoadFragment).commit();
                        break;
                    case R.id.downLoad:
                        root.tvToolbar.setText("下载");
                        getCommit();
                        getSupportFragmentManager().beginTransaction().show(downLoadFragment).commit();
                        break;
                }
                root.draw.closeDrawer(Gravity.LEFT);
                return false;
            }
        });
        headerView = (ImageView) root.nv.getHeaderView(R.id.header_img);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    choosPhoto();
            }
        });
        TextView photo = (TextView) root.nv.getHeaderView(R.id.tv_photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
    }




    private void choosPhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_CHOOSE_PHOTO:
                Uri uri = data.getData();
                String filePath = FileUtil.getFilePathByUri(this, uri);
                if (!TextUtils.isEmpty(filePath)) {
                    RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                    //将照片显示在 ivImage上
                    Glide.with(this).load(filePath).apply(requestOptions1).into(headerView);
                }
                break;
            case RC_TAKE_PHOTO:
                RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                //将图片显示在ivImage上
                Glide.with(this).load(mTempPhotoPath).apply(requestOptions).into(headerView);
                break;
        }
    }

    private void takePhoto() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "photoTest" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File photoFile = new File(fileDir, "photo.jpeg");
        mTempPhotoPath = photoFile.getAbsolutePath();
        imageUri = FileProvider.getUriForFile(this, "", photoFile);
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO);
    }

    private void initView() {
        setSupportActionBar(root.toolbar);
        root.toolbar.setTitle(" ");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, root.draw, root.toolbar, R.string.open, R.string.close);
        root.draw.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void getCommit() {
        getSupportFragmentManager().beginTransaction().hide(upLoadFragment).hide(downLoadFragment).commit();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

    }
}
