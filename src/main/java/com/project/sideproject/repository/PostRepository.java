package com.project.sideproject.repository;

import com.project.sideproject.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @NonNull
    Optional<Post> findById(@NonNull Long id);

    @NonNull
    List<Post> findAll();
}


