package org.distributed.lock.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.distributed.lock.model.Stock;

import java.util.List;

@Mapper
public interface StockMapper {

    public int updateStockNum(@Param("id")int id, @Param("needNum")int needNum);

    public void addStockTable(@Param("stock") Stock stock);

    public List<Stock> searchStocks();

    public Stock searchStock(@Param("shopId")Integer shopId);

}
