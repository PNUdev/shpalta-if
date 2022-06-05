package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Override
    @QueryHints(@QueryHint(name = HINT_CACHEABLE, value = "true"))
    List<Category> findAll();

    @QueryHints(@QueryHint(name = HINT_CACHEABLE, value = "true"))
    Optional<Category> findByPublicUrl(String publicUrl);

    boolean existsByPublicUrl(String publicUrl);

}
