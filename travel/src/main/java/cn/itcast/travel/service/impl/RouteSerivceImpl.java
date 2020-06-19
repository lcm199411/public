package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteSerivceImpl implements RouteService {
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private  RouteDao dao = new RouteDaoImpl();
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {
        PageBean<Route> pb = new PageBean<Route>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        //总记录数
        int totalCounr = dao.findTotalCounr(cid,rname);

        pb.setTotalCount(totalCounr);

        int totalPage = (totalCounr % pageSize) == 0? (totalCounr / pageSize):(totalCounr / pageSize)+1;
        //总页数

        pb.setTotalPage(totalPage);
        //当前页显示的数据
        int start = (currentPage -1)*pageSize;
        List<Route> page = dao.findByPage(cid, start, pageSize,rname);
        pb.setList(page);

        return pb;
    }

    @Override
    public Route findOne(String rid) {
        //根据id去route表中查询route对象
        Route route = dao.findOne(Integer.parseInt(rid));

        //根据route id查询图片集合信息
        List<RouteImg> list = routeImgDao.findByRid(route.getRid());
    //把查询到的图片集合信息添加到route对象中
        route.setRouteImgList(list);

        //根据route 的sid 查询商家对象
        return null;
    }
}
