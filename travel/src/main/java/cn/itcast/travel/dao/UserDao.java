package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    User findByUsername(String username);

    void save(User user);

    User referUser(String code);

    void updateUserStatus(User user);

    User findUser(String username, String password);
}
