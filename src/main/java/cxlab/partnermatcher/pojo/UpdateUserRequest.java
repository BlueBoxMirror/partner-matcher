package cxlab.partnermatcher.pojo;

import lombok.Data;
import java.util.List;

@Data
public class UpdateUserRequest {
    private String username;
    private String profile;
    private List<String> tags;
}
