package com.farmeasy.catalog.repository;

import com.farmeasy.catalog.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TopicRepository extends JpaRepository<Topic, UUID> {
    List<Topic> findByChapterIdOrderBySortOrderAsc(UUID chapterId);
}
