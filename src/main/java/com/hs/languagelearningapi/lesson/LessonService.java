package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.DTO;
import com.hs.languagelearningapi.common.exception.LessonException;
import com.hs.languagelearningapi.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class LessonService {
    private final LanguageRepository languageRepository;
    private final LessonRepository lessonRepository;
    private final ExerciseRepository exerciseRepository;
    private final MultipleChoiceRepository multipleChoiceRepository;
    private final ExerciseAnswerRepository exerciseAnswerRepository;
    private final UserProgressRepository userProgressRepository;
    private final AnswerAttemptRepository answerAttemptRepository;

    public LessonService(LanguageRepository languageRepository,
                         LessonRepository lessonRepository,
                         ExerciseRepository exerciseRepository,
                         MultipleChoiceRepository multipleChoiceRepository,
                         ExerciseAnswerRepository exerciseAnswerRepository,
                         UserProgressRepository userProgressRepository,
                         AnswerAttemptRepository answerAttemptRepository) {
        this.languageRepository = languageRepository;
        this.lessonRepository = lessonRepository;
        this.exerciseRepository = exerciseRepository;
        this.multipleChoiceRepository = multipleChoiceRepository;
        this.exerciseAnswerRepository = exerciseAnswerRepository;
        this.userProgressRepository = userProgressRepository;
        this.answerAttemptRepository = answerAttemptRepository;
    }

    @Transactional
    public DTO.LanguageResponse addSupportedLanguage(DTO.LanguageRequest languageRequest) {
        var language = Language.builder().name(languageRequest.name()).build();
        var persistedLanguage = languageRepository.save(language);
        return new DTO.LanguageResponse(persistedLanguage.getId(), persistedLanguage.getName());
    }

    @Transactional(readOnly = true)
    public DTO.PagedCollection<DTO.LanguageResponse> findAllSupportedLanmguages(int page, int pageSize) {
        var pageNo = page > 0 ? page - 1 : 0;
        var pageable = PageRequest.of(pageNo, pageSize);
        var languagesPage = languageRepository.findAll(pageable)
                .map(language -> new DTO.LanguageResponse(language.getId(), language.getName()));
        return new DTO.PagedCollection<>(
                languagesPage.getContent(),
                languagesPage.getTotalElements(),
                languagesPage.getNumber() + 1,
                languagesPage.getTotalPages(),
                languagesPage.isFirst(),
                languagesPage.isLast(),
                languagesPage.hasNext(),
                languagesPage.hasPrevious()
        );
    }

    @Transactional
    public DTO.LanguageResponse updateSupportedLanguage(UUID languageId, DTO.LanguageRequest languageRequest) {
        var languageToUpdate = languageRepository.findLanguageById(languageId);
        languageToUpdate.setName(languageRequest.name());
        var updatedLanguage = languageRepository.save(languageToUpdate);
        return new DTO.LanguageResponse(updatedLanguage.getId(), updatedLanguage.getName());
    }

    @Transactional
    public void deleteSupportedLanguage(UUID languageId) {
        var languageToDelete = languageRepository.findLanguageById(languageId);
        languageRepository.delete(languageToDelete);
    }

    @Transactional(readOnly = true)
    public DTO.LanguageResponse findSupportedLanguageById(UUID languageId) {
        var language = languageRepository.findLanguageById(languageId);
        return new DTO.LanguageResponse(language.getId(), language.getName());
    }

    @Transactional
    public DTO.LessonResponse createLanguageLesson(DTO.LessonRequest lessonRequest) {
        var lesson = mapLessonRequestToLesson(lessonRequest);
        var persistedLesson = lessonRepository.save(lesson);
        return mapLessonToLessonResponse(persistedLesson);
    }

    private DTO.LessonResponse mapLessonToLessonResponse(Lesson lesson) {
        return new DTO.LessonResponse(lesson.getId(), lesson.getIndex(), lesson.getName(), lesson.getDescription(),
                lesson.getLanguage().getName(), lesson.getLanguageProficiency());
    }

    private Lesson mapLessonRequestToLesson(DTO.LessonRequest lessonRequest) {
        var language = languageRepository.findLanguageById(lessonRequest.languageId());
        return Lesson.builder()
                .language(language)
                .index(lessonRequest.index())
                .percentagePassMark(lessonRequest.percentagePassMark())
                .name(lessonRequest.name())
                .description(lessonRequest.description())
                .languageProficiency(lessonRequest.languageProficiency())
                .build();
    }

    @Transactional(readOnly = true)
    public DTO.LessonResponse findLanguageLessonById(UUID lessonId) {
        var lesson = lessonRepository.findLessonById(lessonId);
        return mapLessonToLessonResponse(lesson);
    }

    @Transactional(readOnly = true)
    public DTO.PagedCollection<DTO.LessonResponse> findAllLanguageLessons(UUID languageId, int page, int pageSize) {
        var pageNo = page > 0 ? page - 1 : 0;
        var pageable = PageRequest.of(pageNo, pageSize);
        var lessonsPage = lessonRepository.findAllLanguageLessons(languageId, pageable)
                .map(this::mapLessonToLessonResponse);
        return new DTO.PagedCollection<>(
                lessonsPage.getContent(),
                lessonsPage.getTotalElements(),
                lessonsPage.getNumber() + 1,
                lessonsPage.getTotalPages(),
                lessonsPage.isFirst(),
                lessonsPage.isLast(),
                lessonsPage.hasNext(),
                lessonsPage.hasPrevious()
        );

    }

    @Transactional
    public void updateLanguageLesson(UUID lessonId, DTO.LessonRequest lessonRequest) {
        var lessonToUpdate = lessonRepository.findLessonById(lessonId);
        var language = languageRepository.findLanguageById(lessonRequest.languageId());
        lessonToUpdate.setLanguage(language);
        lessonToUpdate.setName(lessonToUpdate.getName());
        lessonToUpdate.setDescription(lessonToUpdate.getDescription());
        lessonToUpdate.setLanguageProficiency(lessonRequest.languageProficiency());
        lessonRepository.save(lessonToUpdate);
    }

    @Transactional
    public void deleteLanguageLesson(UUID lessonId) {
        var lessonToDelete = lessonRepository.findLessonById(lessonId);
        lessonRepository.delete(lessonToDelete);
    }

    @Transactional
    public DTO.CreateExerciseResponse createLessonExercise(UUID lessonId, DTO.ExerciseRequest exerciseRequest) {
        var lesson = lessonRepository.findLessonById(lessonId);
        var exercise = mapExerciseRequestToExercise(lesson, exerciseRequest);
        var persistedExercise = exerciseRepository.save(exercise);
        var multipleChoices = new ArrayList<MultipleChoice>();
        var matchingChoices = new ArrayList<MultipleChoice>();
        if (exerciseRequest.multipleChoices() != null && !exerciseRequest.multipleChoices().isEmpty()){
            multipleChoices.addAll(saveExerciseMultipleChoices(persistedExercise, exerciseRequest.multipleChoices()));
        }
        if (exerciseRequest.matchingChoices() != null && !exerciseRequest.matchingChoices().isEmpty()){
            matchingChoices.addAll(saveExerciseMatchingChoices(persistedExercise, exerciseRequest.matchingChoices()));
        }
        saveExerciseAnswers(persistedExercise, exerciseRequest.answer());
        return mapExerciseToCreateExerciseResponse(persistedExercise, multipleChoices, matchingChoices);
    }

    private List<MultipleChoice> saveExerciseMatchingChoices(Exercise exercise,
            List<DTO.MatchingChoice> matchingChoices) {
        var newMatchingChoices = new ArrayList<MultipleChoice>();
        matchingChoices.stream()
                .map(matchingChoice -> mapMatchingChoiceToMultipleChoice(exercise, matchingChoice))
                .forEach(matchingChoice -> {
                    var persistedChoice = multipleChoiceRepository.save(matchingChoice);
                    newMatchingChoices.add(persistedChoice);
                });
        return newMatchingChoices;
    }

    private MultipleChoice mapMatchingChoiceToMultipleChoice(Exercise exercise, DTO.MatchingChoice matchingChoice) {
        return MultipleChoice.builder()
                .exercise(exercise)
                .index(matchingChoice.index())
                .isMatchingChoice(true)
                .choice(matchingChoice.choice())
                .build();
    }

    private DTO.CreateExerciseResponse mapExerciseToCreateExerciseResponse(Exercise exercise,
                                                                           List<MultipleChoice> multipleChoices,
                                                                           List<MultipleChoice> matchingChoices) {
        var multipleChoiceDto = mapMultipleChoicesToMultipleChoiceDtos(multipleChoices);
        return new DTO.CreateExerciseResponse(exercise.getId(), exercise.getIndex(), exercise.getTask(),
                exercise.getTaskScore(), exercise.getTaskType(), multipleChoiceDto);
    }

    private List<DTO.MultipleChoiceDto> mapMultipleChoicesToMultipleChoiceDtos(List<MultipleChoice> multipleChoices) {
        return multipleChoices.stream()
                .map(this::mapMultipleChoiceToMultipleChoiceDto)
                .toList();
    }

    private DTO.MultipleChoiceDto mapMultipleChoiceToMultipleChoiceDto(MultipleChoice multipleChoice) {
        return new DTO.MultipleChoiceDto(multipleChoice.getIndex(), multipleChoice.getChoice());
    }

    private List<ExerciseAnswer> saveExerciseAnswers(Exercise exercise, DTO.Answer answer) {
        var exerciseAnswers = new ArrayList<ExerciseAnswer>();
        var exerciseType = exercise.getTaskType();
        if (exerciseType == DTO.TaskType.SHORT_ANSWER){
            var exerciseAnswer = ExerciseAnswer.builder()
                    .exercise(exercise)
                    .answerText(answer.answerText())
                    .build();
            var persistedAnswer = exerciseAnswerRepository.save(exerciseAnswer);
            exerciseAnswers.add(persistedAnswer);
        }
        else if (exerciseType == DTO.TaskType.MATCHING) {
            answer.matchingPairs()
                    .stream()
                    .map(list -> String.join(",", list))
                    .forEach(s -> {
                        var exerciseAnswer = ExerciseAnswer.builder()
                                .exercise(exercise)
                                .answerText(s)
                                .build();
                        var persistedAnswer = exerciseAnswerRepository.save(exerciseAnswer);
                        exerciseAnswers.add(persistedAnswer);
                    });
        }
        else if (exerciseType == DTO.TaskType.MULTIPLE_CHOICE_SINGLE_ANSWER ||
                exerciseType == DTO.TaskType.MULTIPLE_CHOICE_MULTIPLE_ANSWERS) {
            var answerIndices  = answer.multipleChoices();
            answerIndices.stream()
                    .map(index -> multipleChoiceRepository.findMultipleChoiceByExerciseAndIndex(exercise, index))
                    .map(multipleChoice -> ExerciseAnswer.builder()
                            .exercise(exercise)
                            .multipleChoice(multipleChoice)
                            .build())
                    .forEach(exerciseAnswer -> {
                        var savedAnswer = exerciseAnswerRepository.save(exerciseAnswer);
                        exerciseAnswers.add(savedAnswer);
                    });
        }
        return exerciseAnswers;
    }

    private List<MultipleChoice> saveExerciseMultipleChoices(Exercise exercise, List<DTO.MultipleChoiceDto> multipleChoices) {
        var newMultipleChoices = new ArrayList<MultipleChoice>();
        multipleChoices.stream()
                .map(multipleChoiceDto -> mapMultipleChoiceDtoToMultipleChoice(exercise, multipleChoiceDto))
                .forEach(multipleChoice -> {
                    var persistedChoice = multipleChoiceRepository.save(multipleChoice);
                    newMultipleChoices.add(persistedChoice);
                });
        return newMultipleChoices;
    }

    private MultipleChoice mapMultipleChoiceDtoToMultipleChoice(Exercise exercise,
                                                                DTO.MultipleChoiceDto multipleChoiceDto) {
        return MultipleChoice.builder()
                .exercise(exercise)
                .index(multipleChoiceDto.index())
                .choice(multipleChoiceDto.choice())
                .isMatchingChoice(false)
                .build();
    }

    private Exercise mapExerciseRequestToExercise(Lesson lesson, DTO.ExerciseRequest exerciseRequest) {
        return Exercise.builder()
                .lesson(lesson)
                .index(exerciseRequest.index())
                .task(exerciseRequest.task())
                .taskScore(exerciseRequest.taskScore())
                .taskType(exerciseRequest.taskType())
                .build();
    }

    @Transactional
    public void deleteLessonExercise(UUID lessonId, UUID exerciseId) {
        var exerciseToDelete = exerciseRepository.findExerciseById(lessonId, exerciseId);
        exerciseRepository.delete(exerciseToDelete);
    }

    @Transactional(readOnly = true)
    public DTO.ExerciseResponse findLessonExerciseById(UUID lessonId, UUID exerciseId) {
        var exercise = exerciseRepository.findExerciseById(lessonId, exerciseId);
        return new DTO.ExerciseResponse(exercise.getId(), exercise.getIndex(), exercise.getTask(),
                exercise.getTaskScore(), exercise.getTaskType(),
                mapMultipleChoicesToMultipleChoiceDtos(exercise.getMultipleChoices()));
    }

    @Transactional(readOnly = true)
    public DTO.PagedCollection<DTO.ExerciseResponse> findAllLessonExercises(UUID lessonId, 
                                                                                  int page, int pageSize) {
        var pageNo = page > 0 ? page - 1 : 0;
        var pageable = PageRequest.of(pageNo, pageSize);
        var exercisesPage = exerciseRepository.findAllLessonExercises(lessonId, pageable)
                .map(exercise -> mapExerciseToExerciseResponse(exercise));
        return new DTO.PagedCollection<>(
                exercisesPage.getContent(),
                exercisesPage.getTotalElements(),
                exercisesPage.getNumber() + 1,
                exercisesPage.getTotalPages(),
                exercisesPage.isFirst(),
                exercisesPage.isLast(),
                exercisesPage.hasNext(),
                exercisesPage.hasPrevious()
        );
    }

    private DTO.ExerciseResponse mapExerciseToExerciseResponse(Exercise exercise) {
        return  new DTO.ExerciseResponse(exercise.getId(), exercise.getIndex(), exercise.getTask(),
                exercise.getTaskScore(), exercise.getTaskType(),
                mapMultipleChoicesToMultipleChoiceDtos(exercise.getMultipleChoices()));
    }

    @Transactional
    public void updateLessonExercise(UUID lessonId, UUID exerciseId, DTO.ExerciseRequest exerciseRequest) {
        var exerciseToUpdate = exerciseRepository.findExerciseById(lessonId, exerciseId);
    }

    @Transactional
    public DTO.LessonResponse findLessonByLanguageProficiencyAndPriority(UUID userId, UUID languageId,
                                                                         DTO.LanguageProficiency proficiency) {
        var attemptedLessons = userProgressRepository.findByUserIdAndLessonLanguageIdAndLessonLanguageProficiency(userId, languageId, proficiency);
        if (!attemptedLessons.isEmpty()){
            throw new RuntimeException("You cannot start a new lesson while another lesson is in progress.");
        }
        var firstLesson = lessonRepository.getFirstLesson(languageId, proficiency);
        var numberOfExercises = countNumberOfExercises(firstLesson);
        var totalScore = getLessonTotalScore(firstLesson);
        var userProgress = UserProgress.builder()
                .userId(userId)
                .lesson(firstLesson)
                .attemptedExercises(0)
                .pendingExercises(numberOfExercises)
                .failedExercises(0)
                .userScore(0)
                .totalScore(totalScore)
                .build();
        userProgressRepository.save(userProgress);
        return mapLessonToLessonResponse(firstLesson);
    }

    private int getLessonTotalScore(Lesson lesson) {
        return exerciseRepository.findAllByLesson(lesson).stream()
                .map(Exercise::getTaskScore)
                .reduce(0, Integer::sum, Integer::sum);
    }

    private Integer countNumberOfExercises(Lesson lesson) {
        return exerciseRepository.findAllByLesson(lesson).size();
    }

    @Transactional
    public DTO.ResponseFeedback submitExerciseAnswer(UUID lessonId, UUID exerciseId, UUID userId,
                                                     DTO.Answer answer) {
        verifyUserHasNotTriedExercise(userId, exerciseId);
        var exercise = exerciseRepository.findExerciseById(lessonId, exerciseId);
        var exerciseScore = exercise.getTaskScore();
        var answerAtt = AnswerAttempt.builder().build();
        var awardedMarks = 0;
        var status = DTO.AttemptStatus.FAILED;
        if (exercise.getTaskType() == DTO.TaskType.SHORT_ANSWER){
            var isCorrect = verifyShortAnswer(exercise, answer.answerText());
            var answerAttempt = AnswerAttempt.builder()
                    .userId(userId)
                    .exercise(exercise)
                    .userAnswer(answer.answerText())
                    .marksAwarded(isCorrect ? exerciseScore : 0)
                    .status(isCorrect ? DTO.AttemptStatus.PASSED : DTO.AttemptStatus.FAILED )
                    .build();
            answerAtt = answerAttemptRepository.save(answerAttempt);
        }
        else if (exercise.getTaskType() == DTO.TaskType.MATCHING){
            var answers = exerciseAnswerRepository.findAllByExercise(exercise).stream()
                    .map(exerciseAnswer -> exerciseAnswer.getAnswerText()).toList();
            var userAns = answer.matchingPairs().stream()
                    .map(list -> String.join(",", list))
                    .toList();
            if (answers.size() == userAns.size() && answers.containsAll(userAns)){
                awardedMarks = exerciseScore;
                status = DTO.AttemptStatus.PASSED;
                var answerAttempt = AnswerAttempt.builder()
                        .userId(userId)
                        .exercise(exercise)
                        .marksAwarded(awardedMarks)
                        .status(status )
                        .build();
                answerAtt = answerAttemptRepository.save(answerAttempt);
            }
        }
        else {
            var multipleChoiceAns = answer.multipleChoices().stream()
                    .map(idx -> multipleChoiceRepository.findMultipleChoiceByExerciseAndIndex(exercise, idx))
                    .toList();
            var exerciseAns = exerciseAnswerRepository.findAllByExercise(exercise).stream()
                    .map(ExerciseAnswer::getMultipleChoice).toList();
            if (exerciseAns.size() == multipleChoiceAns.size() && exerciseAns.containsAll(multipleChoiceAns)){
                awardedMarks = exerciseScore;
                status = DTO.AttemptStatus.PASSED;
            }
            var answerAttempt = AnswerAttempt.builder()
                    .userId(userId)
                    .exercise(exercise)
                    .marksAwarded(awardedMarks)
                    .status(status )
                    .build();
            answerAtt = answerAttemptRepository.save(answerAttempt);

        }
        // update user progress
        var userProgress = userProgressRepository.findProgressByLesson(exercise.getLesson(), userId);
        userProgress.setAttemptedExercises(userProgress.getAttemptedExercises()+1);
        userProgress.setPendingExercises(exerciseRepository.findPendingExercises(userId, lessonId));
        userProgress.setFailedExercises(answerAttemptRepository.findFailedExercises(userId, lessonId, DTO.AttemptStatus.FAILED));
        userProgress.setUserScore(answerAttemptRepository.findCurrentUserScore(userId, lessonId));
        userProgress.setTotalScore(exerciseRepository.findTotalScore(lessonId));
        var updatedUserProgress = userProgressRepository.save(userProgress);
        return new DTO.ResponseFeedback(answerAtt.getStatus().toString(), updatedUserProgress.getUserScore(),
                updatedUserProgress.getAttemptedExercises(), updatedUserProgress.getPendingExercises(),
                updatedUserProgress.getTotalScore());
    }

    private void verifyUserHasNotTriedExercise(UUID userId, UUID exerciseId) {
        var attempt = answerAttemptRepository.findByUserIdAndExerciseId(userId, exerciseId);
        if (attempt.isPresent()){
            throw new LessonException("You have already practised this exercise");
        }
    }

    private Boolean verifyShortAnswer(Exercise exercise, String shortAnswer) {
        var answer = exerciseAnswerRepository.findAllByExercise(exercise).stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Answer for exercise is not available"));
        return answer.getAnswerText().equalsIgnoreCase(shortAnswer.trim());
    }

    @Transactional(readOnly = true)
    public DTO.LessonResponse getRecommendation(UUID userId, UUID languageId) {
        var userProgress = userProgressRepository.findByUserId(userId);
        var pendingProgress = userProgress
                .stream().filter(userP -> userP.getPendingExercises() > 0)
                .findFirst();
        if (pendingProgress.isPresent()){
            return mapLessonToLessonResponse(pendingProgress.get().getLesson());
        }
        var mostRecentLesson = userProgress.stream()
                .max(Comparator.comparing(userProg -> userProg.getLesson().getIndex() ));
        if (mostRecentLesson.isPresent()){
            var lastIndex = mostRecentLesson.get().getLesson().getIndex();
            var languageProficiency = mostRecentLesson.get().getLesson().getLanguageProficiency();
            var lesson = lessonRepository.findNextLesson(languageId, languageProficiency, lastIndex);
            if (lesson.isPresent()){
                return mapLessonToLessonResponse(lesson.get());
            }
        }
        var lesson = lessonRepository.getFirstLesson(languageId, DTO.LanguageProficiency.BEGINNER);
        return  mapLessonToLessonResponse(lesson);
    }
}
