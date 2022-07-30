package org.distributed.lock.service;

import org.distributed.lock.model.Stock;
import org.distributed.lock.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


public class MyThread implements Runnable{

    //@Autowired
    private RedisTemplate redisTemplate;
    private static Logger logger = LoggerFactory.getLogger(MyThread.class);

    private Integer reduceNumber;
    private Integer id;

    public MyThread(Integer paramNumber, Integer paramId, RedisTemplate predisTemplate){
        reduceNumber = paramNumber;
        id = paramId;
        redisTemplate = predisTemplate;
        //测试修改后再次提交gitHub MyThread类
    }

    public MyThread(){}

    @Override
    public void run() {
        Object objStock = redisTemplate.opsForValue().get(id);
        Stock redisStock = JsonUtils.parse(objStock.toString(), Stock.class);
        Integer redisStockNum = redisStock.getStock();
        logger.info("redisStockNum: " + redisStockNum);
        if(redisStockNum == 0){
            logger.info("秒杀结束");
            return;
        }

        //减库存
        Long afterReductionStock = redisTemplate.opsForValue().decrement(id, reduceNumber);
        if(afterReductionStock < 0){
            //由于库存减为负数,再从redis中获取库存, 将之前超减的库存补回
            redisStock = (Stock) redisTemplate.opsForValue().get(id);
            redisStockNum = redisStock.getStock();
            if(redisStockNum != 0 && redisStockNum < reduceNumber){
                Long makeUpStockNum = redisTemplate.opsForValue().increment(id, reduceNumber);
            }else if(redisStockNum == 0){
                redisStock.setStock(redisStockNum);
                redisTemplate.opsForValue().set(id, JsonUtils.serialize(redisStock));
            }
            redisStock = (Stock) redisTemplate.opsForValue().get(id);
            redisStockNum = redisStock.getStock();
            logger.info("redis缓存中库存数量: " + redisStockNum);
            logger.info("库存不足, 秒杀失败。。。。。。");
            return;
        }
        //秒杀成功
        Map<String, Integer> params = new HashMap<>();
        params.put("id", id);
        params.put("reduceNumber", reduceNumber);

        logger.info("id: " + id + "\t reduceNumber: " + reduceNumber);
        redisTemplate.convertAndSend("TOPIC_USERNAME", params);
    }

}
