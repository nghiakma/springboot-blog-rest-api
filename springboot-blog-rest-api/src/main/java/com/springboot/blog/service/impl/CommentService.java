package com.springboot.blog.service.impl;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;

import java.util.List;

public interface CommentService {

    CommentDto createComment(Long postId, CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(long postId);

    CommentDto getCommentById(Long postId, Long commentId);

    CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest);

    void deleteComment(Long postId, Long commentId);
}
