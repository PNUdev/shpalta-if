package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.dto.PostDto;
import com.pnu.dev.shpaltaif.dto.filter.PostsAdminFilter;
import com.pnu.dev.shpaltaif.dto.filter.PostsPublicFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    Page<Post> findAll(User user, PostsAdminFilter postsAdminFilter, Pageable pageable);

    Page<Post> findAll(PostsPublicFilter postsPublicFilter, Pageable pageable);

    List<Post> findByTitleContains(String title, int size);

    Post findById(User user, Long id);

    Post findActiveById(Long id);

    void create(User user, PostDto postDto);

    void update(User user, Long id, PostDto postDto);

    void deactivate(User user, Long id);

    void activate(User user, Long id);

    void delete(User user, Long id);
}
