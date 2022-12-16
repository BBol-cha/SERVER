INSERT INTO authority (authority_name) VALUES ('ROLE_USER');
INSERT INTO authority (authority_name) VALUES ('ROLE_ADMIN');

INSERT INTO user (id,email,name,pw) VALUES (1,'1000playch@naver.com','admin','$2a$10$D99zvd9eSCquwrkA5ss7L.GiYshRHu2x.MEgvTbk80SpnGahGNKse');
INSERT INTO user (id,email,name,pw) VALUES (2,'1000peach@naver.com','user','$2a$10$D99zvd9eSCquwrkA5ss7L.GiYshRHu2x.MEgvTbk80SpnGahGNKse');

INSERT INTO user_authority (id,authority_name) VALUES (1,'ROLE_ADMIN');
INSERT INTO user_authority (id,authority_name) VALUES (1,'ROLE_USER');
INSERT INTO user_authority (id,authority_name) VALUES (2,'ROLE_USER');

INSERT INTO board (id,uid,name,title,note,views,answer,create_at,update_at,bimg) VALUES (1,1,"admin","제가 찾았던 문제입니다!",
"문제 내용 : 석구의 부모님은 TV에 나와 연일 대중들의 입방아에 오르내리고 있었다. 효진은 이런 석구의 부모님을 보고 “ 너희 부모님이 또 뉴스에 나오고 있어!” 라고 말해줬다. 이 말을 들은 석구는 더 슬퍼졌다. 왜 그랬을까?",
0, "정답","2022-12-09T11:49:13","2022-12-09T11:49:13","a.png");

INSERT INTO board (id,uid,name,title,note,views,answer,create_at,update_at,bimg) VALUES (2,2,"user","이 문제 재밌네요",
"문제 내용 : 한 소녀가 살고 있었다. 소녀의 부모님은 외출할 때마다 항상 이렇게 말했다. “절대로 지하실 문을 열어보면 안 돼!” 하지만 소녀는 14살이 되던 해 생일날 또다시 집에 혼자 남게 된다. 부모님은 이번에도 외출하면서 절대로 지하실 문을 열지 말라며 신신당부했다. 하지만 호기심에 가득 찬 소녀는 살짝 지하실 문을 열어본다. 이후 소녀는 실어증에 걸릴 정도로 큰 충격에 빠지게 된다. 이 소녀는 본 것은 무엇이었을까?",
0, "정답","2022-12-09T11:49:13","2022-12-09T11:49:13","b.png");

INSERT INTO board (id,uid,name,title,note,views,answer,create_at,update_at,bimg) VALUES (3,1,"user","이게 실화라뇨..",
"문제 내용 : 청소원 A는 “누가 토마토 소스를 이렇게 많이 버린거야.” 생각했다. ",
0, "정답","2022-12-09T11:49:13","2022-12-09T11:49:13","c.png");

INSERT INTO board (id,uid,name,title,note,views,answer,create_at,update_at,bimg) VALUES (4,1,"user","이게 실화라뇨..",
"문제 내용 : 청소원 A는 “누가 토마토 소스를 이렇게 많이 버린거야.” 생각했다. ",
0, "정답","2022-12-09T11:49:13","2022-12-09T11:49:13","c.png");

INSERT INTO board (id,uid,name,title,note,views,answer,create_at,update_at,bimg) VALUES (5,1,"user","이게 실화라뇨..",
"문제 내용 : 청소원 A는 “누가 토마토 소스를 이렇게 많이 버린거야.” 생각했다. ",
0, "정답","2022-12-09T11:49:13","2022-12-09T11:49:13","c.png");

INSERT INTO board (id,uid,name,title,note,views,answer,create_at,update_at,bimg) VALUES (6,1,"user","이게 실화라뇨..",
"문제 내용 : 청소원 A는 “누가 토마토 소스를 이렇게 많이 버린거야.” 생각했다. ",
0, "정답","2022-12-09T11:49:13","2022-12-09T11:49:13","c.png");

INSERT INTO board (id,uid,name,title,note,views,answer,create_at,update_at,bimg) VALUES (7,1,"user","이게 실화라뇨..",
"문제 내용 : 청소원 A는 “누가 토마토 소스를 이렇게 많이 버린거야.” 생각했다. ",
0, "정답","2022-12-09T11:49:13","2022-12-09T11:49:13","c.png");

INSERT INTO board (id,uid,name,title,note,views,answer,create_at,update_at,bimg) VALUES (8,1,"user","이게 실화라뇨..",
"문제 내용 : 청소원 A는 “누가 토마토 소스를 이렇게 많이 버린거야.” 생각했다. ",
0, "정답","2022-12-09T11:49:13","2022-12-09T11:49:13","c.png");

INSERT INTO board (id,uid,name,title,note,views,answer,create_at,update_at,bimg) VALUES (9,1,"user","이게 실화라뇨..",
"문제 내용 : 청소원 A는 “누가 토마토 소스를 이렇게 많이 버린거야.” 생각했다. ",
0, "정답","2022-12-09T11:49:13","2022-12-09T11:49:13","c.png");

INSERT INTO board (id,uid,name,title,note,views,answer,create_at,update_at,bimg) VALUES (10,1,"user","이게 실화라뇨..",
"문제 내용 : 청소원 A는 “누가 토마토 소스를 이렇게 많이 버린거야.” 생각했다. ",
0, "정답","2022-12-09T11:49:13","2022-12-09T11:49:13","c.png");

INSERT INTO board (id,uid,name,title,note,views,answer,create_at,update_at,bimg) VALUES (11,1,"user","이게 실화라뇨..",
"문제 내용 : 청소원 A는 “누가 토마토 소스를 이렇게 많이 버린거야.” 생각했다. ",
0, "정답","2022-12-09T11:49:13","2022-12-09T11:49:13","c.png");

INSERT INTO board (id,uid,name,title,note,views,answer,create_at,update_at,bimg) VALUES (12,1,"user","이게 실화라뇨..",
"문제 내용 : 청소원 A는 “누가 토마토 소스를 이렇게 많이 버린거야.” 생각했다. ",
0, "정답","2022-12-09T11:49:13","2022-12-09T11:49:13","c.png");

INSERT INTO board (id,uid,name,title,note,views,answer,create_at,update_at,bimg) VALUES (13,1,"user","이게 실화라뇨..",
"문제 내용 : 청소원 A는 “누가 토마토 소스를 이렇게 많이 버린거야.” 생각했다. ",
0, "정답","2022-12-09T11:49:13","2022-12-09T11:49:13","c.png");

