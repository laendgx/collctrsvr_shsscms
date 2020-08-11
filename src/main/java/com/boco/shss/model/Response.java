package com.boco.shss.model;

import java.io.ByteArrayOutputStream;

import com.boco.shss.utils.CoderUtils;

public class Response {
	/**
	 * 地址
	 */
	private byte[] addr;
	
	/**
	 * 数据
	 */
	private byte[] data;
	
	/**
	 * 检验码
	 */
	private byte[] checkCrc;

	/**
	 * 构造函数
	 * @param addr
	 * @param data
	 * @param checkCrc
	 */
	public Response(byte[] addr, byte[] data, byte[] checkCrc) {
		super();
		this.addr = addr;
		this.data = data;
		this.checkCrc = checkCrc;
	}
	
	/**
	 * 将地址转换为字符串，并返回
	 * @return
	 */
	public String getAddr(){
		if (this.addr == null){
			return null;
		}
		String result = new String(this.addr);
		return result;
	}
	
	/**
	 * 将设备返回的数据转换为字符串，并返回
	 * @return
	 */
	public String getData(){
		if (this.data == null){
			return null;
		}
		String result = new String(this.data);
		return result;
	}
	
	/**
	 * 以字节数组形式，返回接收到的数据
	 * @return
	 */
	public byte[] getDataArray(){		
		return this.data;
	}
	
	/**
	 * 判断是否符合校验
	 * @return
	 */
	public boolean isChecked(){
		if (this.addr == null || this.data == null || this.checkCrc == null || this.checkCrc.length < 2){
			return false;
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write(this.addr);
			baos.write(this.data);
			byte[] buffer = baos.toByteArray();
			
			short newCheckValue = CoderUtils.gen_crc(buffer, buffer.length);
			short oldCheckValue = (short)(this.checkCrc[0] << 8 + this.checkCrc[1]);
			
			if (newCheckValue == oldCheckValue){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return false;		
	}
}
