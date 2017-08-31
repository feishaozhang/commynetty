package com.mynetty.client;

import com.mynetty.client.cache.CacheKey;
import com.mynetty.client.cache.ClientCache;
import com.mynetty.client.listener.ClientCallback;
import com.mynetty.client.model.ClientStartParams;
import com.mynetty.commom.msgpack.encoderTool.MessageSender;
import com.mynetty.commom.msgpack.encoderTool.MessageTool;
import com.mynetty.commom.msgpack.messageEnum.MessageStatusEnum;
import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import com.mynetty.commom.msgpack.model.ProtocolMessage;
import io.netty.channel.Channel;
import org.apache.log4j.Logger;

import java.util.Scanner;

public class InnerMain {
    private static Logger logger = Logger.getLogger(InnerMain.class);
    public static void main(String[]  args){
//                for (int i=0;i<1;i++){
//                    new Thread(new Runnable() {
//                        public void run() {
                            int crcCode = 0xccdc0101;
                            String auth = "2000";
                            Client client = Client.getInstance();
                            ClientStartParams parms = new ClientStartParams();
                            parms.setHost(ClientConfiguration.SERVER_HOSET);
                            parms.setPort(ClientConfiguration.SERVER_PORT);
                            parms.setCrcCode(crcCode);
                            parms.setAuth(auth);
                            try{
                                client.startWithTestPattern(parms, new ClientCallback() {
                                    public void onComplete(OpType opType, String message) {
                        sendMessage();
//                                        logger.info("当前1第："+currentCount);
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                            }
//                        }
//                    }).start();
//            }

    }
    /**
     * 发送文字板
     */
    public static void sendMessage(){
        try{
            while (true){
                System.out.println("请输入消息");
                Scanner s = new Scanner(System.in);
                String line  = s.nextLine();
                long userId = Long.parseLong(ClientCache.getCacheValue(CacheKey.USER_ID,String.class));
                ProtocolMessage pm = MessageTool.getProtocolMessage(line,userId,3000, MessageTypeEnum.MESSAGE_BUSSINESS, MessageStatusEnum.REQUEST);
                Channel channel = (Channel)ClientCache.getCacheValue(CacheKey.CONNECTED_CHANNEL);
                MessageSender.sendMessage(channel,pm);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
