
package com.lenovo.studentClient.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lenovo.studentClient.R;
import com.lenovo.studentClient.utils.LoadingDialog;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * BaseActivity 所有Activity都是继承此类
 * 所以父类的基类
 * @author asus
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
    /**
     * 标题信息
     */
    public String TAG = getClass().getSimpleName();
    protected TextView mTitleTV;
    private boolean status = false;
    public static ExecutorService service = new ThreadPoolExecutor(7,20,10, TimeUnit.MICROSECONDS
            ,new ArrayBlockingQueue<>(10)
            ,new ThreadPoolExecutor.AbortPolicy());
     NewWork  work;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initializeWithState(savedInstanceState);
        initData();
        setTitleLayout();
    }

    private void initWork() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        work = new NewWork(this);
        registerReceiver(work,intentFilter);
    }

    protected abstract void initializeWithState(Bundle savedInstanceState);


    /**
     * 设置页面标题栏
     */
    private void setTitleLayout() {

        RelativeLayout layout =findViewById(R.id.titlelayout);
        if (layout==null){
            return;
        }
        ImageView imageView = layout.findViewById(R.id.back);
        mTitleTV = layout.findViewById(R.id.title);
        mTitleTV.setText(getLayoutTitle());
        imageView.setOnClickListener(v -> {

            onAfter();
        });
    }

    /**
     * @return 页面标题
     */
    protected abstract String getLayoutTitle();

    /**
     * 点击返回图标处理事件
     */
    protected abstract void onAfter();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * @return 对应的布局文件id
     */
    protected abstract int getLayoutId();


    private LoadingDialog mLoadingDialog;
    protected void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.show();
    }

    protected void onRequestFinish() {
        hideLoadingDialog();
    }

    private void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        status = false;
        initWork();
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(work);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoadingDialog();

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown: "+isTaskRoot());
        return super.onKeyDown(keyCode, event);
    }
     class NewWork extends BroadcastReceiver {
        Context context;
        NewWork(Context context){
            this.context = context;
        }
        AlertDialog dialog;
        private void ShowNetWorkDding(String data){
            dialog = new AlertDialog.Builder(context).create();
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_work,null);
            TextView textView = view.findViewById(R.id.tv_network);
            textView.setText(data);
            Button button = view.findViewById(R.id.btnconfirm);
            button.setOnClickListener(v -> {
                dialog.dismiss();
            });
            dialog.setView(view);
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        public void onReceive(Context context, Intent intent) {
//            ConnectivityManager
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                Network network = connectivityManager.getActiveNetwork();
                if (status){
                    if (network == null) {
                        if (dialog!=null){
                            dialog.dismiss();
                        }
                        ShowNetWorkDding("网络已经断开");
                    } else {
                        if (dialog!=null){
                            dialog.dismiss();
                        }
                        ShowNetWorkDding("网络已连接");
                    }
                }else {
                    status =true;
                }

            } else {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (status){
                    if (networkInfo == null) {
                        if (dialog!=null){
                            dialog.dismiss();
                        }
                        ShowNetWorkDding("网络已经断开");
                    } else {
                        if (dialog!=null){
                            dialog.dismiss();
                        }
                        ShowNetWorkDding("网络已连接");
                    }
                }else {
                    status =true;
                }
            }
        }
    }
}
