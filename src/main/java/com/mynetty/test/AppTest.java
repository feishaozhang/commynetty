package com.mynetty.test;

import com.mynetty.client.Client;
import com.mynetty.client.ClientConfiguration;
import com.mynetty.client.listener.ClientCallback;
import com.mynetty.client.model.ClientStartParams;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.log4j.Logger;

import java.util.Random;

/**
 * Unit test for simple App.
 */
public class AppTest extends AbstractJavaSamplerClient {
    private Logger logger = Logger.getLogger(AppTest.class);

    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        final SampleResult sp = new SampleResult(); //采样结果
        sp.sampleStart();
        try {
            int crcCode = 0xccdc0101;
            final Client client = Client.getInstance();
            final ClientStartParams parms = new ClientStartParams();
            parms.setHost(ClientConfiguration.SERVER_HOSET);
            parms.setPort(ClientConfiguration.SERVER_PORT);
            parms.setCrcCode(crcCode);
            Random r = new Random();
            long auth = r.nextInt(1000000);
            parms.setAuth(auth+"");
            new Thread(new Runnable() {
                public void run() {
                    client.startWithTestPattern(parms, new ClientCallback() {
                        public void onComplete(OpType opType, String message) {
//                        sendMessage();
                            sp.setSuccessful(true);
                            sp.setResponseMessage(message);
                        }
                    });
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            sp.setSuccessful(false);
            sp.setResponseMessage(e.getMessage());
        }
        sp.sampleEnd(); //采用结束
        return sp;
    }
}