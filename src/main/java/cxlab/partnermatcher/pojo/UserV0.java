package cxlab.partnermatcher.pojo;

import lombok.Data;
import java.util.List;

@Data
public class UserV0 {
    private Long id;
    private String username;
    private String avatarUri;
    private String profile;
    private List<String> tags;
}
