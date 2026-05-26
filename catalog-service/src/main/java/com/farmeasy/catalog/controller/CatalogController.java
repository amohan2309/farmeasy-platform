package com.farmeasy.catalog.controller;

import com.farmeasy.catalog.entity.*;
import com.farmeasy.catalog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CatalogController {
    private final FarmAppRepository appRepo;
    private final ChapterRepository chapterRepo;
    private final TopicRepository topicRepo;
    private final SubtopicRepository subtopicRepo;

    @GetMapping("/api/apps")
    public List<FarmApp> listApps(@RequestParam(defaultValue = "en") String locale) {
        return appRepo.findByActiveTrueOrderBySortOrderAsc();
    }

    @GetMapping("/api/apps/{appId}")
    public FarmApp getApp(@PathVariable UUID appId) {
        return appRepo.findById(appId).orElseThrow();
    }

    @GetMapping("/api/content/apps/{appId}/chapters")
    public List<Chapter> chapters(@PathVariable UUID appId) {
        return chapterRepo.findByAppIdOrderBySortOrderAsc(appId);
    }

    @GetMapping("/api/content/chapters/{chapterId}/topics")
    public List<Topic> topics(@PathVariable UUID chapterId) {
        return topicRepo.findByChapterIdOrderBySortOrderAsc(chapterId);
    }

    @GetMapping("/api/content/topics/{topicId}/subtopics")
    public List<Subtopic> subtopics(@PathVariable UUID topicId) {
        return subtopicRepo.findByTopicIdOrderBySortOrderAsc(topicId);
    }
}
