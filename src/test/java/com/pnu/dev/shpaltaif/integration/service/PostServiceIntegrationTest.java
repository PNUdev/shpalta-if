package com.pnu.dev.shpaltaif.integration.service;

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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostServiceIntegrationTest extends BaseIntegrationTest {

    private static final String FIRST_CATEGORY_URL = "FIRST_CATEGORY_URL";

    private static final String SECOND_CATEGORY_URL = "SECOND_CATEGORY_URL";

    private static final Pageable PAGEABLE = PageRequest.of(0, Integer.MAX_VALUE);

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

    @Test
    public void findAllAdminFilterTest() {

        Category category1 = createAndSaveCategory("category1", FIRST_CATEGORY_URL);
        Category category2 = createAndSaveCategory("category2", SECOND_CATEGORY_URL);
        User firstWriter = createUserWriter("firstWriter");
        User secondWriter = createUserWriter("secondWriter");

        LocalDateTime now = LocalDateTime.now();
        createCustomPost(firstWriter, category1, true, "American story", now.minusDays(4));
        createCustomPost(firstWriter, category1, false, "Russian story", now.minusDays(1));
        createCustomPost(firstWriter, category2, true, "Ukrainian story", now.minusDays(4));
        createCustomPost(firstWriter, category2, true, "Indian story", now.minusDays(15));
        createCustomPost(secondWriter, category1, false, "Canadian story", now.minusDays(1));
        createCustomPost(secondWriter, category1, true, "Carpathian walk", now.minusDays(10));

        User admin = createUserAdmin();
        PostsAdminFilter postsAdminFilterOne = PostsAdminFilter.builder()
                .active(true)
                .title("story")
                .categoryId(category1.getId())
                .build();

        List<Post> posts = postService.findAll(admin, postsAdminFilterOne, PAGEABLE).getContent();
        for (Post post : posts) {
            assertEquals(post.isActive(), postsAdminFilterOne.isActive());
            assertThat(post.getTitle()).contains(postsAdminFilterOne.getTitle());
            assertEquals(post.getCategory(), category1);
        }

        PostsAdminFilter postsAdminFilterTwo = PostsAdminFilter.builder()
                .active(true)
                .title("story")
                .authorPublicAccountId(firstWriter.getPublicAccount().getId())
                .build();

        posts = postService.findAll(admin, postsAdminFilterTwo, PAGEABLE).getContent();
        for (Post post : posts) {
            assertEquals(post.isActive(), postsAdminFilterTwo.isActive());
            assertThat(post.getTitle()).contains(postsAdminFilterTwo.getTitle());
            assertEquals(post.getAuthorPublicAccount(), firstWriter.getPublicAccount());
        }

        PostsAdminFilter postsAdminFilterThree = PostsAdminFilter.builder()
                .categoryId(category1.getId())
                .build();

        posts = postService.findAll(firstWriter, postsAdminFilterThree, PAGEABLE).getContent();
        for (Post post : posts) {
            assertEquals(post.getAuthorPublicAccount(), firstWriter.getPublicAccount());
            assertEquals(post.getCategory(), category1);
        }
    }

    @Test
    public void findAllPublicFilterTest() {

        Category category1 = createAndSaveCategory("category1", FIRST_CATEGORY_URL);
        Category category2 = createAndSaveCategory("category2", SECOND_CATEGORY_URL);
        User firstWriter = createUserWriter("firstWriter");
        User secondWriter = createUserWriter("secondWriter");

        LocalDateTime now = LocalDateTime.now();
        createCustomPost(firstWriter, category1, true, "American story", now.minusDays(4));
        createCustomPost(firstWriter, category1, false, "Russian story", now.minusDays(1));
        createCustomPost(firstWriter, category2, true, "Ukrainian story", now.minusDays(4));
        createCustomPost(firstWriter, category2, true, "Indian story", now.minusDays(15));
        createCustomPost(secondWriter, category1, false, "Canadian story", now.minusDays(1));
        createCustomPost(secondWriter, category1, true, "Carpathian walk", now.minusDays(10));

        PostsPublicFilter postsPublicFilterTitle = PostsPublicFilter.builder()
                .title("story")
                .build();

        List<Post> posts = postService.findAll(postsPublicFilterTitle, PAGEABLE).getContent();
        for (Post post : posts) {
            assertTrue(post.isActive());
            assertThat(post.getTitle()).contains(postsPublicFilterTitle.getTitle());
        }

        PostsPublicFilter postsPublicFilterCategoryUrl = PostsPublicFilter.builder()
                .categoryUrl(FIRST_CATEGORY_URL)
                .build();

        posts = postService.findAll(postsPublicFilterCategoryUrl, PAGEABLE).getContent();
        for (Post post : posts) {
            assertTrue(post.isActive());
            assertEquals(FIRST_CATEGORY_URL, post.getCategory().getPublicUrl());
        }

    }

    @Test
    public void findPostByIdAdmin() {

        User writer = createUserWriter("writer");
        Category category = createAndSaveCategory("title", FIRST_CATEGORY_URL);
        Post post = createPost(writer, category);

        User admin = createUserAdmin();
        Post postFromDb = postService.findById(admin, post.getId());
        assertNotEquals(postFromDb.getAuthorPublicAccount().getUser(), admin);
    }

    @Test
    public void findPostByIdWriterSuccessFlow() {

        User writer = createUserWriter("writer");
        Category category = createAndSaveCategory("title", FIRST_CATEGORY_URL);
        Post post = createPost(writer, category);

        Post postFromDb = postService.findById(writer, post.getId());
        assertEquals(postFromDb.getAuthorPublicAccount().getUser(), writer);
    }

    @Test
    public void findPostByIdWriterExceptionFlow() {

        User writerAuthor = createUserWriter("writerAuthor");
        Category category = createAndSaveCategory("title", FIRST_CATEGORY_URL);
        Post post = createPost(writerAuthor, category);

        User writerNotAuthor = createUserWriter("writerNotAuthor");

        ServiceException thrown = assertThrows(ServiceException.class,
                () -> postService.findById(writerNotAuthor, post.getId()));
        assertEquals("Ви не маєте доступ до цього поста", thrown.getMessage());

    }

    @Test
    public void updatePostTest() {
        User writer = createUserWriter("writer");
        Category category = createAndSaveCategory("title", FIRST_CATEGORY_URL);
        Post post = createPost(writer, category);
        Category newCategory = createAndSaveCategory("another title", SECOND_CATEGORY_URL);
        PostDto postUpdateDto = PostDto.builder()
                .content("new content")
                .title("new title")
                .active(!post.isActive())
                .categoryId(newCategory.getId())
                .pictureUrl("new url")
                .build();

        Post expectedPost = post.toBuilder()
                .content(postUpdateDto.getContent())
                .title(postUpdateDto.getTitle())
                .active(postUpdateDto.isActive())
                .category(newCategory)
                .pictureUrl(postUpdateDto.getPictureUrl())
                .build();

        postService.update(writer, post.getId(), postUpdateDto);
        Post updatedPost = postService.findById(writer, post.getId());

        assertThat(updatedPost).isEqualTo(expectedPost);

    }

    @Test
    public void deactivatePostTest() {

        User writer = createUserWriter("writer");
        Category category = createAndSaveCategory("title", FIRST_CATEGORY_URL);
        Post post = createPost(writer, category);
        postService.deactivate(writer, post.getId());
        Post deactivatedPost = postService.findById(writer, post.getId());
        assertFalse(deactivatedPost.isActive());
    }

    @Test
    public void deleteActivePostTest() {

        User writer = createUserWriter("writer");
        Category category = createAndSaveCategory("title", FIRST_CATEGORY_URL);
        Post post = createPost(writer, category);
        ServiceException thrown = assertThrows(ServiceException.class,
                () -> postService.delete(writer, post.getId()));
        assertEquals("Пост повинен бути переміщеним в архів перед видаленням", thrown.getMessage());

    }

    @Test
    public void deleteDeactivatedPostTest() {

        User writer = createUserWriter("writer");
        Category category = createAndSaveCategory("title", FIRST_CATEGORY_URL);
        Post post = createPost(writer, category);
        postService.deactivate(writer, post.getId());
        postService.delete(writer, post.getId());
        ServiceException thrown = assertThrows(ServiceException.class,
                () -> postService.findById(writer, post.getId()));
        assertEquals("Пост не знайдено", thrown.getMessage());
    }

    private User createUserAdmin() {
        User user = User.builder()
                .username("admin")
                .password("password")
                .role(UserRole.ROLE_ADMIN)
                .active(true)
                .build();

        return userRepository.save(user);
    }

    private User createUserWriter(String username) {

        int usersNumberBeforeCreate = userRepository.findAll().size();

        CreateUserDto createUserDto = CreateUserDto
                .builder()
                .username(username)
                .password("password")
                .repeatedPassword("password")
                .name("name")
                .surname("surname")
                .role(UserRole.ROLE_WRITER)
                .build();

        userService.create(createUserDto);
        List<User> allUsers = userRepository.findAll();
        assertEquals(usersNumberBeforeCreate + 1, allUsers.size());

        return allUsers.get(usersNumberBeforeCreate);
    }

    private Category createAndSaveCategory(String title, String publicUrl) {

        Category category = Category.builder()
                .colorTheme("colorTheme")
                .title(title)
                .publicUrl(publicUrl)
                .build();

        return categoryRepository.save(category);
    }

    private Post createCustomPost(User user, Category category, boolean active, String title,
                                  LocalDateTime createdAt) {

        Post post = Post.builder()
                .title(title)
                .pictureUrl("pictureUrl")
                .content("content")
                .active(active)
                .category(category)
                .authorPublicAccount(user.getPublicAccount())
                .createdAt(createdAt)
                .build();

        return postRepository.save(post);

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

}
