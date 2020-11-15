package com.pnu.dev.shpaltaif.integration.service;

import com.google.common.collect.Iterables;
import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.CreateUserDto;
import com.pnu.dev.shpaltaif.dto.PostDto;
import com.pnu.dev.shpaltaif.dto.filter.PostsAdminFilter;
import com.pnu.dev.shpaltaif.dto.filter.PostsPublicFilter;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.integration.BaseIntegrationTest;
import com.pnu.dev.shpaltaif.listener.ApplicationReadyEventListener;
import com.pnu.dev.shpaltaif.repository.CategoryRepository;
import com.pnu.dev.shpaltaif.repository.PostRepository;
import com.pnu.dev.shpaltaif.repository.UserRepository;
import com.pnu.dev.shpaltaif.service.PostService;
import com.pnu.dev.shpaltaif.service.UserService;
import com.pnu.dev.shpaltaif.service.telegram.TelegramNotificationService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class NewPostServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    @MockBean // present here to disable default admin user creation
    private ApplicationReadyEventListener applicationReadyEventListener;

    @MockBean
    private TelegramNotificationService telegramNotificationService;

    private static final Pageable PAGEABLE = PageRequest.of(0, Integer.MAX_VALUE);
    private User admin;
    private User editor;
    private List<User> writers;
    private List<Category> categories;

    @BeforeEach
    @Rollback(false)
    public void setup() {
        this.categories = new ArrayList<>();
        Category category1 = Category.builder()
                .colorTheme("colorTheme1")
                .title("title1")
                .publicUrl("publicUrl1")
                .build();
        this.categories.add(categoryRepository.save(category1));
        Category category2 = Category.builder()
                .colorTheme("colorTheme2")
                .title("title2")
                .publicUrl("publicUrl2")
                .build();
        this.categories.add(categoryRepository.save(category2));

        writers = new ArrayList<>();
        CreateUserDto createUserWriterDto1 = CreateUserDto
                .builder()
                .username("writer1")
                .password("password")
                .repeatedPassword("password")
                .name("name")
                .surname("surname")
                .role(UserRole.ROLE_WRITER)
                .build();
        userService.create(createUserWriterDto1);
        writers.add(getLastCreatedUser());
        CreateUserDto createUserWriterDto2 = CreateUserDto
                .builder()
                .username("writer2")
                .password("password")
                .repeatedPassword("password")
                .name("name")
                .surname("surname")
                .role(UserRole.ROLE_WRITER)
                .build();
        userService.create(createUserWriterDto2);
        writers.add(getLastCreatedUser());
        User admin = User.builder()
                .username("admin")
                .password("password")
                .role(UserRole.ROLE_ADMIN)
                .active(true)
                .build();
        this.admin = userRepository.save(admin);
        User editor = User.builder()
                .username("editor")
                .password("password")
                .role(UserRole.ROLE_EDITOR)
                .active(true)
                .build();
        this.editor = userRepository.save(editor);
    }

    @Test
    public void findAllAdminFilterTest() {
        int postsNumberToCreate = 200;
        int postsAdminFiltersNumberToTest = 10;
        User writer = this.writers.get(0);

        //Generate posts for testing
        generateRandomPosts(postsNumberToCreate);

        //Loop with creating and testing different filters
        for (int i = 0; i < postsAdminFiltersNumberToTest; i++) {
            //Generate PostsFilter with random parameters
            PostsAdminFilter postsAdminFilter = generateRandomPostsAdminFilter();
            List<Post> postsFoundByAdmin = postService.findAll(this.admin, postsAdminFilter, PAGEABLE).getContent();
            List<Post> postsFoundByEditor = postService.findAll(this.editor, postsAdminFilter, PAGEABLE).getContent();

            // Posts should be the same for admin and editor with same filter
            assertEquals(postsFoundByAdmin, postsFoundByEditor);

            //Assert posts by generated PostsAdminFilter
            for (Post post : postsFoundByAdmin) {
                assertEquals(post.isActive(), postsAdminFilter.isActive());
                if (nonNull(postsAdminFilter.getTitle())) {
                    assertThat(post.getTitle()).contains(postsAdminFilter.getTitle());
                }
                if (nonNull(postsAdminFilter.getAuthorPublicAccountId())) {
                    assertEquals(post.getAuthorPublicAccount().getId(), postsAdminFilter.getAuthorPublicAccountId());
                }
                if (nonNull(postsAdminFilter.getCategoryId())) {
                    assertEquals(post.getCategory().getId(), postsAdminFilter.getCategoryId());
                }
            }
            //Assert posts found by writer
            List<Post> postsFoundByWriter = postService.findAll(writer, postsAdminFilter, PAGEABLE).getContent();
            for (Post post : postsFoundByWriter) {
                assertEquals(post.getAuthorPublicAccount().getId(), writer.getPublicAccount().getId());
                assertEquals(post.isActive(), postsAdminFilter.isActive());
                if (nonNull(postsAdminFilter.getTitle())) {
                    assertThat(post.getTitle()).contains(postsAdminFilter.getTitle());
                }
                if (nonNull(postsAdminFilter.getCategoryId())) {
                    assertEquals(post.getCategory().getId(), postsAdminFilter.getCategoryId());
                }
            }
        }
    }

    @Test
    public void findAllPublicFilterTest() {
        int postsNumberToCreate = 100;
        int postsPublicFiltersToTest = 10;
        //Generate posts for test
        generateRandomPosts(postsNumberToCreate);
        for (int i = 0; i < postsPublicFiltersToTest; i++) {
            //Generate PostsPublicFilter with random parameters
            PostsPublicFilter postsPublicFilter = generateRandomPostsPublicFilter();
            List<Post> posts = postService.findAll(postsPublicFilter, PAGEABLE).getContent();
            for (Post post : posts) {
                assertTrue(post.isActive());
                if (nonNull(postsPublicFilter.getTitle())) {
                    assertThat(post.getTitle()).contains(postsPublicFilter.getTitle());
                }
                if (nonNull(postsPublicFilter.getCategoryUrl())) {
                    assertEquals(post.getCategory().getPublicUrl(), postsPublicFilter.getCategoryUrl());
                }
            }
        }
    }

    @Test
    public void findPostById() {
        User writerAuthor = this.writers.get(0);
        User writerNotAuthor = this.writers.get(1);
        Category category = this.categories.get(0);

        //Create post
        Post post = createPost(writerAuthor, category);

        //Get created post by it's author
        Post postFoundByAuthor = postService.findById(writerAuthor, post.getId());
        assertEquals(postFoundByAuthor.getAuthorPublicAccount().getUser(), writerAuthor);

        //Get created post by another writer
        ServiceException thrown = assertThrows(ServiceException.class,
                () -> postService.findById(writerNotAuthor, post.getId()));
        assertEquals("Ви не маєте доступ до цього поста", thrown.getMessage());

        //Get created post by admin
        Post postFoundByAdmin = postService.findById(this.admin, post.getId());
        assertNotEquals(postFoundByAdmin.getAuthorPublicAccount().getUser(), this.admin);

        //Get created post by it's editor
        Post postFoundByEditor = postService.findById(this.editor, post.getId());
        assertNotEquals(postFoundByEditor.getAuthorPublicAccount().getUser(), this.editor);
    }

    @Test
    public void updatePostTest() {
        User writerAuthor = this.writers.get(0);
        User writerNotAuthor = this.writers.get(1);
        Category category = this.categories.get(0);
        Category anotherCategory = this.categories.get(1);

        //Create post
        Post post = createPost(writerAuthor, category);

        //PostDto for first update by writer
        PostDto postUpdateDto = PostDto.builder()
                .content("new content")
                .title("new title")
                .active(!post.isActive())
                .categoryId(anotherCategory.getId())
                .pictureUrl("new url")
                .build();

        Post expectedPost = post.toBuilder()
                .content(postUpdateDto.getContent())
                .title(postUpdateDto.getTitle())
                .active(postUpdateDto.isActive())
                .category(anotherCategory)
                .pictureUrl(postUpdateDto.getPictureUrl())
                .build();

        //Attempt to update post by another writer - Exception flow
        ServiceException thrown = assertThrows(ServiceException.class,
                () -> postService.update(writerNotAuthor, post.getId(), postUpdateDto));
        assertEquals("Ви не маєте доступ до цього поста", thrown.getMessage());

        //Update post by it's author
        postService.update(writerAuthor, post.getId(), postUpdateDto);
        Post updatedPost = postService.findById(writerAuthor, post.getId());
        assertThat(updatedPost).isEqualTo(expectedPost);

        //PostDto for second update by editor
        PostDto postUpdateDto2 = PostDto.builder()
                .content("new content")
                .title("new title")
                .active(!updatedPost.isActive())
                .categoryId(category.getId())
                .pictureUrl("new url")
                .build();

        Post expectedPost2 = post.toBuilder()
                .content(postUpdateDto2.getContent())
                .title(postUpdateDto2.getTitle())
                .active(postUpdateDto2.isActive())
                .category(category)
                .pictureUrl(postUpdateDto2.getPictureUrl())
                .build();

        //Update post by editor
        postService.update(editor, post.getId(), postUpdateDto2);
        Post updatedPost2 = postService.findById(editor, post.getId());
        assertThat(updatedPost2).isEqualTo(expectedPost2);
    }

    @Test
    public void deactivateAndThenActivatePostTest() {
        User writerAuthor = this.writers.get(0);
        User writerNotAuthor = this.writers.get(1);
        Category category = this.categories.get(0);

        //Create post
        Post post = createPost(writerAuthor, category);

        //Attempt to deactivate post by another writer - Exception flow
        ServiceException deactivateThrown = assertThrows(ServiceException.class,
                () -> postService.deactivate(writerNotAuthor, post.getId()));
        assertEquals("Ви не маєте доступ до цього поста", deactivateThrown.getMessage());

        //Deactivate post by it's author
        postService.deactivate(writerAuthor, post.getId());
        Post deactivatedPost = postService.findById(writerAuthor, post.getId());
        assertFalse(deactivatedPost.isActive());

        //Attempt to activate post by another writer - Exception flow
        ServiceException activateThrown = assertThrows(ServiceException.class,
                () -> postService.activate(writerNotAuthor, post.getId()));
        assertEquals("Ви не маєте доступ до цього поста", activateThrown.getMessage());

        //Activate post by it's author
        postService.activate(writerAuthor, post.getId());
        Post activatedPost = postService.findById(writerAuthor, post.getId());
        assertTrue(activatedPost.isActive());

        //Deactivate post by editor
        postService.deactivate(this.editor, post.getId());
        Post deactivatedPostByEditor = postService.findById(this.editor, post.getId());
        assertFalse(deactivatedPostByEditor.isActive());
        //Activate post by editor
        postService.activate(this.editor, post.getId());
        Post activatedPostByEditor = postService.findById(this.editor, post.getId());
        assertTrue(activatedPostByEditor.isActive());

        //Deactivate post by admin
        postService.deactivate(this.admin, post.getId());
        Post deactivatedPostByAdmin = postService.findById(this.admin, post.getId());
        assertFalse(deactivatedPostByAdmin.isActive());
        //Activate post by admin
        postService.activate(this.admin, post.getId());
        Post activatedPostByAdmin = postService.findById(this.admin, post.getId());
        assertTrue(activatedPostByAdmin.isActive());
    }

    @Test
    public void deleteActivePostTest() {
        User writer = this.writers.get(0);
        Category category = this.categories.get(0);
        //Create post
        Post post = createPost(writer, category);
        //Attempt to delete active post - Exception flow
        ServiceException thrown = assertThrows(ServiceException.class,
                () -> postService.delete(writer, post.getId()));
        assertEquals("Пост повинен бути переміщеним в архів перед видаленням", thrown.getMessage());
    }

    @Test
    public void deleteDeactivatedPostTest() {
        User writer = this.writers.get(0);
        User writerNotAuthor = this.writers.get(1);
        Category category = this.categories.get(0);

        //Create post
        Post post = createPost(writer, category);

        //Deactivate post by it's author
        postService.deactivate(writer, post.getId());

        //Attempt to delete post by another writer - Exception flow
        ServiceException thrownForNotAuthor = assertThrows(ServiceException.class,
                () -> postService.delete(writerNotAuthor, post.getId()));
        assertEquals("Ви не маєте доступ до цього поста", thrownForNotAuthor.getMessage());

        //Delete post by it's author
        postService.delete(writer, post.getId());
        ServiceException thrownNotFound = assertThrows(ServiceException.class,
                () -> postService.findById(writer, post.getId()));
        assertEquals("Пост не знайдено", thrownNotFound.getMessage());
    }

    private Post createPost(User user, Category category) {

        assertEquals(user.getRole(), UserRole.ROLE_WRITER);

        PostDto postDto = PostDto.builder()
                .title("title")
                .pictureUrl("pictureUrl")
                .categoryId(category.getId())
                .content("content")
                .sendTelegramNotifications(true)
                .build();

        PostsAdminFilter postsAdminFilter = PostsAdminFilter.builder()
                .active(true)
                .build();

        List<Post> allPostsBeforeCreate = postService
                .findAll(user, postsAdminFilter, PAGEABLE)
                .getContent();

        assertEquals(0, allPostsBeforeCreate.size());

        postService.create(user, postDto);

        List<Post> allPostsAfterCreate = postService
                .findAll(user, postsAdminFilter, PAGEABLE)
                .getContent();

        assertEquals(1, allPostsAfterCreate.size());

        Post post = allPostsAfterCreate.get(0);

        Post expectedPost = Post.builder()
                .title(postDto.getTitle())
                .category(category)
                .content(postDto.getContent())
                .pictureUrl(postDto.getPictureUrl())
                .active(true)
                .authorPublicAccount(user.getPublicAccount())
                .build();

        assertThat(post).isEqualToIgnoringGivenFields(expectedPost, "id", "createdAt");

        Mockito.verify(telegramNotificationService, Mockito.only()).sendNotificationsOfNewPost(post);

        return post;
    }

    private PostsPublicFilter generateRandomPostsPublicFilter() {
        Random random = new Random();
        String title = null;
        if (random.nextBoolean()) {
            title = RandomStringUtils.random(2, true, false).toLowerCase();
        }
        String categoryUrl = null;
        if (random.nextBoolean()) {
            categoryUrl = this.categories.get(random.nextInt(this.categories.size())).getPublicUrl();
        }
        return PostsPublicFilter.builder()
                .title(title)
                .categoryUrl(categoryUrl)
                .build();
    }

    private PostsAdminFilter generateRandomPostsAdminFilter() {
        Random random = new Random();
        String title = null;
        if (random.nextBoolean()) {
            title = RandomStringUtils.random(2, true, false).toLowerCase();
        }
        Long authorPublicAccountId = null;
        if (random.nextBoolean()) {
            authorPublicAccountId = this.writers.get(random.nextInt(this.writers.size())).getPublicAccount().getId();
        }
        Long categoryId = null;
        if (random.nextBoolean()) {
            categoryId = this.categories.get(random.nextInt(this.categories.size())).getId();
        }
        boolean active = random.nextBoolean();

        return PostsAdminFilter.builder()
                .title(title)
                .authorPublicAccountId(authorPublicAccountId)
                .categoryId(categoryId)
                .active(active)
                .build();
    }

    private void generateRandomPosts(int number) {
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < number; i++) {
            int titleLength = random.nextInt((50 - 15) + 1) + 15;
            String generatedTitle = RandomStringUtils.random(titleLength, true, false).toLowerCase();
            Post post = Post.builder()
                    .title(generatedTitle)
                    .pictureUrl("pictureUrl")
                    .content("content")
                    .active(random.nextBoolean())
                    .category(this.categories.get(random.nextInt(this.categories.size())))
                    .authorPublicAccount(this.writers.get(random.nextInt(this.writers.size())).getPublicAccount())
                    .createdAt(now.minusDays(random.nextInt(15)))
                    .build();
            postRepository.save(post);
        }
    }

    private User getLastCreatedUser() {
        return Iterables.getLast(userRepository.findAll());
    }
}
