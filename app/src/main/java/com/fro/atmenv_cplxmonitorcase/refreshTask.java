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

    public refreshTask(Context context, TextView tem_tv, TextView hum_tv, TextView sun_tv, TextView pm25_tv, TextView info_tv,
                       Button refresh_bt) {
        super(context, tem_tv, hum_tv, sun_tv, pm25_tv, info_tv, refresh_bt);
    }

    @Override
    protected void onPreExecute() {
        info_tv.setText("刷新成功");
        Log.d("RefreshTask","Refresh Start");
    }

    @Override
    protected Void doInBackground(Void... params) {
        // 连接
        sunSocket = getSocket(Const.SUN_IP, Const.SUN_PORT);
        temHumSocket = getSocket(Const.TEMHUM_IP, Const.TEMHUM_PORT);
        pm25Socket = getSocket(Const.PM25_IP, Const.PM25_PORT);

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
}
