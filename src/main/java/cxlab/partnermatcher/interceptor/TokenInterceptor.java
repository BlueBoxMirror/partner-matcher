package cxlab.partnermatcher.interceptor;

import cxlab.partnermatcher.common.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class TokenInterceptor implements HandlerInterceptor {

    // 用于把Result对象转成JSON格式返回给前端
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取前端传的Authorization请求头
        String authHeader = request.getHeader("Authorization");

        // 2. 判断格式是否为"Bearer 空格 + token"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 未登录/Token无效，按前端约定返回code=401，HTTP状态码也设为401
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter writer = response.getWriter();
            writer.write(objectMapper.writeValueAsString(Result.error(401, "未登录或Token已过期")));
            writer.flush();
            writer.close();
            return false; // 拦截请求，不让继续执行
        }

        // 3. 提取真正的token（去掉前面的"Bearer "前缀，共7个字符）
        String token = authHeader.substring(7);

        // 暂时先放行，后续可以在这里加token有效性校验（比如从Redis查token是否存在）
        return true;
    }
}