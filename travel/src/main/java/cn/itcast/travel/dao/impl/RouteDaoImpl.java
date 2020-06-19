package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public int findTotalCounr(int cid, String rname) {
        /*try {
            String sql="select count(*) from tab_route where cid =?";
            return template.queryForObject(sql,Integer.class,cid);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return 0;
        }*/
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();
        if(cid !=0){
        sb.append(" and cid = ? ");
        params.add(cid);
        }
        if(rname != null && rname.length()>0){
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");
        }
        sql = sb.toString();
        return  template.queryForObject(sql,Integer.class,params.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize,String rname) {
        /*try {
            String sql = "select * from tab_route where cid = ? limit ?,?";
            return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),cid,start,pageSize);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return  null;
        }*/

        String sql =" select * from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();
        if(cid !=0){
            sb.append(" and cid = ? ");
            params.add(cid);
        }
        System.out.println(rname);
        if(rname != null && rname.length()>0){
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");
        }
        System.out.println(rname);
            sb.append(" limit ? , ?  ");//分页条件

        params.add(start);
        params.add(pageSize);

        sql = sb.toString();
        return  template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),params.toArray());
    }

    @Override
    public Route findOne(int rid) {
        String sql ="select * from tab_route where rid = ?";

        return template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class),rid);
    }
}
