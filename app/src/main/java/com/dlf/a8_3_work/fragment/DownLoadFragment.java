package com.dlf.a8_3_work.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dlf.a8_3_work.bean.Load_bean;
import com.dlf.a8_3_work.R;
import com.dlf.a8_3_work.service.MyService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DownLoadFragment extends Fragment  implements View.OnClickListener{
    private ProgressBar mPro;
    private TextView mProTv;
    private Button mProBtn;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.downlayout, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(@NonNull final View itemView) {
        mPro = (ProgressBar) itemView.findViewById(R.id.pro);
        mProTv = (TextView) itemView.findViewById(R.id.tv_pro);
        mProBtn = (Button) itemView.findViewById(R.id.btn_down);
        mProBtn.setOnClickListener(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(Load_bean load_bean){
        int contentLength = load_bean.getContentLength();
        int length = load_bean.getLength();
        mPro.setMax(contentLength);
        mPro.setProgress(length);
        mProTv.setText("当前进度:"+length*100/contentLength+"%");
        if (length*100/contentLength == 100) {
            Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_down:
                // TODO 20/04/07
                Intent intent = new Intent(context, MyService.class);
                context.startService(intent);
                break;
            default:
                break;
        }
    }
}
