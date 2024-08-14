#24.07.25 현재 코드 테스트 완료 / 그러나 수정꼭 해야함 ㅠㅠㅠ
# ===========================================================
DROP DATABASE `sorisonsoon`;
CREATE DATABASE IF NOT EXISTS `sorisonsoon`;

# ===========================================================
# GRANT ALL PRIVILEGES ON `sorisonsoon`.* TO `admin`@`%`;

# ===========================================================
USE sorisonsoon;

-- Users 테이블
CREATE TABLE `users` (
                                `user_id`           INT AUTO_INCREMENT                  NOT NULL COMMENT '회원 번호',
                                `id`                VARCHAR(255)                        NOT NULL COMMENT '아이디',
                                `password`          VARCHAR(255) DEFAULT NULL                    COMMENT '비밀 번호',
                                `nickname`          VARCHAR(255)                        NOT NULL COMMENT '닉네임',
                                `email`             VARCHAR(255)                        NOT NULL COMMENT '이메일',
                                `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '가입 일시',
                                `deleted_at`        DATETIME                                     COMMENT '탈퇴 일시',
                                `role`              VARCHAR(15)                         NOT NULL COMMENT '권한',
                                `type`              VARCHAR(15)                         NOT NULL COMMENT '종류',
                                `status`            VARCHAR(15) DEFAULT 'ACTIVATE'      NOT NULL COMMENT '상태',
                                `access_token`      VARCHAR(300) DEFAULT NULL                    COMMENT '엑세스 토큰',
                                `refresh_token`     VARCHAR(300)                        NOT NULL COMMENT '리프레시 토큰',
                                `provider`          VARCHAR(15)                         NOT NULL COMMENT '쇼셜 제공자',
                                `profile_image`     VARCHAR(255)                                 COMMENT '프로필 이미지',
                                #PK
                                    PRIMARY KEY (`user_id`),
                                #CK
                                    CONSTRAINT `users_CK1` CHECK (`role` IN ('ADMIN', 'FREE_USER', 'PREMIUM_USER')),
                                    CONSTRAINT `users_CK2` CHECK (`type` IN ('PERSONAL', 'ENTERPRISE')),
                                    CONSTRAINT `users_CK3` CHECK (`status` IN ('ACTIVATE', 'WITHDRAW')),
                                    CONSTRAINT `users_CK4` CHECK (`provider` IN ('GOOGLE', 'NONE'))
) COMMENT = '회원';

-- Sign_Search 테이블
CREATE TABLE `sign_search` (
                                `search_id`         INT AUTO_INCREMENT                  NOT NULL COMMENT '검색 번호',
                                `searcher_id`       INT                                 NOT NULL COMMENT '검색자',
                                `keyword`           VARCHAR(30)                         NOT NULL COMMENT '검색어',
                                `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '검색 일시',
                                `category`          VARCHAR(15)                         NOT NULL COMMENT '분류',
                                #PK
                                    PRIMARY KEY (`search_id`),
                                #FK
                                    FOREIGN KEY (`searcher_id`) REFERENCES `users` (`user_id`),
                                #CK
                                    CONSTRAINT `sign_search_CK1` CHECK (`category` IN ('DAILY_LIFE', 'EMOTIONS', 'OCCUPATIONS', 'PLACES', 'EMERGENCY_SITUATIONS', 'EDUCATION'))
) COMMENT = '검색 기록';

