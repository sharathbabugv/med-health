package com.health.application.medhealth.dto;

import com.health.application.medhealth.utils.CustomUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Doctor implements UserDetails {

    @Id
    @GenericGenerator(name = "id", strategy = "com.health.application.medhealth.utils.CustomIdGenerator")
    @GeneratedValue(generator = "id")
    private String id;
    private String email;
    private String password;
    private String name;
    private String gender;

    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String qualification;
    private String speciality;
    private LocalDate workStartDate;

    @Transient
    private String experience;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_DOCTOR"));
        return authorities;
    }

    public String getExperience() {
        return CustomUtils.getDifferenceBetweenDates(this.workStartDate);
    }

    @Override
    public String getUsername() {
        return id;
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
}
