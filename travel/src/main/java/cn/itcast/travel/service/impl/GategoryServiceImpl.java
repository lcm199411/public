package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.GategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GategoryServiceImpl implements GategoryService {
    private CategoryDao dao = new CategoryImpl();
    @Override
    public List<Category> findAll() {
        Jedis jedis = JedisUtil.getJedis();
        //Set<String> category = jedis.zrange("category", 0, -1);

        //查询数据库中的(cid)和(cname)
        Set<Tuple> category = jedis.zrangeWithScores("category", 0, -1);

        List<Category> list=null;
        if(category ==null || category.size()==0){

            //获取表格中所有数据封装到list集合中
            list =  dao.findAll();
            //把获取到的数据 存入缓存当中
            for (int i = 0; i < list.size(); i++) {
                jedis.zadd("category",list.get(i).getCid(),list.get(i).getCname());

            }

        }else {


            list = new ArrayList<Category>();
            for (Tuple tuple : category) {
                Category category1 = new Category();

                category1.setCname(tuple.getElement());
                category1.setCid((int)tuple.getScore());
                list.add(category1);
            }

        }
       /* List<Category> list = dao.findAll();*/
        return list ;
    }
}
