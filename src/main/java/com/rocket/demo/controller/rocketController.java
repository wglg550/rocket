package com.rocket.demo.controller;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rocket")
public class rocketController {
    private static final Logger logger = LoggerFactory.getLogger(rocketController.class);

    @Autowired
    private TransactionMQProducer transactionMQProducer;

    @GetMapping("/send")
    public void rocketTest(@RequestParam String name) throws MQClientException {
//        logger.info("开始发送消息：" + name);
//        Message sendMsg = new Message("DemoTopic", "DemoTag3", name.getBytes());
//        //默认3秒超时
//        SendResult sendResult = transactionMQProducer.sendMessageInTransaction(sendMsg, null);
//        logger.info("消息发送响应信息：" + sendResult.toString());
        logger.info("开始发送消息：" + name);
        Message sendMsg = new Message("PushTopic", "PushTag1", name.getBytes());
        //默认3秒超时
        SendResult sendResult = transactionMQProducer.sendMessageInTransaction(sendMsg, null);
        logger.info("消息发送响应信息：" + sendResult.toString());
    }
}
