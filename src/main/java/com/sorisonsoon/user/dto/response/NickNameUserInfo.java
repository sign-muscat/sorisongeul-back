package com.sorisonsoon.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NickNameUserInfo {
    private Long userId;
    private String nickname ;
    private String email;
    private String profileImage;

}
