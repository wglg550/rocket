package com.rocket.demo.junit;


import com.rocket.demo.DemoApplication;
import com.rocket.demo.redis.RedisLock;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = DemoApplication.class)
public class RocketTest {
    private static final Logger logger = LoggerFactory.getLogger(RocketTest.class);

//    @Autowired
//    private Consumer consumer;
//
//    @Autowired
//    private Producer producer;
//
//    @Test
//    public void sendTest() throws Exception {
//    }

    /**
     * 使用RocketMq的生产者
     */
//    @Autowired
//    private DefaultMQProducer defaultMQProducer;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private TransactionMQProducer transactionMQProducer;

//    @Test
//    public void send() throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
//        String msg = "demo msg test1";
//        logger.info("开始发送消息：" + msg);
//        Message sendMsg = new Message("DemoTopic", "DemoTag1", msg.getBytes());
//        //默认3秒超时
//        SendResult sendResult = defaultMQProducer.send(sendMsg);
//        msg = "demo msg test2";
//        sendMsg = new Message("DemoTopic", "DemoTag2", msg.getBytes());
//        //默认3秒超时
//        sendResult = defaultMQProducer.send(sendMsg);
//        logger.info("消息发送响应信息：" + sendResult.toString());
////        System.out.println(1 / 0);
//    }

    @Test
//    @Transactional(rollbackFor = Exception.class)
    public void sendTrans() throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        String msg = "demo msg test3";
        logger.info("开始发送消息：" + msg);
        Message sendMsg = new Message("DemoTopic", "DemoTag3", msg.getBytes());
        //默认3秒超时
        SendResult sendResult = transactionMQProducer.sendMessageInTransaction(sendMsg, null);
        logger.info("消息发送响应信息：" + sendResult.toString());
        msg = "demo msg test4";
        logger.info("开始发送消息：" + msg);
        sendMsg = new Message("DemoTopic", "DemoTag4", msg.getBytes());
        //默认3秒超时
        sendResult = transactionMQProducer.sendMessageInTransaction(sendMsg, null);
        logger.info("消息发送响应信息：" + sendResult.toString());
//        System.out.println(1 / 0);
//        transactionMQProducer.shutdown();
    }

    @Test
    public void testRedisLock() {
        System.out.println(redisLock.tryLock("test1", "12345", 60 * 3));
        System.out.println(redisLock.releaseLock("test1"));
    }
}
