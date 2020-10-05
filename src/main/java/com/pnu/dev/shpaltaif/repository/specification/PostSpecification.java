package com.pnu.dev.shpaltaif.repository.specification;

import com.pnu.dev.shpaltaif.domain.Post;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class PostSpecification implements Specification<Post> {

    private final List<SearchCriteria> list;

    public PostSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();

        list.stream().filter(criteria -> criteria.getOperation().equals(SearchOperation.EQUAL))
                .forEach(criteria -> predicates.add(builder.equal(
                        root.get(criteria.getKey()), criteria.getValue())));
        list.stream().filter(criteria -> criteria.getOperation().equals(SearchOperation.MATCH))
                .forEach(criteria -> predicates.add(builder.like(
                        builder.lower(root.get(criteria.getKey())),
                        "%" + criteria.getValue().toString().toLowerCase() + "%")));

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
