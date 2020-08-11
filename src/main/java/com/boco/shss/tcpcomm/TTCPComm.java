package com.boco.shss.tcpcomm;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TCP通信类
 * @author dgx
 *
 */
public class TTCPComm implements Runnable {
	private static final Logger LOGGER= LoggerFactory.getLogger(TTCPComm.class.getName());
	//定时连接线程
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		
	/**
	 * 通讯端口状态监听器
	 */
	private IPortStatusListener portStatusListener;
	/**
	 * 数据上传监听器
	 */
	private IMessageListener messageListener;
	
	/**
	 * 线程池
	 */
	private NioEventLoopGroup group = new NioEventLoopGroup();
	/**
	 * 服务类
	 */
	private Bootstrap bootstrap;
	/**
	 * 状态监听标识
	 */
    private ChannelFutureListener channelFutureListener = null;
    /**
     * 会话
     */
    private Channel channel;

    private String host_;  
	private int port_;  
    /**
     * 关闭标识
     */
    private boolean isCloseFlag = false;
    
    //构造方法
    public TTCPComm(String serverIP, int port, IPortStatusListener portStatusListener, IMessageListener messageListener) {
    	this.host_ = serverIP;
    	this.port_ = port;
    	
		this.portStatusListener = portStatusListener;
		this.messageListener = messageListener;
		initClient();
    }

    /**
     *  初始化客户端
     */
    private void initClient() {   
    	bootstrap = null;
        // Client服务启动器 3.x的ClientBootstrap
        // 改为Bootstrap，且构造函数变化很大，这里用无参构造。
        bootstrap = new Bootstrap();
        // 指定EventLoopGroup
        bootstrap.group(group);
        // 指定channel类型
        bootstrap.channel(NioSocketChannel.class);
        // 指定Handler
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                /**
                 * 变换驱动时需要修改的地方
                 * 1、修改编码
                 * 2、修改解码
                 * 3、修改处理handler
                 */
            	ch.pipeline().addLast(new RequestEncoder());
                ch.pipeline().addLast(new ResponseDecoder());
                ch.pipeline().addLast(new MsgDealHandler(messageListener));
            }
        });
        //设置TCP协议的属性
        //bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		// bootstrap.option(ChannelOption.SO_TIMEOUT, 5000);
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

		channelFutureListener = new ChannelFutureListener() {
			public void operationComplete(ChannelFuture f) throws Exception {
				if (f.isSuccess()) {
					//上传端口状态
	            	portStatusListener.onPortStatusChanged(true);
					LOGGER.info("Host-" + host_ + ", Port-" + port_ + " 连接服务器成功");
				} else {
					LOGGER.error("Host-" + host_ + ", Port-" + port_ + " 连接服务器失败");
				}
			}
		};
    }

    /**
     * 连接到服务端
     */
    private void doConnect() {    	
    	//LOGGER.info("Host-" + host_ + ", Port-" + port_ + " doConnect");
        ChannelFuture future = null;
        if (channel != null){
        	try{
        		channel.close();
        	}catch(Exception ex){
        		ex.printStackTrace();
        	}
        }
        try {
            future = bootstrap.connect(new InetSocketAddress(
                    host_, port_));
            future.awaitUninterruptibly();
            //添加监听器
            future.addListener(channelFutureListener);
            
            channel = future.channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            //上传端口状态
        	portStatusListener.onPortStatusChanged(false);
        	LOGGER.error("Host-" + host_ + ", Port-" + port_ + "关闭连接");
        } finally{
        	if (!isCloseFlag){
        		LOGGER.error("连接已经断开，进入重连操作");
        		//上传端口状态
            	portStatusListener.onPortStatusChanged(false);
	        	
	        	executor.execute(new Runnable(){	        		
					@Override
					public void run() {
						try {
							TimeUnit.SECONDS.sleep(30);
						} catch (InterruptedException e) {
							LOGGER.error(e.getMessage());
						}
						
						doConnect();
					}});
        	}
        }
    }
    
    /**
     * 下发数据
     * @param //msg
     */
    public void sendMsg(Object request){
    	try{
    		if (channel.isActive()){
    			channel.writeAndFlush(request);
    		}
    	}catch(Exception ex){
    		LOGGER.error("Host-" + host_ + ", Port-" + port_ + " 下发数据失败！");
    	}
    }

	/**
	 * 设置退出标识
	 */
	public void setCloseFlag(boolean isCloseFlag) {
		this.isCloseFlag = isCloseFlag;
	}


	public void shutdown(){
    	this.isCloseFlag = true;
    	if (channel != null){
    		try{
    			channel.close();
    		}catch(Exception e){}
    	}
    	
    	try{
    		group.shutdownGracefully();
    	}catch(Exception e){}
    	
    	try{
    		executor.shutdownNow();
    	}catch(Exception e){}
    }

	/**
	 * 连接端口
	 */
	@Override
	public void run() {
		doConnect();		
	}
}
