package com.fro.atmenv_cplxmonitorcase;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;


public class MainActivity extends AppCompatActivity {
	private Context context;
	private Float a = 1.1123F;



	private TextView sun_tv;
	private TextView tem_tv;
	private TextView hum_tv;
	private TextView pm25_tv;
	private Button graph_bt;
	private ToggleButton connect_tb;
	private TextView info_tv;
	private MenuView.ItemView  SettingItem;
	private ConnectTask connectTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		// 绑定控件
		bindView();
		// 事件监听
		initEvent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_setting,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if(item.getItemId() == R.id.SettingMenu){
			Intent intent = new Intent(MainActivity.this,SettingActivity.class);
			startActivity(intent);
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
					connectTask = new ConnectTask(context, tem_tv, hum_tv, sun_tv, pm25_tv, info_tv);
					connectTask.setCIRCLE(true);
					connectTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
}
