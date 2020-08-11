package com.boco.shss.driver;

import java.io.Serializable;
/**
 * 用于注册通信端口的信息
 * @author dgx
 *
 */
public class TCommPortInfo implements Serializable {

	private static final long serialVersionUID = -3583308138889115179L;
	/**
	 * rabbitmqtong通讯交换器名称
	 */
	private String exchangeName;
	/**
	 * rabbitmqtong通讯发送队列路由key值
	 */
	private String sendQueueroutingkey;
	/**
	 * 所属编号
	 */
	private Integer dwDriverId;
	/**
	 * 设备编号
	 */
	private String szDevId;
	/**
	 * tcp通信ip地址
	 */
	private String szDevIp;
	/**
	 * tcp通信端口好
	 */
	private Integer dwDevPort;
	/**
	 * 通信设备的地址信息
	 */
	private String szAddressParam;

	/**
	 * @return the 所属驱动编号
	 */
	public Integer getDwDriverId() {
		return dwDriverId;
	}

	/**
	 * @param //所属驱动编号
	 */
	public void setDwDriverId(Integer dwDriverId) {
		this.dwDriverId = dwDriverId;
	}
	/**
	 * @return the 通信设备的地址信息
	 */
	public String getSzAddressParam() {
		return szAddressParam;
	}

	/**
	 * @param //通信设备的地址信息
	 */
	public void setSzAddressParam(String szAddressParam) {
		this.szAddressParam = szAddressParam;
	}

	/**
	 * 有参构造方法
	 * @param dwDriverId
	 * @param szAddressParam
	 */
	public TCommPortInfo(Integer dwDriverId, String szDevId, String szDevip,String dwDevPort, String szAddressParam) {
		super();
		this.dwDriverId = dwDriverId;
		this.szDevId = szDevId;
		this.szDevIp = szDevip;
		this.dwDevPort = Integer.valueOf(dwDevPort);
		this.szAddressParam = szAddressParam;
	}

	/**
	 * 无参构造方法
	 */
	public TCommPortInfo() {
		super();
	}

	/**
	 * 将实体转换为字符串
	 */
	@Override
	public String toString() {
		return "TCommPortInfo [dwDriverId=" + dwDriverId+ ", szDeviceId=" + szDevId
				+ ", szDevip=" + szDevIp + ", dwDevport=" + dwDevPort + ", szAddressParam="
				+ szAddressParam + "]";
	}


	public String getSzDevId() {
		return szDevId;
	}

	public void setSzDevId(String szDevId) {
		this.szDevId = szDevId;
	}

	public String getSzDevIp() {
		return szDevIp;
	}

	public void setSzDevIp(String szDevIp) {
		this.szDevIp = szDevIp;
	}

	public Integer getDwDevPort() {
		return dwDevPort;
	}

	public void setDwDevPort(Integer dwDevPort) {
		this.dwDevPort = dwDevPort;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getSendQueueroutingkey() {
		return sendQueueroutingkey;
	}

	public void setSendQueueroutingkey(String sendQueueroutingkey) {
		this.sendQueueroutingkey = sendQueueroutingkey;
	}
}
