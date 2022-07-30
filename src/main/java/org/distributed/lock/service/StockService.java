package org.distributed.lock.service;

import org.distributed.lock.dao.StockMapper;
import org.distributed.lock.model.Stock;
import org.distributed.lock.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class StockService {

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    private static Logger logger = LoggerFactory.getLogger(StockService.class);

    public void operationstock(int id){
        for(int index = 0; index < 100; index++) {
            int reduceNum = 10;
            Runnable thread = new MyThread(reduceNum, 3, redisTemplate);
            new Thread(thread).start();
        }
    }


    public String operationstock(String shopId, int needNum, boolean bool){
        String currentThreadName = Thread.currentThread().getName();
        logger.info("currentThreadName: " + currentThreadName);
        ValueOperations operations = redisTemplate.opsForValue();
        Boolean ifAbsent = operations.setIfAbsent(currentThreadName, shopId, 10, TimeUnit.SECONDS);

        //BoundHashOperations<String, Object, Object> hashOperations = stringRedisTemplate.boundHashOps(currentThreadName);
        //Boolean expire = true;//hashOperations.expire(5, TimeUnit.SECONDS);
        logger.info("ifAbsent: " + ifAbsent);
        //Boolean ifAbsent = stringRedisTemplate.opsForValue().setIfAbsent(currentThreadName, shopId, 30, TimeUnit.SECONDS);
        int currentStock = 0;
        if(!ifAbsent){
            return "fail";
        }

        try {
            TimeUnit.SECONDS.sleep(11);
            //ValueOperations<String, String> stockMap = stringRedisTemplate.opsForValue();
            Object obj = operations.get(currentThreadName);
            if(obj != null){
                Integer shopIdvalue = Integer.parseInt(operations.get(currentThreadName).toString());
                logger.info("shopIdvalue: " + shopIdvalue);
                Object objStock = operations.get(shopIdvalue);
                if(objStock == null){
                    Stock stock = stockMapper.searchStock(shopIdvalue);
                    operations.set(shopIdvalue, JsonUtils.serialize(stock));
                }else{
                    Stock existsStock = JsonUtils.parse(objStock.toString(), Stock.class);
                    currentStock = existsStock.getStock();
                    logger.info("currentStock: " + currentStock);
                    if(bool){
                        currentStock = currentStock + needNum;
                    }else{
                        currentStock = currentStock - needNum;
                        if(currentStock < 0){
                            return "Insufficient inventory";
                        }
                    }
                    existsStock.setStock(currentStock);
                    operations.set(shopId, JsonUtils.serialize(existsStock));
                    logger.info("After modification currentStock: " + currentStock);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //String key = redisTemplate.opsForValue().get(currentThreadName);
            //logger.info("key: " + key);
            if(redisTemplate.opsForValue().get(currentThreadName) != null &&
                    shopId.contentEquals((CharSequence) Objects.requireNonNull(redisTemplate.opsForValue().get(currentThreadName)))){
                redisTemplate.delete(currentThreadName);
            }
            logger.info("shopId: " + shopId);
            logger.info("needNum: " + needNum);
            logger.info("bool: " + bool);
        }
        return "success";
    }

    public void addStock(){
        for(int index = 1; index < 10 ; index++) {
            int num = (int)(Math.random() * 10 + 1);
            Stock stock = new Stock(index,"", "shop_name_" + index, num, new Date());
            stockMapper.addStockTable(stock);
        }
    }

}
