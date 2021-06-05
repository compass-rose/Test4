package com.fro.atmenv_cplxmonitorcase;
public class Const {

	public static String TAG="CASE";
	
	//光照度
	public static String SUN_IP= "192.168.0.103";
	public static int SUN_PORT=4001;
	public static String SUN_CHK= "01 03 00 2a 00 01 a5 c2";
	public static int SUN_NUM=1;
	public static int SUN_LEN=7;
	public static Integer sun=null;
	
	//温湿度
	public static String TEMHUM_IP= "192.168.0.100";
	public static int TEMHUM_PORT=4001;
	public static String TEMHUM_CHK= "01 03 00 14 00 02 84 0f";
	public static int TEMHUM_NUM=1;
	public static int TEMHUM_LEN=9;
	public static Integer tem=null;
	public static Integer hum=null;
	
	//pm2.5
	public static String PM25_IP= "192.168.0.101";
	public static int PM25_PORT=4001;
	public static String PM25_CHK= "01 03 00 58 00 01 05 d9";
	public static int PM25_NUM=1;
	public static int PM25_LEN=7;
	public static Integer pm25=null;
	
	//配置
	public static Integer time=700;
}