-- Friend 테이블
CREATE TABLE `friend` (
                                `friend_id`         INT AUTO_INCREMENT                  NOT NULL COMMENT '친구 번호',
                                `to_user`           INT                                 NOT NULL COMMENT '신청 받은 사람',
                                `from_user`         INT                                 NOT NULL COMMENT '신청한 사람',
                                `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '요청 일시',
                                `status`            VARCHAR(15) DEFAULT 'APPLIED'       NOT NULL COMMENT '상태',
                                #PK
                                    PRIMARY KEY (`friend_id`),
                                #FK
                                    FOREIGN KEY (`to_user`) REFERENCES `users` (`user_id`),
                                    FOREIGN KEY (`from_user`) REFERENCES `users` (`user_id`),
                                #CK
                                    CONSTRAINT `friend_CK1` CHECK (`status` IN ('APPLIED', 'ACCEPTED', 'REJECTED', 'DELETED'))
) COMMENT = '친구';

-- Notice 테이블
CREATE TABLE `notice` (
                                `notice_id`         INT AUTO_INCREMENT                  NOT NULL COMMENT '공지 사항 번호',
                                `user_id`           INT                                 NOT NULL COMMENT '작성자',
                                `notice_title`      VARCHAR(200)                        NOT NULL COMMENT '게시글 제목',
                                `notice_content`    TEXT                                NOT NULL COMMENT '게시글 내용',
                                `is_fixed`          BOOLEAN DEFAULT 0                   NOT NULL COMMENT '고정 여부',
                                `views`             INT                                 NOT NULL COMMENT '조회수',
                                `status`            VARCHAR(15) DEFAULT 'ACTIVATE'      NOT NULL COMMENT '상태',
                                `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '작성 일시',
                                `category`          VARCHAR(15)                         NOT NULL COMMENT '분류',
                                #PK
                                    PRIMARY KEY (`notice_id`),
                                #FK
                                    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
                                #CK
                                    CONSTRAINT `notice_CK1` CHECK (`status` IN ('ACTIVATE', 'BLOCKED', 'DELETED')),
                                    CONSTRAINT `notice_CK2` CHECK (`category` IN ('EVENT', 'GENERAL', 'URGENT'))
) COMMENT = '공지 사항';

-- UserInfo 테이블
CREATE TABLE `user_info` (
                                `info_id`           INT AUTO_INCREMENT                  NOT NULL COMMENT '회원 정보 번호',
                                `level`             INT                                 NOT NULL COMMENT '레벨',
                                `experience`        INT                                 NOT NULL COMMENT '경험치',
                                `user_id`           INT                                 NOT NULL COMMENT '유저 ID',
                                #PK
                                    PRIMARY KEY (`info_id`),
                                #FK
                                    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) COMMENT = '회원 정보';

-- Interests 테이블
CREATE TABLE `interests` (
                                `interest_id`       INT AUTO_INCREMENT                  NOT NULL COMMENT '관심사 번호',
                                `user_id`           INT                                 NOT NULL COMMENT '회원 번호',
                                `keyword`           VARCHAR(50)                         NOT NULL COMMENT '관심 키워드',
                                #PK
                                    PRIMARY KEY (`interest_id`),
                                #FK
                                    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) COMMENT = '관심사';

-- Ranking 테이블
CREATE TABLE `ranking` (
                                `ranking_id`        INT AUTO_INCREMENT                  NOT NULL COMMENT '랭킹 번호',
                                `user_id`           INT                                 NOT NULL COMMENT '회원 번호',
                                `category`          VARCHAR(15)                         NOT NULL COMMENT '분류',
                                `score`             INT                                 NOT NULL COMMENT '점수',
                                `created_at`        DATETIME                            NOT NULL COMMENT '플레이 시각',
                                #PK
                                    PRIMARY KEY (`ranking_id`),
                                #FK
                                    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
                                #CK
                                    CONSTRAINT `ranking_CK1` CHECK (`category` IN ('CHALLENGE', 'RIDDLE', 'VOICE'))
) COMMENT = '랭킹';

-- Game_voice 테이블
CREATE TABLE `game_voice` (
                                `voice_id`          INT AUTO_INCREMENT                  NOT NULL COMMENT '너목보 번호',
                                `answer`            VARCHAR(255)                        NOT NULL COMMENT '정답',
                                `question`          VARCHAR(255)                                 COMMENT '음성 문제',
                                `category`          VARCHAR(15)                         NOT NULL COMMENT '분류',
                                `difficulty`        VARCHAR(15)                         NOT NULL COMMENT '난이도',
                                #PK
                                    PRIMARY KEY (`voice_id`),
                                #CK
                                    CONSTRAINT `game_voice_CK1` CHECK (`category` IN ('DRAMAS', 'MOVIES', 'COMEDIES', 'CARTOONS', 'BOOKS', 'MEMES')),
                                    CONSTRAINT `game_voice_CK2` CHECK (`difficulty` IN ('LEVEL_1', 'LEVEL_2', 'LEVEL_3'))
) COMMENT = '너의 목소리가 보여';

