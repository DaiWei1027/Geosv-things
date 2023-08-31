package com.things.utils;

import com.things.common.dynamicCompilation.bytecode.ByteUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * @author DaiWei
 * @date 2023/03/06 09:14
 **/
public class ByteUtil {

    public static byte[] hexString2Bytes(String src) {

        int l = src.length() / 2;

        byte[] ret = new byte[l];

        for (int i = 0; i < l; i++) {

            ret[i] = (byte) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();

        }

        return ret;

    }

    public static int[] hexString2Ints(String src){
        int l = src.length() / 2;

        int[] ret = new int[l];

        for (int i = 0; i < l; i++) {

            ret[i] = Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16);

        }

        return ret;
    }

    /**
     * @param b 字节数组
     * @return 16进制字符串
     * @Description 字节数组转16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        StringBuilder result = new StringBuilder();
        for (byte value : b) {
            result.append(String.format("%02X", value));
//            result.append(' ');
        }
        return result.toString().trim();
    }

    /**
     * 16进制数组转字符
     *
     * @param b 16进制数组
     * @return String
     */
    public static String bytes2String(byte[] b) {

        return hexStringToString(bytes2HexString(b));

    }

    public static void main(String[] args) {

//        System.out.println(hexStringToString("31 2e 34 36 20 32 30 32 33 2d 30 36 2d 31 36 20 30 38 3a 34 39 3a 34 33  ".replace(" ","")));
//        System.out.println(hexStringToString("33 2e 34 31 20 32 30 32 33 2d 30 36 2d 31 36 20 30 38 3a 34 39 3a 34 33 ".replace(" ","")));
//        System.out.println(hexStringToString("33 2e 31 32 20 32 30 32 33 2d 30 36 2d 31 36 20 30 38 3a 34 39 3a 34 33 ".replace(" ","")));
//        System.out.println(hexStringToString("31 2e 34 30 20 32 30 32 33 2d 30 36 2d 31 36 20 31 33 3a 32 34 3a 30 38 31 2e 34 36 20 32 30 32 33 2d 30 36 2d 31 36 20 31 33 3a 32 34 3a 30 38 33 2e 34 31 20 32 30 32 33 2d 30 36 2d 31 36 20 31 33 3a 32 34 3a 30 38 33 2e 31 32 20 32 30 32 33 2d 30 36 2d 31 36 20 31 33 3a 32 34 3a 30 38 ".replace(" ","")));
//
//        byte[] bytes = hexString2Bytes("1b 06 00 00 a3 31 2e 34 30 20 32 30 32 33 2d 30 36 2d 31 36 20 31 33 3a 32 34 3a 30 38 31 2e 34 36 20 32 30 32 33 2d 30 36 2d 31 36 20 31 33 3a 32 34 3a 30 38 33 2e 34 31 20 32 30 32 33 2d 30 36 2d 31 36 20 31 33 3a 32 34 3a 30 38 33 2e 31 32 20 32 30 32 33 2d 30 36 2d 31 36 20 31 33 3a 32 34 3a 30 38 1b 03".replace(" ",""));
//
//        eventHandle(ArrayUtils.subarray(bytes, 5, 29));
//        eventHandle(ArrayUtils.subarray(bytes, 29, 53));
//        eventHandle(ArrayUtils.subarray(bytes, 53, 77));
//        eventHandle(ArrayUtils.subarray(bytes, 77, 101));

        System.out.println(hexStringToString("19 00 00 00".replace(" ","")));
        String s = hexStringToString("76 35 2e 32 2e 30 00 00 00 00 00 00 00 00 00".replace(" ", ""));
        System.out.println(s);
    }

    public static void eventHandle(byte[] payload){

        String eventId = bytes2String(ArrayUtils.subarray(payload, 0, 4));
        String date = bytes2String(ArrayUtils.subarray(payload, 5, 24));

        System.out.println(eventId);
        System.out.println(date);
    }



    public static Double getDoubleByHexstr(String hexstr) {
        //String[] hexarray = hexstr.split(", ");
        // 高地位互换
        //hexstr = hexarray[2] + hexarray[3] + hexarray[0] + hexarray[1];
        hexstr = hexstr.substring(4, 6) + hexstr.substring(6, 8) + hexstr.substring(0, 2) + hexstr.substring(2, 4);
        System.out.println(hexstr);
        // System.out.println( hexstr );
        // 先将16进制数转成二进制数0 10000001 00000000000000000000000 <br>
        String binarystr = hexString2binaryString(hexstr);
        // 1位符号位(SIGN)=0 表示正数
        String sign = binarystr.substring(0, 1);
        // 8位指数位(EXPONENT)=10000001=129[10进制]
        String exponent = binarystr.substring(1, 9);
        int expint = Integer.parseInt(exponent, 2);
        // 23位尾数位(MANTISSA)=00000000000000000000000
        String last = binarystr.substring(9);
        // 小数点移动位数
        int mobit = expint - 127;
        // 小数点右移18位后得10101001 01101001 110.00000
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 23; i++) {
            if (i == mobit)
                sb.append(".");
            char b = last.charAt(i);
            sb.append(b);

        }
        String valstr = "1" + sb.toString();
        int s = valstr.indexOf(".") - 1;// 指数
        Double dval = 0d;
        for (int i = 0; i < valstr.length(); i++) {
            if (valstr.charAt(i) == '.')
                continue;

            Double d = Math.pow(2, s);
            int f = Integer.valueOf(valstr.charAt(i) + "");
            // System.out.println( f );
            d = d * f;
            // System.out.println( "c:=" + valstr.charAt( i ) + ",s=" + s +
            // ",val=" + d + ", Math.pow( 2, s )=" + Math.pow( 2, s ) );
            s = s - 1;
            dval = dval + d;
        }
        if (sign.equals("1"))
            dval = 0 - dval;
        return round(dval);
    }

    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    public static Double round(double dt) {
        DecimalFormat df = new DecimalFormat(".####");
        String strdt = df.format(dt);
        return Double.parseDouble(strdt);
    }


    /**
     * 16进制转换成为string类型字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || "".equals(s)) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

}
