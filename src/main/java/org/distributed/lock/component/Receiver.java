package org.distributed.lock.component;

import org.distributed.lock.dao.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Receiver implements MessageListener {

    @Autowired
    private StockMapper stockMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String content = message.toString();
        //Map params = JSONObject.
        System.out.println("content: " + content);
    }

}
