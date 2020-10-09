package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByPublicUrl(String publicUrl);

    boolean existsByPublicUrl(String publicUrl);

}
