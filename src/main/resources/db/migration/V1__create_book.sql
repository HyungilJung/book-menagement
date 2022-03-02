create table book
(
    book_id     bigint       not null auto_increment,
    created_at  datetime(6) not null,
    author      varchar(255) not null,
    title       varchar(255) not null,
    rent_status varchar(255) not null,
    category_id integer      not null,
    primary key (book_id)
) engine=InnoDB