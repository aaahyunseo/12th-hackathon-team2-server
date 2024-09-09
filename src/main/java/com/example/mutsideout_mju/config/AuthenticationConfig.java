package com.example.mutsideout_mju.config;

import com.example.mutsideout_mju.authentication.AuthenticatedUserArgumentResolver;
import com.example.mutsideout_mju.authentication.AuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final AuthenticatedUserArgumentResolver authenticatedUserArgumentResolver;
    private static final String[] ADD_PLANNER_API_PATH = {"/planners", "planners/{plannerId}", "/planners/{plannerId}/complete", "planners/calendars"};
    private static final String[] ADD_DIARY_API_PATH = {"/diaries", "diaries/{diaryId}"};
    private static final String[] ADD_ROOM_API_PATH = {"/rooms", "rooms/{roomId}"};
    private static final String[] ADD_USER_API_PATH = {"/users", "users/grade", "users/mypage"};
    private static final String ADD_SURVEY_API_PATH = "/surveys";
    private static final String ADD_AUTH_API_PATH = "/auth/logout";
    private static final String[] EXCLUDE_AUTH_API_PATH = {"/auth/login", "/auth/signup", "/auth/refresh"};

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns(ADD_PLANNER_API_PATH)
                .addPathPatterns(ADD_DIARY_API_PATH)
                .addPathPatterns(ADD_ROOM_API_PATH)
                .addPathPatterns(ADD_USER_API_PATH)
                .addPathPatterns(ADD_SURVEY_API_PATH)
                .addPathPatterns(ADD_AUTH_API_PATH)
                .excludePathPatterns(EXCLUDE_AUTH_API_PATH);
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserArgumentResolver);
    }
}
