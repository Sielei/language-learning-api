package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.DTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Practise", description = "API for learning a language by practicing exercises")
@RestController
@RequestMapping("/api/v1/practices")
public class PracticeController {
    private final LessonService lessonService;

    public PracticeController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

}
