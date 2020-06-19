package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet( "/user/*")
public class UserServlet extends BaseServlet {
    private  UserService service = new UserServiceImpl();
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //注册
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if (!check.equals(checkcode_server) || checkcode_server == null) {
            //验证码错误
            ResultInfo info = new ResultInfo();
            //注册失败,设置错误信息
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //把对象转化为json对象进行返回
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }
        //验证码正确,验证其他数据
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            //对键入的数据信息封装对象
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用service完成注册
        //UserService service = new UserServiceImpl();
        boolean flag = service.regist(user);

        ResultInfo info = new ResultInfo();

        if (flag) {
            //注册成功
            info.setFlag(true);
        } else {
            info.setFlag(false);
            info.setErrorMsg("注册失败");
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);

        //将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

    }

    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //激活
        String code = request.getParameter("code");
        if (code != null) {
           // UserService service = new UserServiceImpl();
            boolean flag = service.active(code);

            String msg = null;
            if (flag) {

                msg = "激活成功,请<a href='"+request.getContextPath()+"/login.html'>登录</a>";

            } else {
                msg = "激活失败,请联系管理员";
            }

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //登录
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if(!check.equals(checkcode_server)||checkcode_server==null){
            //验证码错误
            ResultInfo info = new ResultInfo();
            //注册失败,设置错误信息
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //把对象转化为json对象进行返回
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }
        //验证码正确, 获取页面键入数据并封装为对象
        Map<String, String[]> map = request.getParameterMap();

        User user = new User();

        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //UserService service = new UserServiceImpl();
        User u = service.login(user);

        ResultInfo info = new ResultInfo();

        if(u == null){
            //数据库中没有该用户信息,登录失败
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        if(u !=null &&u.getStatus().equals("N")){
            //有该用户信息,但是尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请激活");
        }
        if(u !=null &&u.getStatus().equals("Y")) {
            //登录成功
            info.setFlag(true);
            request.getSession().setAttribute("user",u);
        }

        //响应数据,把对象转化为json串进行返回
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //用户 欢迎实现
        Object user = request.getSession().getAttribute("user");
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),user);

    }
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();

        //跳转登录页面
        response.sendRedirect(request.getContextPath()+"/login.html");

    }
    }
