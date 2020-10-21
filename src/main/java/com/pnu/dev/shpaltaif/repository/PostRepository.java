package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    List<Post> findAllByTitleContainsAndActiveTrue(String title, Pageable pageable);

    boolean existsPostsByCategoryId(Long categoryId);

    boolean existsPostsByAuthorPublicAccountId(Long authorId);

    Optional<Post> findByIdAndActiveTrue(Long id);

}
