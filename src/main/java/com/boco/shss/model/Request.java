package com.boco.shss.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Request {
	/**
	 * 设备变量的索引
	 */
	private String index;
	/**
	 * 地址
	 */
	private int addr;
	
	/**
	 * 帧类型
	 */
	private int frameType;
	
	/**
	 * 数据内容
	 */
	private byte[] data;
	
	/**
	 * @return the 设备变量的索引
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param //设备变量的索引
	 */
	public void setIndex(String index) {
		this.index = index;
	}
	
	/**
	 * @return the 帧类型
	 */
	public int getFrameType() {
		return frameType;
	}

	//构造方法
	public Request(int addr, int frameType, byte[] data){
		this.addr = addr;
		this.frameType = frameType;
		this.data = data;
	}
	
	/**
	 * 获取地址、帧类型、发送数据三方编码后的数据
	 * @return
	 */
	public byte[] getEncoderData(){
		String addr = String.format("%02d", this.addr);
		String frameType = String.format("%02d", this.frameType);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write(addr.getBytes("GBK"));
			baos.write(frameType.getBytes("GBK"));
			baos.write(data);
			
		} catch (IOException e) {			
			e.printStackTrace();
			return null;
		}
		
		return baos.toByteArray();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
			try {
				return "Request [addr=" + addr + ", frameType=" + frameType + ", data="
						+ new String(data,"GBK") + "]";
			} catch (UnsupportedEncodingException e) {				
				e.printStackTrace();
				return null;
			}
		
	}
		
	
}
