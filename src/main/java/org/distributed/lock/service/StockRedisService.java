package org.distributed.lock.service;

import org.distributed.lock.component.RedisLock;
import org.distributed.lock.dao.StockMapper;
import org.distributed.lock.model.Stock;
import org.distributed.lock.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class StockRedisService {

    private long acquireTimeout = 10 * 1000;        //获取锁的等待时间
    private int timeout = 20;                       //获取锁之后的超时时间, 防止死锁

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private StockMapper stockMapper;

    private static Logger logger = LoggerFactory.getLogger(StockRedisService.class);

    //获取分布式锁, 返回锁标识
    public Boolean getRedisLock(String lockname, String lockvalue){
        //计算获取锁的时间
        Long endTime = System.currentTimeMillis() + acquireTimeout;
        //尝试获取锁
        while(System.currentTimeMillis() < endTime){
            //成功获取锁之后设置过期时间
            //让设置key和设置key的有效时间都可以同时执行
            boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockname, lockvalue, timeout, TimeUnit.SECONDS);
            logger.info("result: " + result);
            if(result){
                return true;
            }
        }
        return false;
    }

    //释放分布式锁
    public void unRedisLock(String lockname, String lockvalue){
        if(lockvalue.equalsIgnoreCase(stringRedisTemplate.opsForValue().get(lockname))){
            stringRedisTemplate.delete(lockname);
        }
    }

    public void setStock(String id){
        Stock stock = stockMapper.searchStock(3);
        stringRedisTemplate.opsForValue().set(id, JsonUtils.serialize(stock));
    }

    public String deductStock(String id, Integer number){
        String key = "lock";
        String lock_value = UUID.randomUUID().toString();
        try {
            boolean redisLock = getRedisLock(key, lock_value);
            if(redisLock){
                //获取redis中商品数量
                String objStock = stringRedisTemplate.opsForValue().get(id);
                logger.info("objStock....." + objStock);
                //objStock = objStock.substring(1, objStock.length() - 1);
                Stock stock = JsonUtils.parse(objStock, Stock.class);
                Integer stockNum = stock.getStock();
                logger.info("stockNum....." + stockNum);
                //减库存
                if(stockNum > 0){
                    Integer realStock = stockNum - number;
                    stock.setStock(realStock);
                    stringRedisTemplate.opsForValue().set(id, JsonUtils.serialize(stock));
                    logger.info("商品扣减成功，剩余商品: " + realStock);
                }else{
                    logger.info("库存不足.....");
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            unRedisLock(key, lock_value);
            logger.info("解除lock: " + lock_value);
        }
        return "success";
    }
    //https://www.bbsmax.com/A/kjdwa2ArJN/
}


