package com.pnu.dev.shpaltaif.repository.specification;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.PostFiltersDto;
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

    private final PublicAccountService publicAccountRepository;

    private final CategoryService categoryRepository;

    @Autowired
    public PostSpecificationBuilder(PublicAccountService publicAccountService, CategoryService categoryService) {
        this.publicAccountRepository = publicAccountService;
        this.categoryRepository = categoryService;
    }

    public Specification<Post> buildPostSpecification(User user, PostFiltersDto postFiltersDto) {

        List<SearchCriteria> searchCriteriaList = buildCommonSearchCriteriaList(postFiltersDto);

        if (user.getRole().equals(UserRole.ROLE_WRITER)) {
            searchCriteriaList.add(new SearchCriteria("authorPublicAccount", user.getPublicAccount(), SearchOperation.EQUAL));
        } else if (nonNull(postFiltersDto.getAuthorPublicAccountId())) {
            PublicAccount publicAccount = publicAccountRepository.findById(postFiltersDto.getAuthorPublicAccountId());
            searchCriteriaList.add(new SearchCriteria("authorPublicAccount", publicAccount, SearchOperation.EQUAL));
        }

        return new PostSpecification(searchCriteriaList);
    }

    public Specification<Post> buildPostSpecification(PostFiltersDto postFiltersDto) {

        List<SearchCriteria> searchCriteriaList = buildCommonSearchCriteriaList(postFiltersDto);
        return new PostSpecification(searchCriteriaList);
    }

    private List<SearchCriteria> buildCommonSearchCriteriaList(PostFiltersDto postFiltersDto) {

        List<SearchCriteria> searchCriteriaList = new ArrayList<>();

        searchCriteriaList.add(new SearchCriteria("active", postFiltersDto.isActive(), SearchOperation.EQUAL));


        if (nonNull(postFiltersDto.getTitle())) {
            searchCriteriaList.add(new SearchCriteria("title", postFiltersDto.getTitle(), SearchOperation.MATCH));
        }

        if (nonNull(postFiltersDto.getCategoryId())) {
            Category category = categoryRepository.findById(postFiltersDto.getCategoryId());
            searchCriteriaList.add(new SearchCriteria("category", category, SearchOperation.EQUAL));
        }

        return searchCriteriaList;
    }
}
