package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.PostDto;
import com.pnu.dev.shpaltaif.dto.filter.PostsAdminFilter;
import com.pnu.dev.shpaltaif.dto.filter.PostsPublicFilter;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.repository.CategoryRepository;
import com.pnu.dev.shpaltaif.repository.PostRepository;
import com.pnu.dev.shpaltaif.repository.specification.PostSpecificationBuilder;
import com.pnu.dev.shpaltaif.service.telegram.TelegramNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final CategoryRepository categoryRepository;

    private final PostSpecificationBuilder postSpecificationBuilder;

    private final TelegramNotificationService telegramNotificationService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           CategoryRepository categoryRepository,
                           PostSpecificationBuilder postSpecificationBuilder,
                           TelegramNotificationService telegramNotificationService) {

        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.postSpecificationBuilder = postSpecificationBuilder;
        this.telegramNotificationService = telegramNotificationService;
    }

    @Override
    public Page<Post> findAll(User user, PostsAdminFilter postsAdminFilter, Pageable pageable) {

        Specification<Post> specification = postSpecificationBuilder.buildPostSpecification(user, postsAdminFilter);
        return postRepository.findAll(specification, pageable);
    }

    @Override
    public Page<Post> findAll(PostsPublicFilter postsPublicFilter, Pageable pageable) {

        Specification<Post> specification = postSpecificationBuilder.buildPostSpecification(postsPublicFilter);

        return postRepository.findAll(specification, pageable);
    }

    @Override
    public List<Post> findTop5ByTitleContains(String title) {
        return postRepository.findTop5ByTitleContainsAndActiveTrue(title.trim());
    }

    @Override
    public Post findActiveById(Long id) {
        return postRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ServiceException("Пост не знайдено"));
    }

    @Override
    public Post findById(User user, Long id) {
        Post post = findById(id);
        checkUserAccessPermissions(post, user);
        return post;
    }

    @Override
    public void create(User user, PostDto postDto) {
        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(() -> new ServiceException("Категорію не знайдено"));

        Post post = Post.builder()
                .authorPublicAccount(user.getPublicAccount())
                .active(Boolean.TRUE)
                .category(category)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .pictureUrl(postDto.getPictureUrl())
                .createdAt(LocalDateTime.now())
                .build();

        Post savedPost = postRepository.save(post);

        if (postDto.isSendTelegramNotifications()) {
            telegramNotificationService.sendNotificationsOfNewPost(savedPost);
        }
    }

    @Override
    public void update(User user, Long id, PostDto postDto) {
        Post postFromDb = findById(id);
        checkWriterAccessPermissions(postFromDb, user);

        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(() -> new ServiceException("Категорію не знайдено"));
        Post updatedPost = postFromDb.toBuilder()
                .title(postDto.getTitle())
                .pictureUrl(postDto.getPictureUrl())
                .category(category)
                .content(postDto.getContent())
                .active(postDto.isActive())
                .build();
        postRepository.save(updatedPost);
    }

    @Override
    public void activate(User user, Long id) {
        Post post = findById(id);
        checkUserAccessPermissions(post, user);

        Post updatedPost = post.toBuilder()
                .active(Boolean.TRUE)
                .build();
        postRepository.save(updatedPost);
    }

    @Override
    public void delete(User user, Long id) {
        if (user.getRole() == UserRole.ROLE_EDITOR) {
            throw new ServiceException("Редактор не може видалити пост назавжди");
        }
        Post post = findById(id);
        checkUserAccessPermissions(post, user);
        if (post.isActive()) {
            throw new ServiceException("Пост повинен бути переміщеним в архів перед видаленням");
        }
        postRepository.deleteById(id);
    }

    @Override
    public void deactivate(User user, Long id) {
        Post post = findById(id);
        checkUserAccessPermissions(post, user);
        Post updatedPost = post.toBuilder()
                .active(Boolean.FALSE)
                .build();

        postRepository.save(updatedPost);
    }

    private void checkUserAccessPermissions(Post post, User user) {
        if (user.getRole() == UserRole.ROLE_ADMIN || user.getRole() == UserRole.ROLE_EDITOR) {
            return;
        }
        checkWriterAccessPermissions(post, user);
    }

    private void checkWriterAccessPermissions(Post post, User user) {
        if (!post.getAuthorPublicAccount().getId().equals(user.getPublicAccount().getId())) {
            throw new ServiceException("Ви не маєте доступ до цього поста");
        }
    }

    private Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Пост не знайдено"));
    }
}
