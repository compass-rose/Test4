package com.fro.atmenv_cplxmonitorcase;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.fro.atmenv_cplxmonitorcase.view.VerticalSeekBar;
import com.fro.util.FROPm25;
import com.fro.util.FROSun;
import com.fro.util.FROTemHum;
import com.fro.util.StreamUtil;

/**
 * Created by Jorble on 2016/3/4.
 */
public class GraphTask extends AsyncTask<Void, Void, Void> {

	private Context context;
	private VerticalSeekBar tem_sb;
	private VerticalSeekBar hum_sb;
	private VerticalSeekBar sun_sb;
	private VerticalSeekBar pm25_sb;

	private TextView tem_graph_tv;
	private TextView hum_graph_tv;
	private TextView sun_graph_tv;
	private TextView pm25_graph_tv;

	private boolean CIRCLE = false;

	public GraphTask(Context context, VerticalSeekBar tem_sb, VerticalSeekBar hum_sb, VerticalSeekBar sun_sb,
			VerticalSeekBar pm25_sb, TextView tem_graph_tv, TextView hum_graph_tv, TextView sun_graph_tv,
			TextView pm25_graph_tv) {
		this.context = context;
		this.tem_sb = tem_sb;
		this.hum_sb = hum_sb;
		this.sun_sb = sun_sb;
		this.pm25_sb = pm25_sb;
		this.tem_graph_tv = tem_graph_tv;
		this.hum_graph_tv = hum_graph_tv;
		this.sun_graph_tv = sun_graph_tv;
		this.pm25_graph_tv = pm25_graph_tv;
	}

	/**
	 * 更新界面
	 */
	@Override
	protected void onProgressUpdate(Void... values) {
		// 显示当前状态
		if (tem_graph_tv != null &&Const.tem != null && Const.tem >= 0 && Const.tem <= 40) {
			tem_sb.setProgress(Const.tem);
			tem_graph_tv.setText(String.valueOf(Const.tem));

		}
		
		if (hum_graph_tv != null &&Const.hum != null && Const.hum >= 0 && Const.hum <= 100) {
			
			//设置拖动条当前值
			hum_sb.setProgress(Const.hum);
			hum_graph_tv.setText(String.valueOf(Const.hum));
		}
		
		if (sun_graph_tv != null &&Const.sun != null && Const.sun >= 0 && Const.sun <= 5000) {
			sun_sb.setProgress(Const.sun);
			sun_graph_tv.setText(String.valueOf(Const.sun));
		}
		if (pm25_graph_tv != null && Const.pm25 != null && Const.pm25 >= 0 && Const.pm25 <= 250) {
			pm25_sb.setProgress(Const.pm25);
			pm25_graph_tv.setText(String.valueOf(Const.pm25));
		}

	}

	/**
	 * 子线程任务
	 * 
	 * @param params
	 * @return
	 */
	@Override
	protected Void doInBackground(Void... params) {
		// 循环读取数据
		while (CIRCLE) {
			try {
				Thread.sleep(1000);

				// 更新界面
				publishProgress();
				Thread.sleep(200);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	public void setCIRCLE(boolean cIRCLE) {
		CIRCLE = cIRCLE;
	}

	@Override
	protected void onCancelled() {
	}

}
