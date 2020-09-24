package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.PostDto;
import com.pnu.dev.shpaltaif.exception.ServiceAdminException;
import com.pnu.dev.shpaltaif.repository.CategoryRepository;
import com.pnu.dev.shpaltaif.repository.PostRepository;
import com.pnu.dev.shpaltaif.repository.PublicAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final CategoryRepository categoryRepository;

    private final PublicAccountRepository publicAccountRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, CategoryRepository categoryRepository, PublicAccountRepository publicAccountRepository) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.publicAccountRepository = publicAccountRepository;
    }

    @Override
    public Page<Post> findAll(User user, Pageable pageable) {
        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            return postRepository.findAllByActive(Boolean.TRUE, pageable);
        } else {

            PublicAccount publicAccount = publicAccountRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ServiceAdminException("Акаунт не знайдено"));
            return postRepository.findAllByAuthorPublicAccountIdAndActive(publicAccount.getId(), Boolean.TRUE, pageable);
        }
    }

    @Override
    public Page<Post> findAllArchived(User user, Pageable pageable) {
        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            return postRepository.findAllByActive(Boolean.FALSE, pageable);
        }
        PublicAccount publicAccount = publicAccountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ServiceAdminException("Акаунт не знайдено"));


        return postRepository.findAllByAuthorPublicAccountIdAndActive(publicAccount.getId(), Boolean.FALSE, pageable);

    }


    @Override
    public Post findById(User user, Long id) {
        Optional<Post> optionalPost;
        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            optionalPost = postRepository.findById(id);
        } else {
            PublicAccount publicAccount = publicAccountRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ServiceAdminException("Акаунт не знайдено"));

            optionalPost = postRepository.findByIdAndAuthorPublicAccountId(id, publicAccount.getId());
        }
        return optionalPost.orElseThrow(() -> new ServiceAdminException("Пост не знайдено"));

    }

    @Override
    public void create(User user, PostDto postDto) {

        PublicAccount publicAccount = publicAccountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ServiceAdminException("Акаунт не знайдено"));

        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(() -> new ServiceAdminException("Категорію не знайдено"));

        Post post = Post.builder()
                .authorPublicAccount(publicAccount)
                .active(Boolean.TRUE)
                .category(category)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .pictureUrl(postDto.getPictureUrl())
                .createdAt(LocalDateTime.now())
                .build();

        postRepository.save(post);

    }

    @Override
    public void update(User user, Long id, PostDto postDto) {

        Post postFromDb = findById(user, id);

        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(() -> new ServiceAdminException("Категорію не знайдено"));

        Post updatedPost = postFromDb.toBuilder()
                .title(postDto.getTitle())
                .pictureUrl(postDto.getPictureUrl())
                .category(category)
                .content(postDto.getContent())
                .build();

        postRepository.save(updatedPost);
    }

    @Override
    public void deleteById(User user, Long id) {

        Post post = findById(user, id);

        if (post.isActive()) {
            Post updatedPost = post.toBuilder()
                    .active(Boolean.FALSE)
                    .build();

            postRepository.save(updatedPost);
            return;
        }

        postRepository.deleteById(id);
    }
}

























