package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.impl.RouteSerivceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/rout/*")
public class RoutServlet extends BaseServlet {
    private  RouteSerivceImpl serivce = new RouteSerivceImpl();
    public void pageQuery(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rname = req.getParameter("rname");

        if ("null".equals(rname)){
            rname = "";
        }

       // rname = new String(rname.getBytes("iso-8859-1"),"utf-8");

        String currentPageStr = req.getParameter("currentPage");
        String pageSizeStr = req.getParameter("pageSize");
        String cidStr = req.getParameter("cid");
        int cid = 0;
        if(cidStr !=null && cidStr.length()>0 && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }
        int pageSize = 0; //每页显示条数
        if(pageSizeStr !=null && pageSizeStr.length()>0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else {
            pageSize = 5;
        }
        int currentPage = 0;//当前页码
        if(currentPageStr !=null && currentPageStr.length()>0){
            currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage = 1;
        }

        //调用service

        PageBean<Route> pb = serivce.pageQuery(cid,currentPage,pageSize,rname);

        try {
            writeValue(pb,resp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /*
    根据id查询一个旅游线路的详细信息
     */
    public void finOne(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //接受参数id
        String rid = req.getParameter("rid");

        //调用service查询
        Route route =serivce.findOne(rid);



        //转为json返回客户端
        writeValue(route,resp);
    }
}
