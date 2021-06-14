package com.fro.util;
/**
 * Created by Jorble on 2016/3/1.
 */
public class FROPm25 {
    private static final String TAG="FROPm25";

    /**
     * 解析烟雾数据
     * @param rightLen
     * @param nodeNum
     * @param read_buff 整个返回值
     * @return
     */
    public static Float getData(int rightLen, int nodeNum,byte[] read_buff) {

        Float data= null;
        if (read_buff!=null) {
            // 长度是否正确，节点号是否正确，CRC是否正确
            if ((read_buff.length == rightLen && read_buff[0] == nodeNum) && CRCValidate.isCRCConfig(read_buff)) {
                /******************** CRC校验正确之后做的，解析数据 ********************/
                // 参数（要拷贝的数组源，拷贝的开始位置，要拷贝的目标数组，填写的开始位置，拷贝的长度）
                byte[] data_buff = new byte[2];//存放数据数组
                //数据开始位,第四位开始
                int dataOffset=3;
                //抠出数据，放进data_buff
                System.arraycopy(read_buff, dataOffset, data_buff, 0, 2);
                //解析数据data_buff（16进制转10进制）
                data= ByteToFloatUtil.hBytesToFloat(data_buff);
                /*********直接返回数据**********/
                return data;
            }
        }
        return data;// 返回数据
    }
}
