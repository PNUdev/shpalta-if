package com.pnu.dev.shpaltaif.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private PublicAccount authorPublicAccount;

    @NotNull
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Category category;

    // ToDo add more fields

}