-- Game_riddle 테이블
CREATE TABLE `game_riddle` (
                                `riddle_id`         INT AUTO_INCREMENT                  NOT NULL COMMENT '맞수수 번호',
                                `question`          VARCHAR(255)                        NOT NULL COMMENT '문제',
                                `video`             VARCHAR(255)                        NOT NULL COMMENT '영상',
                                `category`          VARCHAR(15)                         NOT NULL COMMENT '분류',
                                `difficulty`        VARCHAR(15)                         NOT NULL COMMENT '난이도',
                                #PK
                                    PRIMARY KEY (`riddle_id`),
                                #CK
                                    CONSTRAINT `game_riddle_CK1` CHECK (`category` IN ('DAILY_LIFE', 'EMOTION', 'ANIMALS_PLANTS', 'JOB', 'FOOD_CLOTHING_PLACE', 'ETC')),
                                    CONSTRAINT `game_riddle_CK2` CHECK (`difficulty` IN ('LEVEL_1', 'LEVEL_2', 'LEVEL_3'))
) COMMENT = '맞춰라 수수께끼';

-- game_riddle_step 테이블
create table game_riddle_step
(
    riddle_step_id  int auto_increment comment '맞수수 동작 번호',
    riddle_id   int not null comment '맞수수 단어 번호',
    step        int not null comment '동작 순서',
    answer      varchar(255) not null comment '동작 가이드',
    #PK
    PRIMARY KEY (`riddle_step_id`),
    #FK
    FOREIGN KEY (`riddle_id`) REFERENCES `game_riddle` (`riddle_id`)
)
    comment '맞춰라 수수께끼 동작';

-- Game_challenge 테이블
CREATE TABLE `game_challenge` (
                                `challenge_id`      INT AUTO_INCREMENT                  NOT NULL COMMENT '도소탐 번호',
                                `question`          VARCHAR(255)                        NOT NULL COMMENT '문제',
                                `category`          VARCHAR(15)                         NOT NULL COMMENT '분류',
                                `difficulty`        VARCHAR(15)                         NOT NULL COMMENT '난이도',
                                #PK
                                    PRIMARY KEY (`challenge_id`),
                                #CK
                                    CONSTRAINT `game_challenge_CK1` CHECK (`category` IN ('DRAMAS', 'MOVIES', 'COMEDIES', 'CARTOONS', 'BOOKS', 'MEMES')),
                                    CONSTRAINT `game_challenge_CK2` CHECK (`difficulty` IN ('LEVEL_1', 'LEVEL_2', 'LEVEL_3'))
) COMMENT = '도전 소리 탐정';

CREATE TABLE `game_challenge_schedule` (
                                  `challenge_schedule_id`   INT AUTO_INCREMENT              NOT NULL COMMENT '출제 기록 번호',
                                  `challenge_id`            INT                             NOT NULL COMMENT '도소탐 번호',
                                  `schedule`                DATE DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '출제 날짜',
                                  #PK
                                  PRIMARY KEY (`challenge_schedule_id`),
                                  #FK
                                  FOREIGN KEY (`challenge_id`) REFERENCES `game_challenge` (`challenge_id`)
) COMMENT = '도전 소리 탐정 출제 기록';

-- Game_whisper 테이블
CREATE TABLE `game_whisper` (
                                `whisper_id`        INT AUTO_INCREMENT                  NOT NULL COMMENT '고요침 번호',
                                `question`          VARCHAR(255)                        NOT NULL COMMENT '문제',
                                `category`          VARCHAR(15)                         NOT NULL COMMENT '분류',
                                #PK
                                    PRIMARY KEY (`whisper_id`),
                                #CK
                                    CONSTRAINT `game_whisper_CK1` CHECK (`category` IN ('PEOPLE', 'MOVIES', 'FOODS', 'STUFFS'))
) COMMENT = '고요 속의 외침';

-- Payment 테이블
CREATE TABLE `payment` (
                                `payment_id`        INT AUTO_INCREMENT                  NOT NULL COMMENT '결제 번호',
                                `payer_id`          INT                                 NOT NULL COMMENT '결재자',
                                `amount`            INT                                 NOT NULL COMMENT '결제 금액',
                                `payed_at`          DATETIME DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '결제 일시',
                                #PK
                                   PRIMARY KEY (`payment_id`),
                                #FK
                                   FOREIGN KEY (`payer_id`) REFERENCES `users` (`user_id`)
) COMMENT = '결제 내역';

