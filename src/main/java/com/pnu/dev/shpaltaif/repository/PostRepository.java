package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsPostsByCategoryId(Long categoryId);

    boolean existsPostsByAuthorPublicAccountId(Long authorId);

    Page<Post> findAllByAuthorPublicAccountIdAndActive(Long authorId, boolean active, Pageable pageable);

    Page<Post> findAllByActive(boolean active, Pageable pageable);

    Optional<Post> findByIdAndAuthorPublicAccountId(Long id, Long authorId);

}
