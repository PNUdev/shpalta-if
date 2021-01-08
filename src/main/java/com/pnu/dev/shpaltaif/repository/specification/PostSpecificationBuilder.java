package com.pnu.dev.shpaltaif.repository.specification;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.filter.PostsAdminFilter;
import com.pnu.dev.shpaltaif.dto.filter.PostsPublicFilter;
import com.pnu.dev.shpaltaif.service.CategoryService;
import com.pnu.dev.shpaltaif.service.PublicAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Component
public class PostSpecificationBuilder {

    private final PublicAccountService publicAccountService;

    private final CategoryService categoryService;

    @Autowired
    public PostSpecificationBuilder(PublicAccountService publicAccountService, CategoryService categoryService) {
        this.publicAccountService = publicAccountService;
        this.categoryService = categoryService;
    }

    public Specification<Post> buildPostSpecification(User user, PostsAdminFilter postsAdminFilter) {

        List<SearchCriteria> searchCriteriaList = new ArrayList<>();

        searchCriteriaList.add(new SearchCriteria("active", postsAdminFilter.isActive(), SearchOperation.EQUAL));

        if (nonNull(postsAdminFilter.getTitle())) {
            searchCriteriaList.add(new SearchCriteria("title", postsAdminFilter.getTitle(), SearchOperation.MATCH));
        }

        if (nonNull(postsAdminFilter.getCategoryId())) {
            Category category = categoryService.findById(postsAdminFilter.getCategoryId());
            searchCriteriaList.add(new SearchCriteria("category", category, SearchOperation.EQUAL));
        }

        if (user.getRole().equals(UserRole.ROLE_WRITER)) {
            searchCriteriaList.add(new SearchCriteria("authorPublicAccount", user.getPublicAccount(), SearchOperation.EQUAL));
        } else if (nonNull(postsAdminFilter.getAuthorPublicAccountId())) {
            PublicAccount publicAccount = publicAccountService.findById(postsAdminFilter.getAuthorPublicAccountId());
            searchCriteriaList.add(new SearchCriteria("authorPublicAccount", publicAccount, SearchOperation.EQUAL));
        }

        return new PostSpecification(searchCriteriaList);
    }

    public Specification<Post> buildPostSpecification(PostsPublicFilter postsPublicFilter) {

        List<SearchCriteria> searchCriteriaList = new ArrayList<>();

        // only active posts should be publicly available
        searchCriteriaList.add(new SearchCriteria("active", Boolean.TRUE, SearchOperation.EQUAL));

        if (nonNull(postsPublicFilter.getTitle())) {
            searchCriteriaList.add(new SearchCriteria("title", postsPublicFilter.getTitle(), SearchOperation.MATCH));
        }

        if (nonNull(postsPublicFilter.getCategoryUrl())) {
            Category category = categoryService.findByPublicUrl(postsPublicFilter.getCategoryUrl());
            searchCriteriaList.add(new SearchCriteria("category", category, SearchOperation.EQUAL));
        }

        if (nonNull(postsPublicFilter.getAuthorPublicAccountId())) {
            PublicAccount publicAccount = publicAccountService.findById(postsPublicFilter.getAuthorPublicAccountId());
            searchCriteriaList.add(new SearchCriteria("authorPublicAccount", publicAccount, SearchOperation.EQUAL));
        }

        return new PostSpecification(searchCriteriaList);
    }

}
