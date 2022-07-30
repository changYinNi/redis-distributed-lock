package org.distributed.lock.control;

import org.distributed.lock.component.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class StockRedisController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisLock redisLock;

    public void setStock(Integer id, Integer number){
        stringRedisTemplate.opsForValue().set(id.toString(), number.toString());
    }

    public String deductStock(){
//        String key = "lock";
//        String lock_value = UUID.randomUUID().toString();
//        boolean redisLock = redisLock.getRedisLock(key, lock_value);
        return "";
    }

}
