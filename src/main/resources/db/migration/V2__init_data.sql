insert into users (id, first_name, last_name, email, password, role) VALUES
('422c4d13-9a4e-4270-b564-22c9f5ca8088', 'Wallah bin', 'Walla', 'admin@llm.com', '$2a$12$RP/y9/b.Xs7/q.p5Ph0w7OGAprT5zKvtUiC9vNaH97QzMGAIbx0AK', 'TUTOR'),
('12818fa9-d7b1-4083-adbc-cac0f1c34bdf', 'Joshua', 'Bloch', 'jbloch@java.com', '$2a$12$RP/y9/b.Xs7/q.p5Ph0w7OGAprT5zKvtUiC9vNaH97QzMGAIbx0AK', 'TUTOR');


INSERT INTO password_resets(id, expiry, token, is_used, user_id) VALUES
('3e468594-6b0b-424a-9dc9-4c561f1005cd', '2023-06-12T11:15:31Z', '3d1fb1a7-91bb-46c9-842f-5bfe2b53d32f', true, '422c4d13-9a4e-4270-b564-22c9f5ca8088'),
('88600cae-b553-42ec-b50d-df5c9db311f6', CURRENT_DATE + TIME '23:59:59', 'ba24e2aa-2987-496c-9240-2f3b8fb48f91', false, '422c4d13-9a4e-4270-b564-22c9f5ca8088' );
-- END --


INSERT INTO supported_languages (id, name) VALUES ('ab6e7aee-f96c-4c4e-a598-f1e5ba792195', 'Swahili');

INSERT INTO lessons (id, language_id, index, language_proficiency, name, description, percentage_pass_mark) VALUES
('f07ed148-e56b-4abc-9468-d2fa30c72407', 'ab6e7aee-f96c-4c4e-a598-f1e5ba792195', 1, 'BEGINNER', 'Introduce Yourself', 'In this lesson you will learn how to introduce yourself in Swahili', 85);

INSERT INTO exercises (id, lesson_id, index, task, task_score, task_type) VALUES
('1021837d-fd96-4014-97fd-0394874a114f', 'f07ed148-e56b-4abc-9468-d2fa30c72407', 1, 'Write in Swahili: Your name is Herman', 5, 'SHORT_ANSWER'),
('fdd1f4dd-61ef-4785-a62e-ff7e823063b0', 'f07ed148-e56b-4abc-9468-d2fa30c72407', 2, 'Select the correct meaning: You', 1, 'MULTIPLE_CHOICE_SINGLE_ANSWER'),
('72fdb669-20ba-4058-8c11-f859b509f36e', 'f07ed148-e56b-4abc-9468-d2fa30c72407', 3, 'Select the matching pairs', 10, 'MATCHING');

INSERT INTO multiple_choices (id, exercise_id, index, is_matching_choice, choice) VALUES
('919652be-6ebe-43d8-a722-5546fba87038', 'fdd1f4dd-61ef-4785-a62e-ff7e823063b0', 1, false, 'Yeye'),
('ad4076a9-5666-4234-af28-15f4a8aeb4d0', 'fdd1f4dd-61ef-4785-a62e-ff7e823063b0', 2, false, 'Mimi'),
('6349d907-f565-4797-94bc-549347911ab6', 'fdd1f4dd-61ef-4785-a62e-ff7e823063b0', 3, false, 'Wewe'),
('351e02ef-3970-4363-a970-c8569e0e9455', '72fdb669-20ba-4058-8c11-f859b509f36e', 1, true, 'You'),
('021c4f73-ce62-4723-b8e0-b8acd6a81e90', '72fdb669-20ba-4058-8c11-f859b509f36e', 2, true, 'Is'),
('20af1576-90df-445b-8e85-61ebc9485352', '72fdb669-20ba-4058-8c11-f859b509f36e', 3, true, 'I'),
('5be7c45e-a340-44e0-a5cc-d8e73389837c', '72fdb669-20ba-4058-8c11-f859b509f36e', 4, true, 'She'),
('2e923baf-ca29-4bea-9b2b-068a1fbd3065', '72fdb669-20ba-4058-8c11-f859b509f36e', 5, true, 'Ni'),
('09366b97-d021-4be1-8baf-311c03e20f42', '72fdb669-20ba-4058-8c11-f859b509f36e', 6, true, 'Yeye'),
('ddf24f9d-be74-4f36-b061-b9213e49dae5', '72fdb669-20ba-4058-8c11-f859b509f36e', 7, true, 'Wewe'),
('59d948a7-8e48-42fd-9a08-f788b7d3ec4c', '72fdb669-20ba-4058-8c11-f859b509f36e', 8, true, 'Mimi');

INSERT INTO public.exercise_answers (id, exercise_id, multiple_choice_id, answer_text) VALUES
('be54f8de-534a-400e-ae49-e9f8c0398629', '1021837d-fd96-4014-97fd-0394874a114f', null, 'Jina lako ni Herman'),
('837c0819-bc65-44c2-8b0f-e1e417892ec1', 'fdd1f4dd-61ef-4785-a62e-ff7e823063b0', '6349d907-f565-4797-94bc-549347911ab6', null),
('fc560bcf-eb45-4cf6-8c6d-3e113ea5ba3a', '72fdb669-20ba-4058-8c11-f859b509f36e', null, 'You,Wewe'),
('afb95db2-e6eb-46af-a138-aeb2da7070b7', '72fdb669-20ba-4058-8c11-f859b509f36e', null, 'Is,Ni'),
('e1797bd9-09ec-403a-a360-f0a89a8bbc3b', '72fdb669-20ba-4058-8c11-f859b509f36e', null, 'I,Mimi'),
('1ebd303c-b7b4-4091-b1da-ed3160761910', '72fdb669-20ba-4058-8c11-f859b509f36e', null, 'She,Yeye');