package org.distributed.lock.component;

import org.distributed.lock.dao.StockMapper;
import org.distributed.lock.model.Stock;
import org.distributed.lock.util.JsonUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebListener implements InitializingBean {

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    //private Integer id;

    public WebListener(){}

    //public WebListener(Integer paramId){
        //id = paramId;
    //}

    @Override
    public void afterPropertiesSet() throws Exception {
//        List<Stock> stocks = stockMapper.searchStocks();
//        //Stock stock = stockMapper.searchStock(id);
//        for(Stock item: stocks){
//            redisTemplate.opsForValue().set(item.getId(), JsonUtils.serialize(item));
//        }
    }

}
