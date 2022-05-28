package com.project.sideproject.repo;

import com.project.sideproject.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface  PostRepository extends CrudRepository<Post, Long> {


}
