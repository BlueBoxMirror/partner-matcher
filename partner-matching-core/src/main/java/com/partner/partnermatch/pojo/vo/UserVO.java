package com.partner.partnermatch.pojo.vo;

import com.partner.partnermatch.entity.ai.AIUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class UserVO {
    private List<String> tags;
    private String avatarUri;
    private String profile;
    private int gender;

    public UserVO(AIUser user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.gender = user.getGender();
        this.profile = user.getProfile();
        this.avatarUri = user.getAvatarUri();
        this.tags = user.getTags();
    }
    private Long id;
    private String username;
}
