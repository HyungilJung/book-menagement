create table category
(
    category_id integer      not null auto_increment,
    created_at  datetime(6) not null,
    name        varchar(255) not null,
    primary key (category_id)
) engine=InnoDB