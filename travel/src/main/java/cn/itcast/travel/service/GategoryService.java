package cn.itcast.travel.service;

import cn.itcast.travel.domain.Category;

import java.util.List;

public interface GategoryService {
    List<Category> findAll();
}
