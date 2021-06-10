package com.fro.atmenv_cplxmonitorcase;

import java.util.Timer;
import java.util.TimerTask;


import com.fro.atmenv_cplxmonitorcase.view.VerticalSeekBar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class GraphActivity extends Activity {

	private Context context;
	private GraphTask graphTask;

	private VerticalSeekBar tem_sb;
	private VerticalSeekBar hum_sb;
	private VerticalSeekBar sun_sb;
	private VerticalSeekBar pm25_sb;

	private TextView tem_graph_tv;
	private TextView hum_graph_tv;
	private TextView sun_graph_tv;
	private TextView pm25_graph_tv;

	private Button tem_bt;
	private Button hum_bt;
	private Button sun_bt;
	private Button pm25_bt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		context = this;

		// 绑定控件
		bindView();
		// 初始化数据
		initData();
		// 开启任务,延时1s后开始定时任务,每2s执行一次
		graphTask = new GraphTask(context, tem_sb, hum_sb, sun_sb, pm25_sb, tem_graph_tv, hum_graph_tv, sun_graph_tv,
				pm25_graph_tv);
		graphTask.setCIRCLE(true);
		graphTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		tem_bt = (Button) findViewById(R.id.tem_bt);
		hum_bt = (Button) findViewById(R.id.hum_bt);
		sun_bt = (Button) findViewById(R.id.sun_bt);
		pm25_bt = (Button) findViewById(R.id.pm25_bt);

		tem_bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TemActivity.startThisActivity((Activity) context);
			}
		});
		hum_bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				HumActivity.startThisActivity((Activity) context);
			}
		});
		sun_bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SunActivity.startThisActivity((Activity) context);
			}
		});
		pm25_bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Pm25Activity.startThisActivity((Activity) context);
			}
		});
	}

	/**
	 * 绑定控件
	 */
	private void bindView() {
		tem_sb = (VerticalSeekBar) findViewById(R.id.tem_sb);
		hum_sb = (VerticalSeekBar) findViewById(R.id.hum_sb);
		sun_sb = (VerticalSeekBar) findViewById(R.id.sun_sb);
		pm25_sb = (VerticalSeekBar) findViewById(R.id.pm25_sb);

		tem_graph_tv = (TextView) findViewById(R.id.tem_graph_tv);
		hum_graph_tv = (TextView) findViewById(R.id.hum_graph_tv);
		sun_graph_tv = (TextView) findViewById(R.id.sun_graph_tv);
		pm25_graph_tv = (TextView) findViewById(R.id.pm25_graph_tv);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		tem_sb.setMax(100);// 设置最大值
		tem_sb.setProgress(0);// 设置进度

		hum_sb.setMax(100);// 设置最大值
		hum_sb.setProgress(0);// 设置进度

		sun_sb.setMax(1000);// 设置最大值
		sun_sb.setProgress(0);// 设置进度

		pm25_sb.setMax(100);// 设置最大值
		pm25_sb.setProgress(0);// 设置进度
	}

	/**
	 * 启动自身
	 * 
	 * @param activity
	 */
	public static void startThisActivity(Activity activity) {
		activity.startActivity(new Intent(activity, GraphActivity.class));
	}

	@Override
	public void finish() {
		super.finish();
		// 取消任务
		if (graphTask != null && graphTask.getStatus() == AsyncTask.Status.RUNNING) {
			graphTask.setCIRCLE(false);
			// 如果Task还在运行，则先取消它
			graphTask.cancel(true);
		}
	}

}
