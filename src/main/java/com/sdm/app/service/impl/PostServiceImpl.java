package com.sdm.app.service.impl;


import com.sdm.app.entity.Post;
import com.sdm.app.entity.User;
import com.sdm.app.model.req.create.CreatePostRequest;
import com.sdm.app.model.req.search.SearchPostRequest;
import com.sdm.app.model.req.update.PostPriorityRequest;
import com.sdm.app.model.res.PostResponse;
import com.sdm.app.repository.PostRepository;
import com.sdm.app.utils.GeneralHelper;
import com.sdm.app.utils.ResponseConverter;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl {

  private final PostRepository postRepository;


  @Transactional
  public PostResponse pinPriority(User admin, PostPriorityRequest request){
    GeneralHelper.isAdmin(admin);
    Post post = getPost(request.getId());
    post.setPriority(request.getPriority());
    postRepository.save(post);
    return ResponseConverter.postToResponse(post);
  }

  @Transactional(readOnly = true)
  private Post getPost(String id){
    return postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found!"));
  }

  @Transactional(readOnly = true)
  public PostResponse findPost(String id){
    Post post = getPost(id);
    return ResponseConverter.postToResponse(post);
  }

  @Transactional
  public PostResponse delete(User admin, String id){
    GeneralHelper.isAdmin(admin);
    Post post = getPost(id);
    postRepository.delete(post);
    return ResponseConverter.postToResponse(post);
  }

  @Transactional
  public PostResponse update(User admin, CreatePostRequest request){
    GeneralHelper.isAdmin(admin);
    Post post = getPost(request.getId());
    if(!post.getUser().getId().equals(admin.getId())){
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your account not allow to modify this post");
    }
    Optional.ofNullable(request.getTitle()).filter(StringUtils::hasText).ifPresent(post::setTitle);
    Optional.ofNullable(request.getContent()).filter(StringUtils::hasText).ifPresent(post::setContent);
    post.setImage(request.getImageUrl());
    post.setUpdatedAt(LocalDateTime.now());

    postRepository.save(post);
    return ResponseConverter.postToResponse(post);
  }

  @Transactional
  public PostResponse create(User admin, CreatePostRequest request){
    GeneralHelper.isAdmin(admin);
    Post post = new Post();
    post.setId(UUID.randomUUID().toString());
    post.setTitle(request.getTitle());
    post.setContent(request.getContent());
    post.setImage(request.getImageUrl());
    post.setUser(admin);
    post.setCreatedAt(LocalDateTime.now());
    post.setUpdatedAt(LocalDateTime.now());

    postRepository.save(post);
    return ResponseConverter.postToResponse(post);
  }

  @Transactional(readOnly = true)
  public Page<PostResponse> searchPosts(SearchPostRequest request){

    int page = request.getPage() - 1;

    Specification<Post> specification = (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if(Objects.nonNull(request.getContent())){
        predicates.add(builder.or(
                builder.like(root.get("title"), "%" + request.getContent() + "%"),
                builder.like(root.get("content"), "%" + request.getContent() + "%")));
      }

      if (Objects.nonNull(request.getPriority())) {
        predicates.add(builder.equal(root.get("priority"), request.getPriority()));
      }

      return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
    };


    Sort.Direction sortDirection = "latest".equalsIgnoreCase(request.getDateSortBy()) ? Sort.Direction.DESC : Sort.Direction.ASC;

    Sort sort = Sort.by(
            Sort.Order.desc("priority"),
            new Sort.Order(sortDirection, "createdAt")
    );

    Pageable pageable = PageRequest.of(page, request.getSize(), sort);
    Page<Post> posts = postRepository.findAll(specification, pageable);
    List<PostResponse> postResponse = posts.getContent().stream()
            .map(ResponseConverter::postToResponse)
            .collect(Collectors.toList());

    return new PageImpl<>(postResponse, pageable, posts.getTotalElements());
  }
}
