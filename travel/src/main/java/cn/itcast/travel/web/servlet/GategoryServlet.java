package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.GategoryService;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.GategoryServiceImpl;
import cn.itcast.travel.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class GategoryServlet extends BaseServlet {
   private GategoryService gory = new GategoryServiceImpl();

    public void findAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Category> list = gory.findAll();
        writeValue(list,response);
    }
}
