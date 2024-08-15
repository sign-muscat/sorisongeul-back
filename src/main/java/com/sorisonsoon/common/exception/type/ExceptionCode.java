package com.sorisonsoon.common.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * BaseException 에서 사용할 예외 코드
 * <p>
 * 400 BadRequestException
 * 401 Unauthorized
 * 403 Forbidden
 * 404 NotFoundException
 * 409 ConflictException
 * 500 ServerInternalException
 */
@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    UNAUTHORIZED(401, "인증 되지 않은 요청입니다."),
    INVALID_CREDENTIALS(401, "아이디 또는 비밀번호가 잘못되었습니다."),
    INVALID_ID(401, "아이디가 올바르지 않습니다."),
    INVALID_PASSWORD(401, "비밀번호가 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(401, "리프레시 토큰이 유효하지 않습니다."),
    INVALID_EMAIL(401, "이메일 형식이 올바르지 않습니다."),
    INVALID_GAME_VOICE_ID(401, "게임 문제 번호가 올바르지 않습니다."),
    INVALID_APPLY_TYPE(401, "신청 타입이 올바르지 않습니다."),

    ACCESS_DENIED(403, "허가 되지 않은 요청입니다."),

    NOT_FOUND_USER(404, "회원 정보를 찾을 수 없습니다."),
    NOT_FOUND_CATEGORY(404, "해당 카테고리가 존재하지 않습니다."),
    NOT_FOUND_IMAGE(404, "해당 이미지가 존재하지 않습니다."),
    NOT_FOUND_REPORT(404, "신고 내역이 존재하지 않습니다."),
    NOT_FOUND_POST(404, "게시글이 존재하지 않습니다."),
    NOT_FOUND_COMMENT(404, "댓글이 존재하지 않습니다."),
    NOT_FOUND_FRIEND(404, "해당 친구를 찾을 수 없습니다."),

    ALREADY_EXISTS_ID(409, "이미 존재하는 아이디입니다."),
    ALREADY_EXISTS_EMAIL(409, "이미 존재하는 이메일입니다."),
    ALREADY_EXISTS_NICKNAME(409, "이미 존재하는 닉네임입니다."),
    ALREADY_WITHDRAWN_USER(409, "이미 탈퇴한 회원입니다."),

    FAIL_TO_UPLOAD_FILE(500, "파일 저장에 실패하였습니다."),
    FAIL_TO_DELETE_FILE(500, "파일 삭제에 실패하였습니다."),
    FAIL_TO_EMBEDDING_MODEL(500, "모델 임베딩에 실패하였습니다.");

    private final int code;

    private final String message;
}
