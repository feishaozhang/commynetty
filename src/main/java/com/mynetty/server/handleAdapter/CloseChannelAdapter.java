package com.mynetty.server.handleAdapter;

import com.mynetty.server.cache.SessionChannelCache;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.apache.log4j.Logger;

/**
 * TCP链接关闭时释放服务器资源
 */
public class CloseChannelAdapter extends ChannelHandlerAdapter{
    private Logger logger = Logger.getLogger(CloseChannelAdapter.class);

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        //客户端非正常退出时，需要手动关闭句柄
        logger.debug("客户端非正常退出，移除缓存数据");
        SessionChannelCache.removeSession(ctx.channel().id().asLongText());
    }
}
