package com.narola.springSecurityJPA.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="usertbl")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String username;
    private String password;
    @ManyToMany(cascade={CascadeType.PERSIST,CascadeType.REMOVE},fetch=FetchType.EAGER)
    @JoinColumn(name="roleId",referencedColumnName = "id")
    private List<Role> roles=new ArrayList<>();
}
