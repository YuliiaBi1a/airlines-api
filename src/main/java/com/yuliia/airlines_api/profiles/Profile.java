package com.yuliia.airlines_api.profiles;

import com.yuliia.airlines_api.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String name;
    private String surname;
    private String image;
    private String email;
    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Profile(String name, String surname, String image, String email, String phone, User user) {
        this.name = name;
        this.surname = surname;
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.user = user;
    }
}
