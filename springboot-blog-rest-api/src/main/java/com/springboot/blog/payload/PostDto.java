package com.springboot.blog.payload;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
@Schema(
        description = "PostDto Model Information"
)
public class PostDto {
    private Long id;

    @Schema(
            description = "Blog Post title"
    )
    @NotEmpty
    @Size(min =  2, message = "Post title should have at least 2 characters")
    private String title;

    @Schema(
            description = "Blog Post description"
    )
    @NotEmpty
    @Size(min =  10, message = "Post description should have at least 10 characters")
    private String description;

    @Schema(
            description = "Blog Post content"
    )
    @NotEmpty
    private String content;
    private Set<CommentDto> comments;

    @Schema(
            description = "Blog Post Category"
    )
    private Long categoryId;
}
