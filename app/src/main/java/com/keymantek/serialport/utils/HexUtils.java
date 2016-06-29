package com.keymantek.serialport.utils;

public class HexUtils {
	
    /**
     * byte����ת����ʮ�������ַ���
     * @param bArray Ҫת����byte����
     * @param size ����Ĵ�С
     * @return
     */
    public static final String bytesToHexString(byte[] bArray,int size) {
    	  StringBuffer sb = new StringBuffer(size);
    	  String sTemp;
    	  for (int i = 0; i < size; i++) {
	    	   sTemp = Integer.toHexString(0xFF & bArray[i]);
	    	   if (sTemp.length() < 2)
	    		   sb.append(0);
	    	   sb.append(sTemp.toUpperCase());
	    	   sb.append(" ");
    	  }
    	  return sb.toString();
    	 }
    
    /**
     * ��16�����ַ���ת�����ֽ�����
     * @param hexString
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
     int len = (hex.length() / 2);
     byte[] result = new byte[len];
     char[] achar = hex.toCharArray();
     for (int i = 0; i < len; i++) {
      int pos = i * 2;
      result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
     }
     return result;
    }
    
    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(Character.toUpperCase(c));
        return b;
     }
}
