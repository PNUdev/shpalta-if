package com.pnu.dev.shpaltaif.repository.specification;

import com.pnu.dev.shpaltaif.domain.Post;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Stream;

public class PostSpecification implements Specification<Post> {

    private final List<SearchCriteria> criteriaList;

    public PostSpecification(List<SearchCriteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    @Override
    public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        return builder.and(
                criteriaList.stream()
                        .flatMap(criteria -> {
                            if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
                                return Stream.of(
                                        builder.equal(root.get(criteria.getKey()), criteria.getValue())
                                );
                            } else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
                                return Stream.of(
                                        builder.like(
                                                builder.lower(root.get(criteria.getKey())),
                                                "%" + criteria.getValue().toString().toLowerCase() + "%")
                                );
                            }

                            return Stream.empty();
                        }).toArray(Predicate[]::new));
    }
}
