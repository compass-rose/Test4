package com.fro.atmenv_cplxmonitorcase;

import android.app.Activity;
import android.content.Intent;
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

public class HumActivity extends AppCompatActivity {
    private LineChart lineChart; //折线图控件


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hum);
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
        legend.setTextColor(Color.WHITE);
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
        yAxisLeft.setTextColor(Color.WHITE); //设置颜色
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int)value  + "℃";
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
        final String[] weekStrs = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六","星期日"};
        xAxis.setLabelCount(weekStrs.length); // 设置标签数量
        xAxis.setTextColor(Color.GREEN); // 文本颜色
        xAxis.setTextSize(15f); // 文本大小为18dp
        xAxis.setGranularity(1f); // 设置间隔尺寸
        // 使图表左右留出点空位
        xAxis.setAxisMinimum(-0.1f); // 设置X轴最小值
        //设置颜色
        xAxis.setTextColor(Color.WHITE);
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
        float[] ys1 = new float[]{80f, 90f, 80f, 90f, 80f, 80f,100f};
        // 模拟数据2
        List<Entry> yVals2 = new ArrayList<>();
        float[] ys2 = new float[]{60f, 75f, 60f, 77f, 55f, 65f,75f};
        // 模拟数据3
        List<Entry> yVals3 = new ArrayList<>();
        float[] ys3= new float[]{28f, 45f, 32f, 48f, 40f, 55f,45f};
        for (int i = 0; i < ys1.length; i++) {
            yVals1.add(new Entry(i, ys1[i]));
            yVals2.add(new Entry(i, ys2[i]));
            yVals3.add(new Entry(i, ys3[i]));
        }
        // 2. 分别通过每一组Entry对象集合的数据创建折线数据集
        LineDataSet lineDataSet1 = new LineDataSet(yVals1, "最高温度");
        LineDataSet lineDataSet2 = new LineDataSet(yVals2, "平均温度");
        LineDataSet lineDataSet3 = new LineDataSet(yVals3, "最低温度");
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
        lineData.setValueTextColor(Color.WHITE);
        //将折线数据设置给图表
        lineChart.setData(lineData);
    }
    public static void startThisActivity(Activity activity) {
        activity.startActivity(new Intent(activity, HumActivity.class));
    }
}
