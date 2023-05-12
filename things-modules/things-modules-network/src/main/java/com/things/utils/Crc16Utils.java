package com.things.utils;

public class Crc16Utils {

    /**
     * 一个字节包含位的数量 8
     */
    private static final int BITS_OF_BYTE = 8;

    /**
     * 多项式
     */
    private static final int POLYNOMIAL = 0xA001;

    /**
     * 初始值
     */
    private static final int INITIAL_VALUE = 0xFFFF;

    /**
     * CRC16 编码
     *
     * @param bytes 编码内容
     * @return 编码结果
     */
    public static String crc16(int[] bytes) {
        int res = INITIAL_VALUE;
        for (int data : bytes) {
            res = res ^ data;
            for (int i = 0; i < BITS_OF_BYTE; i++) {
                res = (res & 0x0001) == 1 ? (res >> 1) ^ POLYNOMIAL : res >> 1;
            }
        }
        return convertToHexString(revert(res));
    }

    public static int[] crc16Bytes(int[] bytes) {
        int res = INITIAL_VALUE;
        for (int data : bytes) {
            res = res ^ data;
            for (int i = 0; i < BITS_OF_BYTE; i++) {
                res = (res & 0x0001) == 1 ? (res >> 1) ^ POLYNOMIAL : res >> 1;
            }
        }
        String crc16 = convertToHexString(revert(res));
        int one = Integer.parseInt(crc16.substring(0, 2),16);
        int two = Integer.parseInt(crc16.substring(2),16);
        return new int[]{one, two};
    }


    /**
     * 翻转16位的高八位和低八位字节
     *
     * @param src 翻转数字
     * @return 翻转结果
     */
    private static int revert(int src) {
        int lowByte = (src & 0xFF00) >> 8;
        int highByte = (src & 0x00FF) << 8;
        return lowByte | highByte;
    }

    private static String convertToHexString(int src) {
        return Integer.toHexString(src);
    }


    public static void main(String[] args) {
        //01 05 00 01 FF 00
//        int[] data = new int[]{0x02, 0x01, 0x00, 0x01, 0x00, 0x01};
        // 01 06 00 07 00 0A
//        int[] data = new int[]{0x01, 0x06, 0x00, 0x07, 0x00, 0x0A};
        //01 05 00 01 FF 00
        //01 05 00 01 00 00 9C 0A
//        int[] data = new int[]{0x02, 0x05, 0x00, 0x01, 0x00, 0x00};
        int[] data = new int[]{0x02 , 0x03, 0x00, 0x00, 0x00, 0x04};
//        System.out.println(Crc16Utils.crc16(data).toUpperCase());
//        int[] data = new int[]{0x01, 0x10, 0x00, 0x00, 0x00, 0x04, 0x08, 0x00, 0x02, 0x25, 0x80, 0x00, 0x00, 0x00, 0x00};
        System.out.println(Crc16Utils.crc16(data).toUpperCase());
    }

    public static String int2hexString(int[] ints){
        StringBuilder sb = new StringBuilder();
        for (int anInt : ints) {
            String s = Integer.toHexString(anInt);
            if (s.length() == 1) {
                sb.append("0").append(s);
            } else {
                sb.append(s);
            }
        }

        return sb.toString();
    }


    public static byte[] getSendBytes(String content) {

        content = content.replace(" ", "");                // 去除空格
        content = content.toUpperCase();                   // 全转大写
        char[] chars = content.toCharArray();              // 转换成字符数组
        int length = chars.length / 2;
        // 将字符串先转换成整型数据
        int[] realNums = new int[length];
        for (int i = 0; i < length; i++) {
            realNums[i] = charToInt(chars[2 * i], chars[2 * i + 1]);
            System.out.print(Integer.toHexString(realNums[i]) + " ");  // 打印信息确认是否转换正确
        }
        byte[] sendBytes = intGetBytes(realNums);
        System.out.println(sendBytes.toString());
        return sendBytes;
    }

    public static int charToInt(char c1, char c2) {
        String num = "0123456789ABCDEF";
        int highBits = num.indexOf(c1);
        int lowBits = num.indexOf(c2);

        return (highBits * 16 + lowBits);
    }

    public static byte[] intGetBytes(int[] arrays) {
        byte[] bytes = new byte[arrays.length];
        for (int i = 0; i < arrays.length; i++) {
            bytes[i] = (byte) (arrays[i] & 0xff);
        }
        return bytes;
    }
}
