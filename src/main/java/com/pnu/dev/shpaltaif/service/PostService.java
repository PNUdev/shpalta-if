package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    Page<Post> findAll(User user, Pageable pageable);

    Page<Post> findAllArchived(User user, Pageable pageable);

    Post findById(User user, Long id);

    void create(User user, PostDto postDto);

    void update(User user, Long id, PostDto postDto);

    void deleteById(User user, Long id);
}
