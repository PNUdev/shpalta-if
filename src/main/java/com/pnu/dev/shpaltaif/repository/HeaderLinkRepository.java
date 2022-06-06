package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.HeaderLink;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;

public interface HeaderLinkRepository extends JpaRepository<HeaderLink, Long> {

    @Override
    @QueryHints(@QueryHint(name = HINT_CACHEABLE, value = "true"))
    List<HeaderLink> findAll(Sort sort);

    HeaderLink findFirstBySequenceGreaterThanOrderBySequenceAsc(Long sequence);

    HeaderLink findFirstBySequenceLessThanOrderBySequenceDesc(Long sequence);

    @Query("SELECT MAX(headerLink.sequence) FROM HeaderLink headerLink")
    Long findMaxSequence();

}
