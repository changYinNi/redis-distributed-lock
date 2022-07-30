package org.distributed.lock;

import static org.junit.Assert.assertTrue;

import org.distributed.lock.dao.StockMapper;
import org.distributed.lock.model.Stock;
import org.distributed.lock.service.MyThread;
import org.distributed.lock.service.StockRedisService;
import org.distributed.lock.service.StockService;
import org.distributed.lock.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * Unit test for simple App.
 */
@SpringBootTest(classes = Main.class)
@RunWith(SpringRunner.class)
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRedisService stockRedisService;

//    @Autowired
//    private RedisTemplate redisTemplate;

    @Autowired
    private StockMapper stockMapper;

    @Test
    public void addStock() {
        stockService.addStock();
    }

    @Test
    public void testRedis1() {
//        String currentThreadName = Thread.currentThread().getName();
//        boolean b = redisTemplate.opsForValue().setIfAbsent(currentThreadName,
//                "testValue", 10, TimeUnit.SECONDS);
//        System.out.println("b : " + b);
//        Object obj = redisTemplate.opsForValue().get(currentThreadName);
//        System.out.println(obj.toString());
//
//        redisTemplate.opsForValue().set(100000, "fork");
//
//        b = redisTemplate.delete(currentThreadName);
//        System.out.println("b : " + b);
//        Object objv = redisTemplate.opsForValue().get(currentThreadName);
//        System.out.println(objv.toString());
    }

    @Test
    public void testRedis2() {
//        String currentThreadName = Thread.currentThread().getName();
//        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(currentThreadName);
//
//        if(hashOperations.hasKey("boundkey")){
//            Object boundkey = hashOperations.get("boundkey");
//            System.out.println("boundkey: " + boundkey);
//        }else{
//            Stock stock = stockMapper.searchStock("b3fc4bfa-e955-11ec-b889-000c29c80808");
//            hashOperations.put("boundkey", JsonUtils.serialize(stock));
//        }
//
//        Boolean expire = hashOperations.expire(30, TimeUnit.SECONDS);
//        System.out.println("expire: " + expire);
//
//        boolean b = redisTemplate.delete(currentThreadName);
//        System.out.println("b: " + b);
//
//        if(hashOperations.hasKey("boundkey")){
//            Object boundkey = hashOperations.get("boundkey");
//            System.out.println("boundkey: " + boundkey);
//        }
    }

    @Test
    public void redisOperate1() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println();
            }
        });

//        Thread t2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String res = stockService.operationstock("b3fc4bfa-e955-11ec-b889-000c29c80808", 1, false);
//                System.out.println("result: " + res);
//            }
//        }).start();

        t1.start();
        //t2.start();

        String res = stockService.operationstock("b3fc4bfa-e955-11ec-b889-000c29c80808", 1, false);
        System.out.println("result: " + res);

    }

    @Test
    public void redisOperate2() {
        //StockService service = new StockService();
        stockService.operationstock(3);
    }

    @Test
    public void redisOperate3() {
        //StockService service = new StockService();
        for(int index = 0; index < 100; index++) {
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    String res = stockRedisService.deductStock("3", 1);
                    System.out.println("result: " + res);
                }
            });
            t2.start();
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        String res = stockRedisService.deductStock("3", 1);
//        System.out.println("result: " + res);
    }

    @Test
    public void testjson(){
//        String s = "{\"id\":3,\"shopid\":\"b3fc4bfa-e955-11ec-b889-000c29c80808\",\"shopname\":\"shop_name_3\",\"stock\":10,\"created\":1654860391000}";
//        Stock stock = JsonUtils.parse(s, Stock.class);
//        System.out.println(stock);
        stockRedisService.setStock("3");
    }

}