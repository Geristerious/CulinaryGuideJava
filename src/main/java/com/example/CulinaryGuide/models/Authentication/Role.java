package com.example.CulinaryGuide.models.Authentication;

import lombok.*;

import javax.persistence.*;


@Table(name="authorities")
@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
@AllArgsConstructor
public class Role {


    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @Column(name = "authority")
    private String authority;

    @Id
    @Column(name = "username")
    private String username;



}