-- Membership 테이블
CREATE TABLE `membership` (
                                `membership_id`     INT AUTO_INCREMENT                  NOT NULL COMMENT '멤버십 번호',
                                `user_id`           INT                                 NOT NULL COMMENT '멤버십 가입자',
                                `payment_id`        INT                                 NOT NULL COMMENT '결제 번호',
                                `startDate`         DATE                                NOT NULL COMMENT '시작 일자',
                                `endDate`           DATE                                NOT NULL COMMENT '종료 일자',
                                `is_activate`       BOOLEAN DEFAULT 1                   NOT NULL COMMENT '활성화 여부',
                                #PK
                                    PRIMARY KEY (`membership_id`),
                                #FK
                                    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
                                    FOREIGN KEY (`payment_id`) REFERENCES `payment` (`payment_id`)
) COMMENT = '유료 멤버십';

-- Quest 테이블
CREATE TABLE `quest` (
                                `quest_id`          INT AUTO_INCREMENT                  NOT NULL COMMENT '퀘스트 번호',
                                `quest_name`        VARCHAR(255)                        NOT NULL COMMENT '퀘스트 명',
                                `quest_desc`        VARCHAR(255)                        NOT NULL COMMENT '퀘스트 설명',
                                `medal_image`       TEXT                                NOT NULL COMMENT '메달 이미지',
                                #PK
                                    PRIMARY KEY (`quest_id`)
) COMMENT = '퀘스트';

-- Quest_achievement 테이블
CREATE TABLE `quest_achievement` (
                                `achievement_id`    INT AUTO_INCREMENT                  NOT NULL COMMENT '퀘스트 달성 번호',
                                `participant_id`    INT                                 NOT NULL COMMENT '참가자',
                                `quest_id`          INT                                 NOT NULL COMMENT '퀘스트 번호',
                                `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '달성 시각',
                                #PK
                                    PRIMARY KEY (`achievement_id`),
                                #FK
                                    FOREIGN KEY (`participant_id`) REFERENCES `users` (`user_id`),
                                    FOREIGN KEY (`quest_id`) REFERENCES `quest` (`quest_id`)
) COMMENT = '퀘스트 달성';

-- Community 테이블
CREATE TABLE `community` (
                                `community_id`      INT AUTO_INCREMENT                  NOT NULL COMMENT '커뮤니티 번호',
                                `user_id`           INT                                 NOT NULL COMMENT '작성자',
                                `community_title`   VARCHAR(200)                        NOT NULL COMMENT '게시글 제목',
                                `community_content` TEXT                                NOT NULL COMMENT '게시글 내용',
                                `views`             INT                                 NOT NULL COMMENT '조회수',
                                `status`            VARCHAR(15) DEFAULT 'ACTIVATE'      NOT NULL COMMENT '상태',
                                `is_deleted`        BOOLEAN DEFAULT 0                   NOT NULL COMMENT '삭제 여부',
                                `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '작성 일시',
                                `modified_at`       DATETIME                                     COMMENT '수정 일시',
                                `deleted_at`        DATETIME                                     COMMENT '삭제 일시',
                                #PK
                                    PRIMARY KEY (`community_id`),
                                #FK
                                    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
                                #CK
                                    CONSTRAINT `community_CK1` CHECK (`status` IN ('ACTIVATE', 'BLOCKED', 'DELETED'))
) COMMENT = '커뮤니티 게시판';

