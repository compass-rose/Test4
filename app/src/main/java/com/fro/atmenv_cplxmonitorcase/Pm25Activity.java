package com.fro.atmenv_cplxmonitorcase;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class Pm25Activity extends AppCompatActivity {
    private LineChart lineChart; //折线图控件


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm25);
        //隐藏系统默认标题
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //初始化控件
        lineChart = findViewById(R.id.lc);
        initLineChart();

    }

    /**
     * 初始化图表数据
     */
    private void initLineChart(){
        lineChart.animateXY(2000, 2000); // 呈现动画
        Description description = new Description();
        description.setText(""); //自定义描述
        lineChart.setDescription(description);
        Legend legend = lineChart.getLegend();
        legend.setTextColor(Color.BLACK);
        setYAxis();
        setXAxis();
        setData();

    }

    /**
     * 设置Y轴数据
     */
    private void setYAxis(){
        YAxis yAxisLeft = lineChart.getAxisLeft();// 左边Y轴
        yAxisLeft.setDrawAxisLine(true); // 绘制Y轴
        yAxisLeft.setDrawLabels(true); // 绘制标签
        yAxisLeft.setAxisMaxValue(100); // 设置Y轴最大值
        yAxisLeft.setAxisMinValue(0); // 设置Y轴最小值
        yAxisLeft.setGranularity(3f); // 设置间隔尺寸
        yAxisLeft.setTextColor(Color.BLACK); //设置颜色
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int)value  + "μg/m3";
            }
        });
        // 右侧Y轴
        lineChart.getAxisRight().setEnabled(false); // 不启用
    }

    /**
     * 设置X轴数据
     */
    private void setXAxis(){
        // X轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawAxisLine(false); // 不绘制X轴
        xAxis.setDrawGridLines(false); // 不绘制网格线
        // 模拟X轴标签数据
        SQLManager sql = new SQLManager(Const.upContext);
        String s0 = sql.getPastDate(0).substring(5);
        String s1 = sql.getPastDate(1).substring(5);
        String s2 = sql.getPastDate(2).substring(5);
        String s3 = sql.getPastDate(3).substring(5);
        String s4 = sql.getPastDate(4).substring(5);
        String s5 = sql.getPastDate(5).substring(5);
        String s6 = sql.getPastDate(6).substring(5);
        String[] weekStrs = new String[]{s6, s5, s4, s3, s2, s1,s0};
        xAxis.setLabelCount(weekStrs.length); // 设置标签数量
        xAxis.setTextColor(Color.GREEN); // 文本颜色
        xAxis.setTextSize(15f); // 文本大小为18dp
        xAxis.setGranularity(1f); // 设置间隔尺寸
        // 使图表左右留出点空位
        xAxis.setAxisMinimum(-0.1f); // 设置X轴最小值
        //设置颜色
        xAxis.setTextColor(Color.BLACK);
        // 设置标签的显示格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return weekStrs[(int) value];
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 在底部显示
    }

    /**
     * 填充数据
     */
    private void setData(){
        // 模拟数据1
        List<Entry> yVals1 = new ArrayList<>();
        float[] d0 = dataSearch(0);
        float[] d1 = dataSearch(1);
        float[] d2 = dataSearch(2);
        float[] d3 = dataSearch(3);
        float[] d4 = dataSearch(4);
        float[] d5 = dataSearch(5);
        float[] d6 = dataSearch(6);
        float[] ys1 = new float[]{d6[0], d5[0], d4[0], d3[0], d2[0], d1[0],d0[0]};
        // 模拟数据2
        List<Entry> yVals2 = new ArrayList<>();
        float[] ys2 = new float[]{d6[1], d5[1], d4[1], d3[1], d2[1], d1[1],d0[1]};
        // 模拟数据3
        List<Entry> yVals3 = new ArrayList<>();
        float[] ys3= new float[]{d6[2], d5[2], d4[2], d3[2], d2[2], d1[2],d0[2]};
        for (int i = 0; i < ys1.length; i++) {
            yVals1.add(new Entry(i, ys1[i]));
            yVals2.add(new Entry(i, ys2[i]));
            yVals3.add(new Entry(i, ys3[i]));
        }
        // 2. 分别通过每一组Entry对象集合的数据创建折线数据集
        LineDataSet lineDataSet1 = new LineDataSet(yVals1, "最高pm2.5指数");
        LineDataSet lineDataSet2 = new LineDataSet(yVals2, "平均pm2.5指数");
        LineDataSet lineDataSet3 = new LineDataSet(yVals3, "最低pm2.5指数");
        lineDataSet2.setCircleColor(Color.RED); //设置点圆的颜色
        lineDataSet3.setCircleColor(Color.GREEN);//设置点圆的颜色
        lineDataSet1.setCircleRadius(5); //设置点圆的半径
        lineDataSet2.setCircleRadius(5); //设置点圆的半径
        lineDataSet3.setCircleRadius(5); //设置点圆的半径
        lineDataSet1.setDrawCircleHole(false); // 不绘制圆洞，即为实心圆点
        lineDataSet2.setDrawCircleHole(false); // 不绘制圆洞，即为实心圆点
        lineDataSet3.setDrawCircleHole(false); // 不绘制圆洞，即为实心圆
        lineDataSet2.setColor(Color.RED); // 设置为红色
        lineDataSet3.setColor(Color.GREEN); // 设置为黑色
        // 值的字体大小为12dp
        lineDataSet1.setValueTextSize(12f);
        lineDataSet2.setValueTextSize(12f);
        lineDataSet3.setValueTextSize(12f);
        //将每一组折线数据集添加到折线数据中
        LineData lineData = new LineData(lineDataSet1,lineDataSet2,lineDataSet3);
        //设置颜色
        lineData.setValueTextColor(Color.BLACK);
        //将折线数据设置给图表
        lineChart.setData(lineData);
    }
    public static void startThisActivity(Activity activity) {
        activity.startActivity(new Intent(activity, Pm25Activity.class));
    }
    private float[] dataSearch(int date){
        SQLManager sql = new SQLManager(Const.upContext);
        Cursor c = sql.getCursor(date);
        float sum = 0f;
        int count = 0;
        float max = 0f;
        float min = 100000f;
        float temp = 0f;
        if(c.moveToFirst()){
            do {
                count ++;
                temp = c.getFloat(c.getColumnIndex("pm25"));
                sum += temp;
                if(max < temp){
                    max = temp;
                }
                if(min > temp){
                    min = temp;
                }
            }while(c.moveToNext());
        }
        sum = sum / count;
        float[] data = new float[]{max,sum,min};
        return data;
    }
}
