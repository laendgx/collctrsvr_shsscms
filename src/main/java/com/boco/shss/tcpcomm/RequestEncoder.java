package com.boco.shss.tcpcomm;

import java.io.ByteArrayOutputStream;

import com.boco.shss.model.Request;
import com.boco.shss.constant.DriverConst;
import com.boco.shss.utils.CoderUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
 * <pre>
 * 数据包格式
 * +——----——+——-----——+——-----——+——----——+——----——+——----——+
 * |  包头	|  地址          |  帧类型      |  帧数据    |  校验码    |  包尾       |
 * +——----——+——-----——+——-----——+——----——+——----——+——----——+
 * </pre>
 * @author dgx
 *
 */
public class RequestEncoder extends MessageToByteEncoder<Request> {
	@Override
	protected void encode(ChannelHandlerContext ctx, Request msg, ByteBuf out)
			throws Exception {
		byte[] data = msg.getEncoderData();

		//计算校验
		short crc = CoderUtils.gen_crc(data, data.length);
		byte[] crcArr = new byte[2];
		crcArr[0] = (byte)(crc >> 8);
		crcArr[1] = (byte) crc;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(data);
		baos.write(crcArr);

		byte[] byteArray = baos.toByteArray();
		byte[] transfer = CoderUtils.Transfer(byteArray, byteArray.length, true);

		out.writeByte(DriverConst.Const_FrameHead);
		out.writeBytes(transfer);
		out.writeByte(DriverConst.Const_FrameTail);

	}

}
