package xyz.thomaslau.highlight;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

@Component("nettyClientRunner")
public class NtClientRunner implements ApplicationRunner {
	private static Logger log = LoggerFactory.getLogger(NtClientRunner.class);

	@Value("${netty.port}")
	private int port;

	@Value("${netty.ip}")
	private String ip;
	// 重连次数
	@Value("${netty.reconnectDelay}")
	private int reconnectDelay;

	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	// 创建一个单线程池用于控制台输出
	private final ExecutorService inputExecutor = createSingleThreadExecutor();

	@Override
	public void run(ApplicationArguments args) throws Exception {
		connect();
	}

	private ExecutorService createSingleThreadExecutor() {
		return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), r -> {
			Thread thread = new Thread(r);
			thread.setName("UserInputThread");
			thread.setDaemon(true);
			return thread;
		});
	}

	public void connect() {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						ChannelPipeline pipeline = socketChannel.pipeline();
						pipeline.addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("$".getBytes())));
						pipeline.addLast("decoder", new StringDecoder());
						pipeline.addLast("encoder", new StringEncoder());
//                        pipeline.addLast(new LoginServerHandler());
//                        pipeline.addLast(new HeartbeatHandler());
//                        //pipeline.addLast(new ReconnectHandler(NettyClientRunner.this));
//                        pipeline.addLast(new GroupChatClientHandler());
					}
				});
		try {
			ChannelFuture future = bootstrap.connect(ip, port);
			future.addListener((ChannelFutureListener) channelFuture -> {
				if (channelFuture.isSuccess()) {
					log.info("Connected to server at {}:{}", ip, port);
				}
				// 去掉断线重连逻辑
//                else {
//                    log.error("Connection to server failed. Scheduling reconnection in {} seconds.", reconnectDelay);
//                    channelFuture.channel().eventLoop().schedule(this::connect, reconnectDelay, TimeUnit.MINUTES);
//                }
			});
			// 控制台输出
			inputExecutor.execute(() -> handleUserInput(future.channel()));
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			log.error("Client connection interrupted", e);
		}
	}

	private void handleUserInput(Channel channel) {
		try (Scanner scanner = new Scanner(System.in)) {
			while (!Thread.currentThread().isInterrupted()) {
				if (scanner.hasNextLine()) {
					String msg = scanner.nextLine();
//                    AbstractMessage chat = MessageWrapper.wrapMessage(MessageType.CHAT, MessageType.CHAT.getCode(), msg);
//                    channel.writeAndFlush(Unpooled.copiedBuffer(chat.toByte()));
				}
			}
		} catch (Exception e) {
			log.error("Error reading user input", e);
		}
	}

	// 应用断开时关闭资源
	@PreDestroy
	public void shutdown() {
		log.info("Shutting down the Netty client gracefully...");
		workerGroup.shutdownGracefully();
		inputExecutor.shutdown();
	}
}