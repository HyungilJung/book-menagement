INSERT INTO category (created_at, name)
VALUES (now(), '문학');
INSERT INTO category (created_at, name)
VALUES (now(), '경제 경영');
INSERT INTO category (created_at, name)
VALUES (now(), '인문학');
INSERT INTO category (created_at, name)
VALUES (now(), 'IT');
INSERT INTO category (created_at, name)
VALUES (now(), '과학');

INSERT INTO book (created_at, author, title, rent_status, category_id)
VALUES (now(), '권태영', '너에게 해주지 못한 말들', '대여 가능', 1);
INSERT INTO book (created_at, author, title, rent_status, category_id)
VALUES (now(), '현영서', '단순하게 배부르게', '대여 가능', 1);
INSERT INTO book (created_at, author, title, rent_status, category_id)
VALUES (now(), '권태영', '게으른 사랑', '대여 가능', 1);
INSERT INTO book (created_at, author, title, rent_status, category_id)
VALUES (now(), '권태영', '트렌드 코리아 2322', '대여 가능', 2);
INSERT INTO book (created_at, author, title, rent_status, category_id)
VALUES (now(), '장동혁', '초격자 투자', '대여 가능', 2);
INSERT INTO book (created_at, author, title, rent_status, category_id)
VALUES (now(), '홍길동', '파이어족 강환국의 하면 되지 않는다! 퀀트 투자', '대여 가능', 2);
INSERT INTO book (created_at, author, title, rent_status, category_id)
VALUES (now(), '이서연', '진심보다 밥', '대여 가능', 3);
INSERT INTO book (created_at, author, title, rent_status, category_id)
VALUES (now(), '위성원', '실패에 대하여 생각하지 마라', '대여 가능', 3);
INSERT INTO book (created_at, author, title, rent_status, category_id)
VALUES (now(), '지승열', '실리콘밸리 리더십 쉽다', '대여 가능', 4);
INSERT INTO book (created_at, author, title, rent_status, category_id)
VALUES (now(), '지승열', '데이터분석을 위한 A 프로그래밍', '대여 가능', 4);
INSERT INTO book (created_at, author, title, rent_status, category_id)
VALUES (now(), '장동혁', '인공지능1-12', '대여 가능', 4);
INSERT INTO book (created_at, author, title, rent_status, category_id)
VALUES (now(), '위성원', '-1년차 게임 개발', '대여 가능', 4);
INSERT INTO book (created_at, author, title, rent_status, category_id)
VALUES (now(), '권태영', 'Skye가 알려주는 피부 채색의 비결', '대여 가능', 5);
INSERT INTO book (created_at, author, title, rent_status, category_id)
VALUES (now(), '장지영', '자연의 발전', '대여 가능', 5);
INSERT INTO book (created_at, author, title, rent_status, category_id)
VALUES (now(), '이승열', '코스모스 필 무렵', '대여 가능', 5);