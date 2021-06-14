package com.fro.atmenv_cplxmonitorcase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingActivity extends AppCompatActivity {

    private EditText sunIp_et;
    private EditText sunPort_et;
    private EditText temHumIp_et;
    private EditText temHumPort_et;
    private EditText pm25Ip_et;
    private EditText pm25Port_et;
    private EditText time_et;
    private Button SaveButton;
    private Button DefaultButton;
    private Context context;



    private String SUN_IP = "";
    private String SUN_PORT = "";
    private String TEMHUM_IP = "";
    private String TEMHUM_PORT = "";
    private String PM25_IP = "";
    private String PM25_PORT = "";
    private String time = "";



    @Override
    public boolean releaseInstance() {
        return super.releaseInstance();
    }

    private ConnectTask connectTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        bindSettingView();

        ReadInFile();//读取文件中已保存的配置
        initData();//初始化数据

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
                saveInFile();
                Toast.makeText(SettingActivity.this, "成功保存", Toast.LENGTH_SHORT).show();
            }
        });
        DefaultButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(SettingActivity.this);
                alertDialogbuilder.setTitle("恢复默认设置");
                alertDialogbuilder.setMessage("是否恢复默认设置");
                alertDialogbuilder.setCancelable(true);

                //设置确定按钮
                alertDialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        defalultData();
                        Toast.makeText(SettingActivity.this, "恢复默认设置成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Log.d("DefaultButton", "Default the data");
                    }
                });
                //设定取消按钮
                alertDialogbuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Log.d("DefaultButton", "Cancel Default the data");
                    }
                });
                alertDialogbuilder.show();
            }
        });
    }

    /*
     *绑定view
     */
    private void bindSettingView() {
        sunIp_et = findViewById(R.id.sunIp_et);
        sunPort_et = findViewById(R.id.sunPort_et);
        temHumIp_et = findViewById(R.id.temHumIp_et);
        temHumPort_et = findViewById(R.id.temHumPort_et);
        pm25Ip_et = findViewById(R.id.pm25Ip_et);
        pm25Port_et = findViewById(R.id.pm25Port_et);
        time_et = findViewById(R.id.time_et);
        SaveButton = findViewById(R.id.SaveButton);
        DefaultButton = findViewById(R.id.DefaultButton);
        context = this;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        sunIp_et.setText(SUN_IP);
        sunPort_et.setText(SUN_PORT);
        temHumIp_et.setText(TEMHUM_IP);
        temHumPort_et.setText(TEMHUM_PORT);
        pm25Ip_et.setText(PM25_IP);
        pm25Port_et.setText(PM25_PORT);
        time_et.setText(time);
        setData();
    }


    /**
     * 恢复默认数据
     */

    private void defalultData() {
        sunIp_et.setText(R.string.default_SUN_IP);
        sunPort_et.setText(R.string.default_SUN_PORT);
        temHumIp_et.setText(R.string.default_TEMHUM_IP);
        temHumPort_et.setText(R.string.default_TEMHUM_PORT);
        pm25Ip_et.setText(R.string.default_PM25_IP);
        pm25Port_et.setText(R.string.default_PM25_PORT);
        time_et.setText(R.string.default_time);
        setData();
    }

    /**
     * 设置数据
     */
    private void setData() {

        // 获取输入的IP和端口
        String SUN_IP = sunIp_et.getText().toString().trim();
        String SUN_PORT = sunPort_et.getText().toString().trim();
        String TEMHUM_IP = temHumIp_et.getText().toString().trim();
        String TEMHUM_PORT = temHumPort_et.getText().toString().trim();
        String PM25_IP = pm25Ip_et.getText().toString().trim();
        String PM25_PORT = pm25Port_et.getText().toString().trim();
        String time = time_et.getText().toString().trim();
        if (checkIpPort(SUN_IP, SUN_PORT) && checkIpPort(TEMHUM_IP, TEMHUM_PORT)
                && checkIpPort(PM25_IP, PM25_PORT) && time != null && time != "") {
            Const.SUN_IP = SUN_IP;
            Const.SUN_PORT = Integer.parseInt(SUN_PORT);
            Const.TEMHUM_IP = TEMHUM_IP;
            Const.TEMHUM_PORT = Integer.parseInt(TEMHUM_PORT);
            Const.PM25_IP = PM25_IP;
            Const.PM25_PORT = Integer.parseInt(PM25_PORT);
            Const.time = Integer.parseInt(time);
        } else {
            Toast.makeText(this, "配置信息不正确,请重输！", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * IP地址可用端口号验证，可用端口号（1024-65536）
     *
     * @param IP
     * @param port
     * @return
     */
    private boolean checkIpPort(String IP, String port) {
        boolean isIpAddress = false;
        boolean isPort = false;

        if (IP == null || IP.length() < 7 || IP.length() > 15 || "".equals(IP)
                || port == null || port.length() < 4 || port.length() > 5) {
            return false;
        }
        //判断IP格式和范围
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(IP);

        isIpAddress = mat.find();

        //判断端口
        int portInt = Integer.parseInt(port);
        if (portInt > 1024 && portInt < 65536) {
            isPort = true;
        }

        return (isIpAddress && isPort);
    }
    /*
     *保存配置文件
     */

    private void saveInFile() {
        SharedPreferences sharedPreferences = getSharedPreferences("Setting",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("SUN_IP", Const.SUN_IP);
        editor.putInt("SUN_PORT", Const.SUN_PORT);
        editor.putString("TEMHUM_IP", Const.TEMHUM_IP);
        editor.putInt("TEMHUM_PORT", Const.TEMHUM_PORT);
        editor.putString("PM25_IP", Const.PM25_IP);
        editor.putInt("PM25_PORT",Const.PM25_PORT);
        editor.putInt("Sun_IP", Const.PM25_PORT);
        editor.putInt("time", Const.time);
        editor.apply();
    }

    /*
     * 读取配置文件
     */
    private void ReadInFile() {
        SharedPreferences sharedPreferences = getSharedPreferences("Setting",MODE_PRIVATE);
        SUN_IP = sharedPreferences.getString("SUN_IP", getResources().getString(R.string.default_SUN_IP));
        SUN_PORT = String.valueOf(sharedPreferences.getInt("SUN_PORT", Integer.parseInt(getResources().getString(R.string.default_SUN_PORT))));
        TEMHUM_IP = sharedPreferences.getString("TEMHUM_IP",  getResources().getString(R.string.default_TEMHUM_IP));
        TEMHUM_PORT = String.valueOf(sharedPreferences.getInt("TEMHUM_PORT", Integer.parseInt( getResources().getString(R.string.default_TEMHUM_PORT))));
        PM25_IP = sharedPreferences.getString("PM25_IP", getResources().getString(R.string.default_PM25_IP));
        PM25_PORT = String.valueOf(sharedPreferences.getInt("PM25_PORT", Integer.parseInt(getResources().getString(R.string.default_PM25_PORT))));
        time = String.valueOf(sharedPreferences.getInt("time", Integer.parseInt(getResources().getString(R.string.default_time))));
        Log.d("ReadFILE_Setting",SUN_IP);
        Log.d("ReadFILE_Setting",SUN_PORT);
        Log.d("ReadFILE_Setting",TEMHUM_IP);
        Log.d("ReadFILE_Setting",TEMHUM_PORT);
        Log.d("ReadFILE_Setting",PM25_IP);
        Log.d("ReadFILE_Setting",PM25_PORT);
        Log.d("ReadFILE_Setting",time);
    }
}