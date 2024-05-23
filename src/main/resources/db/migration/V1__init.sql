create table if not exists users (
    id uuid not null primary key,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    email varchar(100) not null unique,
    password varchar(255) not null,
    role varchar(255) not null,
    constraint users_role_check check ( role::text = any (array ['TUTOR'::character varying, 'LEARNER':: character varying] ::text []))
);

create table if not exists password_resets (
    id uuid not null primary key,
    user_id uuid not null,
    token text not null,
    token_expiry timestamp not null,
    is_used boolean not null default false,
    constraint user_id_fk foreign key (user_id)
        references users(id)
        on update no action
        on delete no action
);

create table if not exists supported_languages (
    id uuid not null primary key,
    name varchar(100)
);

create table if not exists lessons (
    id uuid not null primary key,
    language_id uuid not null,
    language_proficiency varchar(100),
    name varchar (255) not null,
    description text,
    percentage_pass_mark integer not null,
    constraint language_id_fk foreign key (language_id)
        references supported_languages (id)
        on update no action
        on delete cascade,
    constraint language_proficiency_check check ( language_proficiency:: text = any (array ['BEGINNER' :: character varying, 'INTERMEDIATE' :: character varying, 'ADVANCED' :: character varying] :: text []))
);

create table if not exists exercises (
    id uuid not null primary key,
    lesson_id uuid not null,
    index integer not null,
    task text not null,
    task_score integer not null,
    task_type varchar(100) not null,
    constraint lesson_id_fk foreign key (lesson_id)
        references lessons (id)
        on update no action
        on delete cascade,
    constraint task_type_check check ( task_type:: text = any (array ['MULTIPLE_CHOICE_SINGLE_ANSWER' :: character varying, 'MULTIPLE_CHOICE_MULTIPLE_ANSWERS' :: character varying, 'TRUE_OR_FALSE' :: character varying, 'SHORT_ANSWER' :: character varying, 'MATCHING' :: character varying] :: text []))
);

create table if not exists multiple_choices (
    id uuid not null primary key,
    exercise_id uuid not null,
    index integer not null,
    is_matching_choice boolean default false,
    is_left_choice boolean,
    choice varchar(255) not null,
    constraint exercise_id_fk foreign key (exercise_id)
        references exercises (id)
        on update no action
        on delete cascade
);

create table if not exists exercise_answers (
    id uuid not null primary key,
    exercise_id uuid not null,
    multiple_choice_id uuid,
    answer_text varchar(255)
);

create table if not exists answer_attempts (
    id uuid not null primary key,
    user_id uuid not null,
    exercise_id uuid not null,
    user_answer varchar (255) not null,
    status varchar (100) not null,
    constraint user_id_fk_attempts foreign key (user_id)
        references users (id)
        on update no action
        on delete no action,
    constraint exercise_id_fk_attempts foreign key (exercise_id)
        references exercises (id)
        on update no action
        on delete no action,
    constraint status_check check ( status ::text = any ( array ['PASSED' :: character varying, 'FAILED' :: character varying] :: text []))
);

create table if not exists user_progress (
    id uuid not null primary key,
    user_id uuid not null,
    lesson_id uuid not null,
    attempted_exercise integer default 0,
    pending_exercises integer default 0,
    failed_exercises integer default 0,
    score integer default 0,
    total_score integer default 0,
    constraint user_id_fk_progress foreign key (user_id)
        references users(id)
        on update no action
        on delete no action,
    constraint lesson_id_fk_progress foreign key (lesson_id)
        references lessons(id)
        on update no action
        on delete no action
);

