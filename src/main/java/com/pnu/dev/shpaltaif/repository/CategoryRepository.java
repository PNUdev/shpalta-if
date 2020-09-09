package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
