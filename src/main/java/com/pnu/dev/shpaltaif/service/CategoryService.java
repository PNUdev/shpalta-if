package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category findById(Long id);

    void create(Category category);

    void update(Long id, Category category);

    void deleteById(Long id);

}
