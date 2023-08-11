package com.springboot.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})}
)
public class Post {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "content", nullable = false)
    private String content;

    /*
    * các hoạt động liên quan đến Post sẽ được truyền xuống cho tất cả các Comment tương ứng.
    * Nghĩa là, nếu thực hiện một thao tác CRUD (Create, Read, Update, Delete) trên Post,
    * tất cả các Comment liên quan cũng sẽ được thực hiện tương tự.
    * */
    /*
    * "orphanRemoval = true" xác định rằng các Comment không có sẽ được tự động xóa khỏi cơ sở dữ liệu
    *  nếu chúng không còn được liên kết với Post nào.
    * */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