-- Comment 테이블
CREATE TABLE `comment` (
                                `comment_id`        INT AUTO_INCREMENT                  NOT NULL COMMENT '댓글 번호',
                                `writer_id`         INT                                 NOT NULL COMMENT '작성자',
                                `community_id`      INT                                 NOT NULL COMMENT '커뮤니티 글 번호',
                                `parent_comment_id` INT                                          COMMENT '부모 댓글 여부',
                                `category`          VARCHAR(15)                         NOT NULL COMMENT '분류',
                                `comment_content`   TEXT                                NOT NULL COMMENT '댓글 내용',
                                `status`            VARCHAR(15) DEFAULT 'ACTIVATE'      NOT NULL COMMENT '상태',
                                `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '작성 일시',
                                `modified_at`       DATETIME                                     COMMENT '수정 일시',
                                `deleted_at`        DATETIME                                     COMMENT '삭제 일시',
                                #PK
                                    PRIMARY KEY (`comment_id`),
                                #FK
                                    FOREIGN KEY (`writer_id`) REFERENCES `users` (`user_id`),
                                    FOREIGN KEY (`community_id`) REFERENCES `community` (`community_id`),
                                    FOREIGN KEY (`parent_comment_id`) REFERENCES `comment` (`comment_id`),
                                #CK
                                    CONSTRAINT `comment_CK2` CHECK (`category` IN ('COMMUNITY')),
                                    CONSTRAINT `comment_CK3` CHECK (`status` IN ('ACTIVATE', 'BLOCKED', 'DELETED'))
) COMMENT = '댓글';

-- Guestbook 테이블
CREATE TABLE `guestbook` (
                                `guestbook_id`               INT AUTO_INCREMENT                  NOT NULL COMMENT '박명록 번호',
                                `sender_id`         INT                                 NOT NULL COMMENT '발신자',
                                `receiver_id`       INT                                 NOT NULL COMMENT '수신자',
                                `content`           VARCHAR(255)                        NOT NULL COMMENT '방명록 내용',
                                `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '작성 일시',
                                `modified_at`       DATETIME                                     COMMENT '수정 일시',
                                `status`            VARCHAR(15) DEFAULT 'ACTIVATE'      NOT NULL COMMENT '상태',
                                #PK
                                    PRIMARY KEY (`guestbook_id`),
                                #FK
                                    FOREIGN KEY (`sender_id`) REFERENCES `users` (`user_id`),
                                    FOREIGN KEY (`receiver_id`) REFERENCES `users` (`user_id`),
                                #CK
                                    CONSTRAINT `guestbook_CK1` CHECK (`status` IN ('ACTIVATE', 'BLOCKED', 'DELETED'))
) COMMENT = '방명록';

-- Report 테이블
CREATE TABLE `report` (
                                `report_id`         INT AUTO_INCREMENT                  NOT NULL COMMENT '신고 번호',
                                `reporter_id`       INT                                 NOT NULL COMMENT '신고자',
                                `post_id`           INT                                 NOT NULL COMMENT '신고 당한 글',
                                `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '신고 일시',
                                `category`          VARCHAR(15)                         NOT NULL COMMENT '분류',
                                #PK
                                    PRIMARY KEY (`report_id`),
                                #FK
                                    FOREIGN KEY (`reporter_id`) REFERENCES `users` (`user_id`),
                                    FOREIGN KEY (`post_id`) REFERENCES `community` (`community_id`),
                                #CK
                                    CONSTRAINT `report_CK1` CHECK (`category` IN ('COMMUNITY'))
) COMMENT = '신고';

-- Record 테이블
CREATE TABLE `record` (
                                `record_id`         INT AUTO_INCREMENT                  NOT NULL COMMENT '기록 번호',
                                `player_id`         INT                                 NOT NULL COMMENT '참가자',
                                `challenge_id`      INT                                          COMMENT '도소탐 문제',
                                `voice_id`          INT                                          COMMENT '너목보 문제',
                                `category`          VARCHAR(15)                         NOT NULL COMMENT '분류',
                                `is_correct`        BOOLEAN                             NOT NULL COMMENT '정답 여부',
                                `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '플레이 일시',
                                `similarity`        DOUBLE                              NOT NULL COMMENT '유사도',
                                #PK
                                    PRIMARY KEY (`record_id`),
                                #FK
                                    FOREIGN KEY (`player_id`) REFERENCES `users` (`user_id`),
                                    FOREIGN KEY (`challenge_id`) REFERENCES `game_challenge` (`challenge_id`),
                                    FOREIGN KEY (`voice_id`) REFERENCES `game_voice` (`voice_id`),
                                #CK
                                    CONSTRAINT `record_CK1` CHECK (`category` IN ('CHALLENGE', 'RIDDLE', 'VOICE'))
) COMMENT = '플레이 기록';

