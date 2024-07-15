package com.neobis.cookscorner.model;

import com.neobis.cookscorner.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"profileImage", "savedRecipes"})
public class User extends BaseEntity implements UserDetails {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    private String bio;

    @OneToOne
    @JoinColumn(name = "profile_image_id")
    private Image profileImage;

    @ManyToMany(mappedBy = "savedByUsers")
    private Set<Recipe> savedRecipes = new HashSet<>();

    @ManyToMany(mappedBy = "likedByUsers")
    private Set<Recipe> likedRecipes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_follows",
            joinColumns = @JoinColumn(name = "follower_user_id"),
            inverseJoinColumns = @JoinColumn(name = "following_user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"follower_user_id", "following_user_id"})
    )
    private Set<User> following = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    private Set<User> followers = new HashSet<>();


    public User(
            String email,
            String name,
            String password,
            UserRole role,
            String bio) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.bio = bio;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && role == user.role && Objects.equals(bio, user.bio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, password, role, bio);
    }
}
