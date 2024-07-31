
-- User : 회원 개발용 dummy DML
INSERT INTO `users` (`id`, `password`, `nickname`, `email`, `created_at`, `deleted_at`, `role`, `type`, `status`, `access_token`, `refresh_token`, `provider`, `profile_image`)
VALUES
    ('user1', 'password1', 'nickname1', 'user1@example.com', NOW(), NULL, 'FREE_USER', 'PERSONAL', 'ACTIVATE', 'access_token1', 'refresh_token1', 'GOOGLE', '/images/profile1.jpg'),
    ('user2', 'password2', 'nickname2', 'user2@example.com', NOW(), NULL, 'PREMIUM_USER', 'PERSONAL', 'ACTIVATE', 'access_token2', 'refresh_token2', 'NONE', '/images/profile2.jpg'),
    ('user3', 'password3', 'nickname3', 'user3@example.com', NOW(), NULL, 'ADMIN', 'ENTERPRISE', 'ACTIVATE', 'access_token3', 'refresh_token3', 'GOOGLE', '/images/profile3.jpg'),
    ('user4', 'password4', 'nickname4', 'user4@example.com', NOW(), NULL, 'FREE_USER', 'PERSONAL', 'WITHDRAW', 'access_token4', 'refresh_token4', 'NONE', '/images/profile4.jpg'),
    ('user5', 'password5', 'nickname5', 'user5@example.com', NOW(), NULL, 'PREMIUM_USER', 'ENTERPRISE', 'ACTIVATE', 'access_token5', 'refresh_token5', 'GOOGLE', '/images/profile5.jpg'),
    ('user6', 'password6', 'nickname6', 'user6@example.com', NOW(), NULL, 'ADMIN', 'PERSONAL', 'ACTIVATE', 'access_token6', 'refresh_token6', 'NONE', '/images/profile6.jpg'),
    ('user7', 'password7', 'nickname7', 'user7@example.com', NOW(), NULL, 'FREE_USER', 'ENTERPRISE', 'WITHDRAW', 'access_token7', 'refresh_token7', 'GOOGLE', '/images/profile7.jpg'),
    ('user8', 'password8', 'nickname8', 'user8@example.com', NOW(), NULL, 'PREMIUM_USER', 'PERSONAL', 'ACTIVATE', 'access_token8', 'refresh_token8', 'NONE', '/images/profile8.jpg'),
    ('user9', 'password9', 'nickname9', 'user9@example.com', NOW(), NULL, 'ADMIN', 'ENTERPRISE', 'ACTIVATE', 'access_token9', 'refresh_token9', 'GOOGLE', '/images/profile9.jpg'),
    ('user10', 'password10', 'nickname10', 'user10@example.com', NOW(), NULL, 'FREE_USER', 'PERSONAL', 'WITHDRAW', 'access_token10', 'refresh_token10', 'NONE', '/images/profile10.jpg');


-- GameVoice : 너의 목소리가 보여 개발용 dummy DML
INSERT INTO sorisonsoon.game_voice (voice_id, answer, question, category, difficulty)
VALUES
    (1, '안녕히 계세요 여러분', '/question_voice/cartoons/question_1.mp4', 'CARTOONS', 'LEVEL_1'),
    (2, '왜 이러는 걸까요', '/question_voice/comedies/question_1.mp4', 'COMEDIES', 'LEVEL_1'),
    (3, '쓰레기들아 아아아악', '/question_voice/dramas/question_1.mp4', 'DRAMAS', 'LEVEL_1'),
    (4, '아 안돼', '/question_voice/memes/question_1.mp4', 'MEMES', 'LEVEL_1'),
    (5, '오 그러셨어요', '/question_voice/memes/question_2.mp4', 'MEMES', 'LEVEL_2'),
    (6, '오케이 거기까지', '/question_voice/memes/question_3.mp4', 'MEMES', 'LEVEL_3'),
    (7, '오 씨 아메리칸스타일', '/question_voice/movies/question_1.mp4', 'MOVIES', 'LEVEL_1'),
    (8, '에이 이런 좆같은', '/question_voice/movies/question_2.mp4', 'MOVIES', 'LEVEL_1'),
    (9, '어이가 없네', '/question_voice/movies/question_3.mp4', 'MOVIES', 'LEVEL_2'),
    (10, '워뗘 후달려', '/question_voice/movies/question_4.mp4', 'MOVIES', 'LEVEL_2'),
    (11, '야 이 개새끼야', '/question_voice/movies/question_5.mp4', 'MOVIES', 'LEVEL_3'),
    (12, '이 아저씨가 어따 약을 팔아', '/question_voice/movies/question_6.mp4', 'MOVIES', 'LEVEL_3');
