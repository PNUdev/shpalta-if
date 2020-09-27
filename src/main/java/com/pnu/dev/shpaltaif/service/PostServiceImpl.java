package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.PostDto;
import com.pnu.dev.shpaltaif.dto.PostFiltersDto;
import com.pnu.dev.shpaltaif.exception.ServiceAdminException;
import com.pnu.dev.shpaltaif.repository.CategoryRepository;
import com.pnu.dev.shpaltaif.repository.PostRepository;
import com.pnu.dev.shpaltaif.repository.PublicAccountRepository;
import com.pnu.dev.shpaltaif.repository.specification.PostSpecification;
import com.pnu.dev.shpaltaif.repository.specification.SearchCriteria;
import com.pnu.dev.shpaltaif.repository.specification.SearchOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Objects.nonNull;

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
    public Page<Post> findAll(User user, PostFiltersDto filtersDto, Pageable pageable) {

        Specification<Post> specification = generatePostSpecification(user, filtersDto);
        return postRepository.findAll(specification, pageable);
    }

    private Specification<Post> generatePostSpecification(User user, PostFiltersDto postFiltersDto) {

        PostSpecification specification = new PostSpecification();

        specification.add(new SearchCriteria("active", postFiltersDto.isActive(), SearchOperation.EQUAL));

        if (user.getRole().equals(UserRole.ROLE_WRITER)) {
            PublicAccount publicAccount = publicAccountRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ServiceAdminException("Акаунт не знайдено"));
            specification.add(new SearchCriteria("authorPublicAccount", publicAccount, SearchOperation.EQUAL));
        } else if (nonNull(postFiltersDto.getAuthorPublicAccountId())) {
            PublicAccount publicAccount = publicAccountRepository.findById(postFiltersDto.getAuthorPublicAccountId())
                    .orElseThrow(() -> new ServiceAdminException("Акаунт не знайдено"));
            specification.add(new SearchCriteria("authorPublicAccount", publicAccount, SearchOperation.EQUAL));
        }

        if (nonNull(postFiltersDto.getTitle())) {
            specification.add(new SearchCriteria("title", postFiltersDto.getTitle(), SearchOperation.MATCH));
        }

        if (nonNull(postFiltersDto.getCategoryId())) {
            Category category = categoryRepository.findById(postFiltersDto.getCategoryId())
                    .orElseThrow(() -> new ServiceAdminException("Категорію не знайдено"));
            specification.add(new SearchCriteria("category", category, SearchOperation.EQUAL));
        }

        if (nonNull(postFiltersDto.getCreatedAtGt())) {
            specification.add(new SearchCriteria("createdAt",
                    (LocalDate.parse(postFiltersDto.getCreatedAtGt()).atStartOfDay()),
                    SearchOperation.GREATER_THAN));
        }

        if (nonNull(postFiltersDto.getCreatedAtLt())) {
            specification.add(new SearchCriteria("createdAt",
                    (LocalDate.parse(postFiltersDto.getCreatedAtLt()).atStartOfDay()),
                    SearchOperation.LESS_THAN));
        }
        return specification;

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

























