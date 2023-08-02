package com.example.board.domain.post.service;

import com.example.board.domain.post.Post;
import com.example.board.domain.post.dto.PostCreateRequest;
import com.example.board.domain.post.dto.PostResponse;
import com.example.board.domain.post.dto.PostUpdateRequest;
import com.example.board.domain.post.repository.PostRepository;
import com.example.board.domain.user.User;
import com.example.board.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Transactional
  public PostResponse createPost(PostCreateRequest postCreateRequest) {
    User foundUser = userRepository.findById(postCreateRequest.userId()).orElseThrow(
        EntityNotFoundException::new);

    Post savedPost = postRepository.save(
        new Post(postCreateRequest.title(), postCreateRequest.content(), foundUser));

    return PostResponse.from(savedPost);
  }

  @Transactional(readOnly = true)
  public PostResponse getPost(Long postId) {
    Post foundPost = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);

    return PostResponse.from(foundPost);
  }

  @Transactional(readOnly = true)
  public Page<PostResponse> getPosts(int page, int size) {
    PageRequest pageRequest = PageRequest.of(page, size);

    return postRepository.findAll(pageRequest)
        .map(PostResponse::from);
  }

  @Transactional
  public void updatePost(Long postId, PostUpdateRequest postUpdateRequest) {
    Post foundPost = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);

    foundPost.updateTitle(postUpdateRequest.title());
    foundPost.updateContent(postUpdateRequest.content());
  }

  @Transactional
  public void deletePost(Long postId) {
    Post foundPost = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);

    postRepository.delete(foundPost);
  }
}
