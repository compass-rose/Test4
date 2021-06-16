package com.fro.atmenv_cplxmonitorcase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    private Context context;
    private Button refresh_bt;
    private TextView sun_tv;
    private TextView tem_tv;
    private TextView hum_tv;
    private TextView pm25_tv;
    private Button graph_bt;
    private ToggleButton connect_tb;
    private TextView info_tv;
    private ConnectTask connectTask;
    private refreshTask refreshTask;
    public SQLManager DataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        // 绑定控件
        bindView();
		//初始化数据
        setData();
        // 事件监听
        initEvent();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.SettingMenu) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.ExportDB_Button){
            Intent shareIntent = new Intent();
            File path = new File(context.getDatabasePath("environment.db").getPath());
            if(!path.exists()){
                Log.d("DataBase Exists","Empty Database");
            }else {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(path));
                shareIntent.setType("application/x-sqlite3");
                context.startActivity(Intent.createChooser(shareIntent, "发送到"));
            }
        }
        return true;
    }


    /**
     * 绑定控件
     */
    private void bindView() {

        connect_tb = (ToggleButton) findViewById(R.id.connect_tb);
        info_tv = (TextView) findViewById(R.id.info_tv);
        sun_tv = (TextView) findViewById(R.id.sun_tv);
        tem_tv = (TextView) findViewById(R.id.tem_tv);
        hum_tv = (TextView) findViewById(R.id.hum_tv);
        pm25_tv = (TextView) findViewById(R.id.pm25_tv);
        graph_bt = (Button) findViewById(R.id.graph_bt);
        refresh_bt = (Button) findViewById(R.id.refresh_bt);
        DataBase = new SQLManager(context);
//        DataBase.insert(0,21,45,165,24);
//        DataBase.insert(0,12,42,65,12);
//        DataBase.insert(1,31,61,45,33);
//        DataBase.insert(1,32,42,145,44);
//        DataBase.insert(2,14,32,95,25);
//        DataBase.insert(2,7,86,105,55);
//        DataBase.insert(3,23,43,12,66);
//        DataBase.insert(3,21,85,35,77);
//        DataBase.insert(4,18,52,77,14);
//        DataBase.insert(4,34,81,123,72);
//        DataBase.insert(5,37,65,14,31);
//        DataBase.insert(5,39,12,51,24);
//        DataBase.insert(6,40,73,67,56);
//        DataBase.insert(6,21,98,88,79);
    }


    /**
     * 按钮监听
     */
    private void initEvent() {

        // 连接
        connect_tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    connectTask = new ConnectTask(context, tem_tv, hum_tv, sun_tv, pm25_tv, info_tv, DataBase);
                    connectTask.setCIRCLE(true);
                    connectTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    refresh_bt.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refreshTask = new refreshTask(context, tem_tv, hum_tv, sun_tv, pm25_tv, info_tv, DataBase);
                            refreshTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, connectTask.sunSocket, connectTask.temHumSocket, connectTask.pm25Socket);
                        }
                    });
                } else {
                    // 取消任务
                    if (connectTask != null && connectTask.getStatus() == AsyncTask.Status.RUNNING) {
                        connectTask.setCIRCLE(false);
                        // 如果Task还在运行，则先取消它
                        connectTask.cancel(true);
                        connectTask.closeSocket();
                    }
                    info_tv.setText("请点击连接！");
                    info_tv.setTextColor(context.getResources().getColor(R.color.gray));
                }
            }
        });


        // 柱状图
        graph_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GraphActivity.startThisActivity((Activity) context);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        // 取消任务
        if (connectTask != null && connectTask.getStatus() == AsyncTask.Status.RUNNING) {
            connectTask.setCIRCLE(false);
            // 如果Task还在运行，则先取消它
            connectTask.cancel(true);
            connectTask.closeSocket();
        }
    }

    /**
     * 读取并设置设置文件中数据
     */
    private void setData() {
        SharedPreferences sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        String SUN_IP = sharedPreferences.getString("SUN_IP", getResources().getString(R.string.default_SUN_IP));
        String SUN_PORT = String.valueOf(sharedPreferences.getInt("SUN_PORT", Integer.parseInt(getResources().getString(R.string.default_SUN_PORT))));
        String TEMHUM_IP = sharedPreferences.getString("TEMHUM_IP", getResources().getString(R.string.default_TEMHUM_IP));
        String TEMHUM_PORT = String.valueOf(sharedPreferences.getInt("TEMHUM_PORT", Integer.parseInt(getResources().getString(R.string.default_TEMHUM_PORT))));
        String PM25_IP = sharedPreferences.getString("PM25_IP", getResources().getString(R.string.default_PM25_IP));
        String PM25_PORT = String.valueOf(sharedPreferences.getInt("PM25_PORT", Integer.parseInt(getResources().getString(R.string.default_PM25_PORT))));
        String time = String.valueOf(sharedPreferences.getInt("time", Integer.parseInt(getResources().getString(R.string.default_time))));

        Log.d("ReadFILE_Main", SUN_IP);
        Log.d("ReadFILE_Main", SUN_PORT);
        Log.d("ReadFILE_Main", TEMHUM_IP);
        Log.d("ReadFILE_Main", TEMHUM_PORT);
        Log.d("ReadFILE_Main", PM25_IP);
        Log.d("ReadFILE_Main", PM25_PORT);
        Log.d("ReadFILE_Main", time);


        Const.SUN_IP = SUN_IP;
        Const.SUN_PORT = Integer.parseInt(SUN_PORT);
        Const.TEMHUM_IP = TEMHUM_IP;
        Const.TEMHUM_PORT = Integer.parseInt(TEMHUM_PORT);
        Const.PM25_IP = PM25_IP;
        Const.PM25_PORT = Integer.parseInt(PM25_PORT);
        Const.time = Integer.parseInt(time);

    }


}
