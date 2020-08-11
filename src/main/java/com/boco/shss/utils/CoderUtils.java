package com.boco.shss.utils;

import java.util.ArrayList;
import java.util.List;

import com.boco.shss.constant.DriverConst;

public class CoderUtils {
	
	/**
	 * CRC校验
	 * @param buffer
	 * @param len
	 * @return
	 */
	public static short gen_crc(byte[] buffer, int len){
		Short result = 0;
		Short Cks;
		
		 for (int i = 0; i <= len - 1; i++)
         {
			 Cks = (short)buffer[i];
             Cks = (short)(Cks << 8);
             result = (short)(result ^ Cks);

             for (int j = 0; j <= 7; j++)
             {
                 if ((result & 0x8000) != 0)
                 {
                     result = (short)(result << 1);
                     result = (short)(result ^ 0x1021);
                 }
                 else
                 {
                     result = (short)(result << 1);
                 }
             }
             result = (short)(result & 0xFFFF);
         }
		
		return result;
	}
	
	/**
	 * 加转义或者减转义
	 * @param buffer
	 * @param Len
	 * @param Xmit
	 * true为加转义
	 * false为减转义
	 * @return
	 */
	public static byte[] Transfer(byte[] buffer, int Len, boolean Xmit)
	{
		int TempLen;
        List<Byte> TempBuf = new ArrayList<Byte>();
        
        //加转义
        if (Xmit) 
        {
            TempLen = Len;
            Len = 0;

            for (int I = 0; I <= TempLen - 1; I++)
            {
                if ((buffer[I] == DriverConst.Const_FrameHead) || (buffer[I] == DriverConst.Const_FrameTail) || (buffer[I] == DriverConst.Const_FrameTrans))
                {
                    TempBuf.add(DriverConst.Const_FrameTrans);
                    Len += 1;
                    TempBuf.add((byte)(buffer[I] - DriverConst.Const_FrameTrans));
                }
                else
                {
                    TempBuf.add(buffer[I]);
                }
                Len += 1;
            }
        }
        else
        {            
            TempLen = Len;
            Len = 0;
            int J = 0;

            while (J <= (TempLen - 1))
            {
                if (buffer[J] == DriverConst.Const_FrameTrans)
                {
                    TempBuf.add((byte)(buffer[J + 1] + DriverConst.Const_FrameTrans));
                    J += 1;
                }
                else
                {
                    TempBuf.add(buffer[J]);
                }
                Len += 1;
                J += 1;
            }
        }
        
        int size = TempBuf.size();       
        byte[] result = new byte[size];
        for(int k = 0; k<size;k++){
        	result[k] = TempBuf.get(k);
        }
		return result;
	}
	
	/**
	 * 将字节数组转换为16进制字符串
	 * @return
	 */
	public static String ByteArray2HexString(byte[] buffer){
		List<String> list = new ArrayList<String>();
		for(int i=0;i<buffer.length;i++){
			list.add(String.format("%02x", buffer[i]));
		}
		return list.toString();
	}
	
	/**
	 * 将整数转换为两位的字符串
	 * @param value
	 * @return
	 */
	public static String IntTo2SizeString(int value){
		String sz = String.format("%02d", value);
		return sz;
	}
}
