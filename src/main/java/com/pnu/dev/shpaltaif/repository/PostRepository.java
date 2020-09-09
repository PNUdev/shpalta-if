package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsPostsByCategoryId(Long categoryId);

}
