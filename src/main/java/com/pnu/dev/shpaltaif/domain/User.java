package com.pnu.dev.shpaltaif.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.List;

@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String login;

    @NotNull
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean active;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private PublicAccount publicAccount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authorPublicAccount")
    private List<Post> posts;

}
