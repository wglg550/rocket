package com.rocket.demo.transaction;

import com.alibaba.druid.util.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.*;

@SpringBootConfiguration
public class TransactionProducerConfiguration {
    public static final Logger LOGGER = LoggerFactory.getLogger(TransactionProducerConfiguration.class);

    /**
     * 发送同一类消息的设置为同一个group，保证唯一,默认不需要设置，rocketmq会使用ip@pid(pid代表jvm名字)作为唯一标示
     */
    @Value("${rocketmq.producer.groupName}")
    private String groupName;
    @Value("${rocketmq.producer.namesrvAddr}")
    private String namesrvAddr;
    /**
     * 消息最大大小，默认4M
     */
    @Value("${rocketmq.producer.maxMessageSize}")
    private Integer maxMessageSize;
    /**
     * 消息发送超时时间，默认3秒
     */
    @Value("${rocketmq.producer.sendMsgTimeout}")
    private Integer sendMsgTimeout;
    /**
     * 消息发送失败重试次数，默认2次
     */
    @Value("${rocketmq.producer.retryTimesWhenSendFailed}")
    private Integer retryTimesWhenSendFailed;

    @Bean
    public TransactionMQProducer getTransactionMQProducer() throws Exception {
        if (StringUtils.isEmpty(this.groupName)) {
            throw new Exception("groupName is blank");
        }
        if (StringUtils.isEmpty(this.namesrvAddr)) {
            throw new Exception("nameServerAddr is blank");
        }
        TransactionListener transactionListener = new TransactionListenerImpl();
        TransactionMQProducer producer = new TransactionMQProducer("trans" + this.groupName);
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });
//        producer.setExecutorService(executorService);
//        producer.setTransactionListener(transactionListener);
//        producer.start();
//        DefaultMQProducer producer;
//        producer = new DefaultMQProducer(this.groupName);
//        producer.setNamesrvAddr(this.namesrvAddr);
        //如果需要同一个jvm中不同的producer往不同的mq集群发送消息，需要设置不同的instanceName
        //producer.setInstanceName(instanceName);
        if (this.maxMessageSize != null) {
            producer.setMaxMessageSize(this.maxMessageSize);
        }
        if (this.sendMsgTimeout != null) {
            producer.setSendMsgTimeout(this.sendMsgTimeout);
        }
        //如果发送消息失败，设置重试次数，默认为2次
        if (this.retryTimesWhenSendFailed != null) {
            producer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
        }
        try {
            producer.setExecutorService(executorService);
            producer.setTransactionListener(transactionListener);
            producer.start();
//            for (int i = 0; i < 10; i++) {
//                try {
//                    Message msg = new Message("DemoTopic", "transDemoTag" + i, "KEY" + i, ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
//                    SendResult sendResult = producer.sendMessageInTransaction(msg, null);
//                    System.out.printf("%s%n", sendResult);
////                    Thread.sleep(10);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            System.out.println(1 / 0);
            LOGGER.info(String.format("producer is start ! groupName:[%s],namesrvAddr:[%s]"
                    , this.groupName, this.namesrvAddr));
        } catch (MQClientException e) {
            LOGGER.error(String.format("producer is error {}"
                    , e.getMessage(), e));
            throw new Exception(e);
        }
        return producer;
    }
}
