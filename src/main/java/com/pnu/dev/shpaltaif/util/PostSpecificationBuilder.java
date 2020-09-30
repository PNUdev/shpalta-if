package com.pnu.dev.shpaltaif.util;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.PostFiltersDto;
import com.pnu.dev.shpaltaif.exception.ServiceAdminException;
import com.pnu.dev.shpaltaif.repository.CategoryRepository;
import com.pnu.dev.shpaltaif.repository.PublicAccountRepository;
import com.pnu.dev.shpaltaif.repository.specification.PostSpecification;
import com.pnu.dev.shpaltaif.repository.specification.SearchCriteria;
import com.pnu.dev.shpaltaif.repository.specification.SearchOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static java.util.Objects.nonNull;

@Component
public class PostSpecificationBuilder {

    private final PublicAccountRepository publicAccountRepository;

    private final CategoryRepository categoryRepository;

    @Autowired
    public PostSpecificationBuilder(PublicAccountRepository publicAccountRepository, CategoryRepository categoryRepository) {
        this.publicAccountRepository = publicAccountRepository;
        this.categoryRepository = categoryRepository;
    }

    public Specification<Post> buildPostSpecification(User user, PostFiltersDto postFiltersDto) {

        PostSpecification specification = new PostSpecification();

        specification.add(new SearchCriteria("active", postFiltersDto.isActive(), SearchOperation.EQUAL));

        if (user.getRole().equals(UserRole.ROLE_WRITER)) {
            specification.add(new SearchCriteria("authorPublicAccount", user.getPublicAccount(), SearchOperation.EQUAL));
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
                    LocalDate.parse(postFiltersDto.getCreatedAtGt()).atStartOfDay(),
                    SearchOperation.GREATER_THAN_EQUAL));
        }

        if (nonNull(postFiltersDto.getCreatedAtLt())) {
            specification.add(new SearchCriteria("createdAt",
                    LocalDate.parse(postFiltersDto.getCreatedAtLt()).atStartOfDay(),
                    SearchOperation.LESS_THAN_EQUAL));
        }

        return specification;
    }
}
