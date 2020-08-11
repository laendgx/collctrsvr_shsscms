package com.boco.shss.tcpcomm;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.boco.shss.constant.DriverConst;
import com.boco.shss.model.Response;
import com.boco.shss.utils.CoderUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * <pre>
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——----——+
 * |  包头	|  地址          |  帧数据    |  校验码    |  包尾       |
 * +——----——+——-----——+——----——+——----——+——----——+
 * </pre>
 * @author dgx
 *
 */
public class ResponseDecoder extends ByteToMessageDecoder {
	/**
	 * 数据包基本长度
	 */
	public static int BASE_LENTH = 1 + 2 + 1 + 2 + 1;
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
						  List<Object> out) throws Exception {
		int currCnt = buffer.readableBytes();
		while(true){
			if(buffer.readableBytes() >= BASE_LENTH){
				//第一个可读数据包的起始位置
				int beginIndex;
				while(true) {
					//包头开始游标点
					beginIndex = buffer.readerIndex();
					//标记初始读游标位置
					buffer.markReaderIndex();
					if (buffer.readByte() == DriverConst.Const_FrameHead) {
						break;
					}

					//未读到包头标识略过一个字节
					buffer.resetReaderIndex();
					buffer.readByte();

					//不满足
					if(buffer.readableBytes() < BASE_LENTH){
						return;
					}
				}

				int cnt = buffer.readableBytes();
				//判断包尾
				byte frameTail = buffer.getByte(cnt);
				if (frameTail != DriverConst.Const_FrameTail){
					buffer.resetReaderIndex();
					return;
				}

				byte[] dealByteArr = new byte[buffer.readableBytes() - 1];
				buffer.readBytes(dealByteArr);

				byte[] transfer = CoderUtils.Transfer(dealByteArr, dealByteArr.length, false);

				//读取地址
				byte[] addr = new byte[2];
				ByteArrayInputStream bais = new ByteArrayInputStream(transfer);
				bais.read(addr);

				//读取数据
				byte[] data = new byte[bais.available() - 2];
				bais.read(data);

				//读取校验
				byte[] checkCrc = new byte[2];
				bais.read(checkCrc);

				//读取包尾
				buffer.readByte();
				Response response = new Response(addr, data, checkCrc);
				out.add(response);

			} else {
				break;
			}
		}
		//数据不完整，等待完整的包
		return;
	}

}
