package com.things.utils;

import java.text.DecimalFormat;

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
        Double doubleByHexstr = getDoubleByHexstr("45def000");
        float v = Float.intBitsToFloat(Integer.parseInt("45def000", 16));
        System.out.println(v);
        System.out.println(hexStringToString("4D455353414745203100000000000000"));
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
