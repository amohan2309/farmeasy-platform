package com.farmeasy.catalog.config;

import com.farmeasy.catalog.entity.*;
import com.farmeasy.catalog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final FarmAppRepository appRepo;
    private final ChapterRepository chapterRepo;
    private final TopicRepository topicRepo;
    private final SubtopicRepository subtopicRepo;

    @Override
    public void run(String... args) {
        if (appRepo.count() > 0) return;

        FarmApp cropDoctor = appRepo.save(FarmApp.builder()
                .code("crop-doctor")
                .titleEn("Crop Doctor")
                .titleHi("फसल डॉक्टर")
                .descriptionEn("AI-assisted crop and leaf analysis")
                .sortOrder(1)
                .active(true)
                .build());

        FarmApp schemes = appRepo.save(FarmApp.builder()
                .code("gov-schemes")
                .titleEn("Government Schemes")
                .titleHi("सरकारी योजनाएं")
                .descriptionEn("Subsidies and farmer programs")
                .sortOrder(2)
                .active(true)
                .build());

        seedMenu(cropDoctor.getId(), "Diagnosis", "निदान");
        seedMenu(schemes.getId(), "Apply", "आवेदन");
    }

    private void seedMenu(java.util.UUID appId, String chapterEn, String chapterHi) {
        Chapter chapter = chapterRepo.save(Chapter.builder()
                .appId(appId)
                .titleEn(chapterEn)
                .titleHi(chapterHi)
                .sortOrder(1)
                .build());
        Topic topic = topicRepo.save(Topic.builder()
                .chapterId(chapter.getId())
                .titleEn("Getting started")
                .titleHi("शुरुआत")
                .sortOrder(1)
                .build());
        subtopicRepo.save(Subtopic.builder()
                .topicId(topic.getId())
                .titleEn("Overview")
                .titleHi("अवलोकन")
                .contentType("article")
                .sortOrder(1)
                .build());
    }
}
