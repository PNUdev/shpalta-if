package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category findById(Long id);

    Category findByPublicUrl(String publicUrl);

    void create(CategoryDto categoryDto);

    void update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);

}
