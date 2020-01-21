/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rocket.demo.transaction;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionListenerImpl implements TransactionListener {
    private AtomicInteger transactionIndex = new AtomicInteger(0);

    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

//    public static void main(String[] args) {
//        for (int i = 0; i < 10; i++) {
//            System.out.println("i value:" + i + ">>>>" + i % 3);
//        }
//    }

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        int value = transactionIndex.getAndIncrement();
        int status = value % 3;
//        int status = 1;
        localTrans.put(msg.getTransactionId(), status);
        return LocalTransactionState.UNKNOW;
//        String msgBody;
//        //执行本地业务的时候，再插入一条数据到事务表中，供checkLocalTransaction进行check使用，避免doBusinessCommit业务成功，但是未返回Commit
//        try {
//            msgBody = new String(msg.getBody(), "utf-8");
//            doBusinessCommit(msg.getKeys(), msgBody);
//            return LocalTransactionState.COMMIT_MESSAGE;
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return LocalTransactionState.ROLLBACK_MESSAGE;
//
//        }
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        Integer status = localTrans.get(msg.getTransactionId());
        if (null != status) {
            switch (status) {
                case 0:
                    return LocalTransactionState.UNKNOW;
                case 1:
                    return LocalTransactionState.COMMIT_MESSAGE;
                case 2:
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                default:
                    return LocalTransactionState.COMMIT_MESSAGE;
            }
        }
        return LocalTransactionState.COMMIT_MESSAGE;
//        Boolean result = checkBusinessStatus(msg.getKeys());
//        if (result) {
//            return LocalTransactionState.COMMIT_MESSAGE;
//        } else {
//            return LocalTransactionState.ROLLBACK_MESSAGE;
//        }
    }

    public static void doBusinessCommit(String messageKey, String msgbody) {
        System.out.println("do something in DataBase");
        System.out.println("insert 事务消息到本地消息表中，消息执行成功，messageKey为：" + messageKey);
    }

    public static Boolean checkBusinessStatus(String messageKey) {
        System.out.println(messageKey);
        if (true) {
            System.out.println("查询数据库 messageKey为" + messageKey + "的消息已经消费成功了，可以提交消息");
            return true;
        } else {
            System.out.println("查询数据库 messageKey为" + messageKey + "的消息不存在或者未消费成功了，可以回滚消息");
            return false;
        }
    }
}