-- RecordRiddle 테이블
CREATE TABLE `record_riddle` (
                          `record_id`         INT AUTO_INCREMENT                  NOT NULL COMMENT '기록 번호',
                          `player_id`         INT                                 NOT NULL COMMENT '참가자',
                          `riddle_id`         INT                                          COMMENT '문제 번호',
                          `category`          VARCHAR(15)                         NOT NULL COMMENT '분류',
                          `is_correct`        BOOLEAN                             NOT NULL COMMENT '정답 여부',
                          `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '플레이 일시',
    #PK
                          PRIMARY KEY (`record_id`),
    #FK
                          FOREIGN KEY (`player_id`) REFERENCES `users` (`user_id`),
                          FOREIGN KEY (`riddle_id`) REFERENCES `game_riddle` (`riddle_id`)
) COMMENT = '맞수수 플레이 기록';

-- Setting 테이블
CREATE TABLE `setting` (
                                `setting_id`        INT AUTO_INCREMENT                  NOT NULL COMMENT '설정 번호',
                                `setter_id`         INT                                 NOT NULL COMMENT '설정자',
                                `is_rank_open`      BOOLEAN DEFAULT 1                   NOT NULL COMMENT '랭킹 공개 여부',
                                `is_guestbook_open` BOOLEAN DEFAULT 1                   NOT NULL COMMENT '방명록 공개 여부',
                                `is_medal_open`     BOOLEAN DEFAULT 1                   NOT NULL COMMENT '메달 공개 여부',
                                #PK
                                    PRIMARY KEY (`setting_id`),
                                #FK
                                    FOREIGN KEY (`setter_id`) REFERENCES `users` (`user_id`)
) COMMENT = '설정';

-- Room 테이블
CREATE TABLE `room` (
                                `room_id`           INT AUTO_INCREMENT                  NOT NULL COMMENT '방번호',
                                `constructor_id`    INT                                 NOT NULL COMMENT '생성자',
                                `participant_id`    INT                                          COMMENT '참여자',
                                `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '생성 일시',
                                `deleted_at`        DATETIME                                     COMMENT '종료 일시',
                                #PK
                                    PRIMARY KEY (`room_id`)
) COMMENT = '게임방';

-- Attached_file 테이블
CREATE TABLE `attached_file` (
                                `attached_id`       INT AUTO_INCREMENT                  NOT NULL COMMENT '첨부 파일 번호',
                                `notice_post_ id`   INT                                          COMMENT '공지 사항 글',
                                `community_post_id` INT                                          COMMENT '커뮤니티 글',
                                `report_id`         INT                                          COMMENT '신고 번호',
                                `category`          VARCHAR(15)                         NOT NULL COMMENT '분류',
                                `file_name`         VARCHAR(255)                        NOT NULL COMMENT '파일명',
                                `origin_name`       VARCHAR(255)                        NOT NULL COMMENT '원본명',
                                `path`              VARCHAR(255)                        NOT NULL COMMENT '경로',
                                `extension`         VARCHAR(10)                         NOT NULL COMMENT '확장자',
                                `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '생성 일시',
                                #PK
                                    PRIMARY KEY (`attached_id`),
                                #FK
                                    FOREIGN KEY (`notice_post_ id`) REFERENCES `notice` (`notice_id`),
                                    FOREIGN KEY (`community_post_id`) REFERENCES `community` (`community_id`),
                                    FOREIGN KEY (`report_id`) REFERENCES `report` (`report_id`),
                                #CK
                                    CONSTRAINT `attached_file_CK1` CHECK (`category` IN ('NOTICE', 'COMMUNITY', 'REPORT'))
) COMMENT = '첨부 파일';

-- Team_record 테이블
CREATE TABLE `team_record` (
                                `team_record_id`    INT AUTO_INCREMENT                  NOT NULL COMMENT '팀 기록 번호',
                                `room_id`           INT                                 NOT NULL COMMENT '입장방',
                                `whisper_id`        INT                                 NOT NULL COMMENT '고요침 문제',
                                `questioner`        INT                                 NOT NULL COMMENT '출제자',
                                `respondent`        INT                                 NOT NULL COMMENT '응시자',
                                `is_correct`        BOOLEAN                             NOT NULL COMMENT '정답 여부',
                                `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '기록 일시',
                                `play_video`        VARCHAR(255)                                 COMMENT '영상',
                                #PK
                                    PRIMARY KEY (`team_record_id`),
                                #FK
                                    FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`),
                                    FOREIGN KEY (`whisper_id`) REFERENCES `game_whisper` (`whisper_id`),
                                    FOREIGN KEY (`questioner`) REFERENCES `users` (`user_id`),
                                    FOREIGN KEY (`respondent`) REFERENCES `users` (`user_id`)
) COMMENT = '팀 플레이 기록';
