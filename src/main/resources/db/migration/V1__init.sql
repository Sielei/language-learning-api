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
    constraint language_id_fk foreign key (language_id)
        references supported_languages (id)
        on update no action
        on delete cascade,
    constraint language_proficiency_check check ( language_proficiency:: text = any (array ['BEGINNER' :: character varying, 'INTERMEDIATE' :: character varying, 'ADVANCED' :: character varying] :: text []))
);

create table if not exists exercises (
    id uuid not null primary key,
    lesson_id uuid not null,
    task text not null,
    options text [],
    answers text[],
    constraint lesson_id_fk foreign key (lesson_id)
        references lessons (id)
        on update no action
        on delete cascade
);

create table if not exists user_progress (
    id uuid not null primary key,
    user_id uuid not null,
    lesson_id uuid not null,
    exercises_completed integer not null,
    exercises_passed uuid []
);

