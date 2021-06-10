package com.fro.atmenv_cplxmonitorcase;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.fro.util.FROPm25;
import com.fro.util.FROSun;
import com.fro.util.FROTemHum;
import com.fro.util.StreamUtil;

import java.io.IOException;
import java.net.Socket;

public class refreshTask extends ConnectTask{



    private Float sun;
    private Float tem;
    private Float hum;
    private Float pm25;
    private byte[] read_buff;

    private Socket sunSocket;
    private Socket temHumSocket;
    private Socket pm25Socket;
    public  Context context;

    public refreshTask(Context context, TextView tem_tv, TextView hum_tv, TextView sun_tv, TextView pm25_tv, TextView info_tv,
                       SQLManager database) {
        super(context, tem_tv, hum_tv, sun_tv, pm25_tv, info_tv, database);
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        Log.d("RefreshTask","Refresh Start");
    }

    @Override
    protected Void doInBackground(Socket... params) {
        sunSocket = params[0];
        temHumSocket  = params[1];
        pm25Socket = params[2];

        // 读取数据
            try {
                // 如果全部连接成功
                if (sunSocket!=null && temHumSocket!=null && pm25Socket!=null) {
                    // 查询光照度
                    StreamUtil.writeCommand(sunSocket.getOutputStream(), Const.SUN_CHK);
                    read_buff = StreamUtil.readData(sunSocket.getInputStream());
                    sun = FROSun.getData(Const.SUN_LEN, Const.SUN_NUM, read_buff);
                    if (sun != null) {
                        Const.sun = (int) (float) sun;
                    }
                    Log.i(Const.TAG, "Const.sun="+Const.sun);

                    // 查询温湿度
                    StreamUtil.writeCommand(temHumSocket.getOutputStream(), Const.TEMHUM_CHK);
                    read_buff = StreamUtil.readData(temHumSocket.getInputStream());
                    tem = FROTemHum.getTemData(Const.TEMHUM_LEN, Const.TEMHUM_NUM, read_buff);
                    hum = FROTemHum.getHumData(Const.TEMHUM_LEN, Const.TEMHUM_NUM, read_buff);
                    if (tem != null && hum != null) {
                        Const.tem = (int) (float) tem;
                        Const.hum = (int) (float) hum;
                    }
                    Log.i(Const.TAG, "Const.tem="+Const.tem);
                    Log.i(Const.TAG, "Const.hum="+Const.hum);

                    // 查询PM2.5
                    StreamUtil.writeCommand(pm25Socket.getOutputStream(), Const.PM25_CHK);
                    read_buff = StreamUtil.readData(pm25Socket.getInputStream());
                    pm25 = FROPm25.getData(Const.PM25_LEN, Const.PM25_NUM, read_buff);
                    if (pm25 != null) {
                        Const.pm25 = (int) (float) pm25;
                    }
                    Log.i(Const.TAG, "Const.pm25="+Const.pm25);
                    super.insertDB(Const.sun,Const.tem,Const.hum,Const.pm25);
                }
                // 更新界面
                publishProgress();
                Log.d("RefreshTask","Refresh end");
                Thread.sleep(200);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        if (sunSocket != null && temHumSocket != null && pm25Socket != null && info_tv != null) {
            info_tv.setTextColor(context.getResources().getColor(R.color.green));
            info_tv.setText("连接正常！");
        } else {
            info_tv.setTextColor(context.getResources().getColor(R.color.red));
            info_tv.setText("连接失败！");
            examineDevice();
        }
        //显示数据
        if (Const.sun != null && sun_tv!=null) {
            sun_tv.setText(String.valueOf(Const.sun));
        }
        if (Const.tem != null && tem_tv!=null) {
            tem_tv.setText(String.valueOf(Const.tem));
        }
        if (Const.hum != null&& hum_tv!=null) {
            hum_tv.setText(String.valueOf(Const.hum));
        }
        if (Const.pm25 != null&& pm25_tv!=null) {
            pm25_tv.setText(String.valueOf(Const.pm25));
        }
    }
}
