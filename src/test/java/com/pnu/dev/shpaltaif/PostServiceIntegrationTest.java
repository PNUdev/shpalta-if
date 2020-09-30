package com.pnu.dev.shpaltaif;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.CreateUserDto;
import com.pnu.dev.shpaltaif.dto.PostDto;
import com.pnu.dev.shpaltaif.dto.PostFiltersDto;
import com.pnu.dev.shpaltaif.exception.ServiceAdminException;
import com.pnu.dev.shpaltaif.listener.ApplicationReadyEventListener;
import com.pnu.dev.shpaltaif.repository.CategoryRepository;
import com.pnu.dev.shpaltaif.repository.PostRepository;
import com.pnu.dev.shpaltaif.repository.UserRepository;
import com.pnu.dev.shpaltaif.service.PostService;
import com.pnu.dev.shpaltaif.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PostServiceIntegrationTest {

    private final Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);


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


    @Test
    @Transactional
    public void findAllAdminTest() {

        Category category1 = createCategory("category1");
        Category category2 = createCategory("category2");
        User firstWriter = createUserWriter("firstWriter");
        User secondWriter = createUserWriter("secondWriter");

        LocalDateTime now = LocalDateTime.now();
        createCustomPost(firstWriter, category1, true, "American story", now.minusDays(4));
        createCustomPost(firstWriter, category1, false, "Russian story", now.minusDays(1));
        createCustomPost(firstWriter, category2, true, "Ukrainian story", now.minusDays(4));
        createCustomPost(firstWriter, category2, true, "Indian story", now.minusDays(15));
        createCustomPost(secondWriter, category1, false, "Canadian story", now.minusDays(1));
        createCustomPost(secondWriter, category1, true, "Carpathian walk", now.minusDays(10));

        User admin = createUserAdmin("admin");
        PostFiltersDto postFiltersDto = PostFiltersDto.builder()
                .active(true)
                .title("story")
                .categoryId(category1.getId())
                .build();

        List<Post> posts = postService.findAll(admin, postFiltersDto, pageable).getContent();
        for (Post post : posts) {
            assertEquals(post.isActive(), postFiltersDto.isActive());
            assertTrue(post.getTitle().contains(postFiltersDto.getTitle()));
            assertEquals(post.getCategory(), category1);
        }

        postFiltersDto = PostFiltersDto.builder()
                .active(true)
                .title("story")
                .authorPublicAccountId(firstWriter.getPublicAccount().getId())
                .build();

        posts = postService.findAll(admin, postFiltersDto, pageable).getContent();
        for (Post post : posts) {
            assertEquals(post.isActive(), postFiltersDto.isActive());
            assertTrue(post.getTitle().contains(postFiltersDto.getTitle()));
            assertEquals(post.getAuthorPublicAccount(), firstWriter.getPublicAccount());
        }

        postFiltersDto = PostFiltersDto.builder()
                .categoryId(category1.getId())
                .build();

        posts = postService.findAll(firstWriter, postFiltersDto, pageable).getContent();
        for (Post post : posts) {
            assertEquals(post.getAuthorPublicAccount(), firstWriter.getPublicAccount());
            assertEquals(post.getCategory(), category1);
        }

        LocalDate fiveDaysAgo = now.toLocalDate().minusDays(6);
        postFiltersDto = PostFiltersDto.builder()
                .createdAtGt(fiveDaysAgo.toString())
                .build();

        posts = postService.findAll(admin, postFiltersDto, pageable).getContent();
        for (Post post : posts) {
            assertTrue(post.getCreatedAt().isAfter(fiveDaysAgo.atStartOfDay()));
        }
    }

    @Test
    @Transactional
    public void findPostByIdAdmin() {

        User writer = createUserWriter("writer");
        Category category = createCategory("title");
        Post post = createPost(writer, category);

        User admin = createUserAdmin("admin");
        Post postFromDb = postService.findById(admin, post.getId());
        assertNotEquals(postFromDb.getAuthorPublicAccount().getUser(), admin);
    }

    @Test
    @Transactional
    public void findPostByIdWriterSuccessFlow() {

        User writer = createUserWriter("writer");
        Category category = createCategory("title");
        Post post = createPost(writer, category);

        Post postFromDb = postService.findById(writer, post.getId());
        assertEquals(postFromDb.getAuthorPublicAccount().getUser(), writer);
    }

    @Test
    @Transactional
    public void findPostByIdWriterExceptionFlow() {

        User writerAuthor = createUserWriter("writerAuthor");
        Category category = createCategory("title");
        Post post = createPost(writerAuthor, category);

        User writerNotAuthor = createUserWriter("writerNotAuthor");

        assertThrows(ServiceAdminException.class,
                () -> postService.findById(writerNotAuthor, post.getId()),
                "Пост не знайдено");
    }

    @Test
    @Transactional
    public void updatePostTest() {
        User writer = createUserWriter("writer");
        Category category = createCategory("title");
        Post post = createPost(writer, category);
        Category newCategory = createCategory("another title");
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
    @Transactional
    public void deactivatePostTest() {

        User writer = createUserWriter("writer");
        Category category = createCategory("title");
        Post post = createPost(writer, category);
        postService.deactivate(writer, post.getId());
        Post deactivatedPost = postService.findById(writer, post.getId());
        assertFalse(deactivatedPost.isActive());
    }

    @Test
    @Transactional
    public void deleteActivePostTest() {

        User writer = createUserWriter("writer");
        Category category = createCategory("title");
        Post post = createPost(writer, category);
        assertThrows(ServiceAdminException.class,
                () -> postService.delete(writer, post.getId()),
                "Пост повинен бути переміщеним в архів перед видаленням");
    }

    @Test
    @Transactional
    public void deleteDeactivatedPostTest() {

        User writer = createUserWriter("writer");
        Category category = createCategory("title");
        Post post = createPost(writer, category);
        postService.deactivate(writer, post.getId());
        postService.delete(writer, post.getId());
        assertThrows(ServiceAdminException.class,
                () -> postService.findById(writer, post.getId()),
                "Пост не знайдено");
    }

    private User createUserAdmin(String username) {
        User user = User.builder()
                .username(username)
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
                .build();

        userService.create(createUserDto);
        List<User> allUsers = userRepository.findAll();
        assertEquals(usersNumberBeforeCreate + 1, allUsers.size());

        return allUsers.get(usersNumberBeforeCreate);
    }

    private Category createCategory(String title) {

        Category category = Category.builder()
                .colorTheme("colorTheme")
                .title(title)
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
                .build();

        PostFiltersDto postFiltersDto = PostFiltersDto.builder()
                .active(true)
                .build();

        List<Post> allPostsBeforeCreate = postService
                .findAll(user, postFiltersDto, pageable)
                .getContent();

        assertEquals(0, allPostsBeforeCreate.size());

        postService.create(user, postDto);

        List<Post> allPostsAfterCreate = postService
                .findAll(user, postFiltersDto, pageable)
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
        return post;
    }

}
