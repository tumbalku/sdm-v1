package com.sdm.app.controller;

import com.sdm.app.entity.User;
import com.sdm.app.model.req.create.CreatePostRequest;
import com.sdm.app.model.req.search.SearchPostRequest;
import com.sdm.app.model.res.PostResponse;
import com.sdm.app.model.res.WebResponse;
import com.sdm.app.model.res.WebResponseWithPaging;
import com.sdm.app.service.impl.PostServiceImpl;
import com.sdm.app.utils.ResponseConverter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/posts")
@AllArgsConstructor
public class PostController {

  private final PostServiceImpl postService;

  @GetMapping
  public WebResponseWithPaging<List<PostResponse>> search(@RequestParam(name = "content", required = false) String content,
                                                          @RequestParam(name = "priority", required = false) Integer priority,
                                                          @RequestParam(name = "dateSortBy", required = false, defaultValue = "latest") String dateSortBy,
                                                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                          @RequestParam(name = "size", required = false, defaultValue = "10") Integer size){

    SearchPostRequest request = SearchPostRequest.builder()
            .priority(priority)
            .content(content)
            .dateSortBy(dateSortBy)
            .size(size)
            .page(page)
            .build();

    Page<PostResponse> responses = postService.searchPosts(request);

    return WebResponseWithPaging.<List<PostResponse>>builder()
            .data(responses.getContent())
            .message("Search Success")
            .pagination(ResponseConverter.getPagingResponse(responses))
            .build();
  }

  @PostMapping
  public WebResponse<PostResponse> create(User user, @RequestBody CreatePostRequest request){

    PostResponse response = postService.create(user, request);

    return WebResponse.<PostResponse>builder()
            .data(response)
            .message("Create Post")
            .build();
  }

  @PatchMapping("/{id}")
  public WebResponse<PostResponse> update(User user, @PathVariable("id") String id, @RequestBody CreatePostRequest request){
    request.setId(id);
    PostResponse response = postService.update(user, request);

    return WebResponse.<PostResponse>builder()
            .data(response)
            .message("Success update")
            .build();
  }

  @GetMapping("/{id}")
  public WebResponse<PostResponse> find(@PathVariable("id") String id){

    PostResponse response = postService.findPost(id);

    return WebResponse.<PostResponse>builder()
            .data(response)
            .message("success find")
            .build();
  }

  @DeleteMapping("/{id}")
  public WebResponse<PostResponse> delete(User user, @PathVariable("id") String id){

    PostResponse response = postService.delete(id);

    return WebResponse.<PostResponse>builder()
            .data(response)
            .message("Post has been deleted")
            .build();
  }
}
