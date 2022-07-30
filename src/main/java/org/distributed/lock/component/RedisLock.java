package org.distributed.lock.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisLock {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private long acquireTimeout = 10 * 1000;        //获取锁的等待时间
    private int timeout = 20;                       //获取锁之后的超时时间, 防止死锁

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //获取分布式锁, 返回锁标识
    public Boolean getRedisLock(String lockname, String lockvalue){
        //计算获取锁的时间
        Long endTime = System.currentTimeMillis() + acquireTimeout;
        //尝试获取锁
        while(System.currentTimeMillis() < endTime){
            //成功获取锁之后设置过期时间
            //让设置key和设置key的有效时间都可以同时执行
            boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockname, lockvalue, timeout, TimeUnit.SECONDS);
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

}
