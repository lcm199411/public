package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    UserDao dao = new UserDaoImpl();
    @Override
    public boolean regist(User user) {
        //先查询数据库中是否有该数据
        User u = dao.findByUsername(user.getUsername());
        if (u != null){
            //数据存在,注册失败
            return  false;
        }
        //2.保存用户信息
        //2.1设置激活码，唯一字符串
        user.setCode(UuidUtil.getUuid());
        //2.2设置激活状态
        user.setStatus("N");
        //往数据库中插入数据
        dao.save(user);

        String content = "<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活【黑马旅游网】</a>";
        MailUtils.sendMail(user.getEmail(),content,"激活邮件");
        return true;
    }

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //根据激活码去数据库中查询用户信息
        User user = dao.referUser(code);
        if(user !=null){
            dao.updateUserStatus(user);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public User login(User user) {
        return  dao.findUser(user.getUsername(),user.getPassword());

    }
}
