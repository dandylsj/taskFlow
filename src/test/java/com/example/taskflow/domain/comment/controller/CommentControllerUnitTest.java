package com.example.taskflow.domain.comment.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taskflow.common.filter.JwtFilter;
import com.example.taskflow.common.model.enums.TaskPriority;
import com.example.taskflow.common.model.enums.TaskStatus;
import com.example.taskflow.common.model.enums.UserRole;
import com.example.taskflow.domain.comment.model.dto.UserInfoDto;
import com.example.taskflow.domain.comment.model.request.CommentCreateRequest;
import com.example.taskflow.domain.comment.model.response.CommentCreateResponse;
import com.example.taskflow.domain.comment.service.CommentService;
import com.example.taskflow.domain.task.controller.TaskController;
import com.example.taskflow.domain.task.model.request.TaskCreateRequest;
import com.example.taskflow.domain.task.model.response.TaskAssgineeResponse;
import com.example.taskflow.domain.task.model.response.TaskCreateResponse;
import com.example.taskflow.domain.task.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = CommentController.class,
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = JwtFilter.class
        )
    }
)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private TaskService taskService;

    @Test
    @WithMockUser
    @DisplayName("POST /api/tasks/{taskId}/comments 댓글 생성 테스트")
    void createCommentApi_success() throws Exception {

        // given
        long taskId = 1L;

        CommentCreateRequest request = new CommentCreateRequest();
        ReflectionTestUtils.setField(request, "content", "댓글 생성 테스트");
        ReflectionTestUtils.setField(request, "parentId", null);

        CommentCreateResponse expectedResponse = new CommentCreateResponse(
            1L, 1L, 1L,
            new UserInfoDto(1L, "test", "test", "test@test.com", UserRole.USER),
            "댓글 생성 테스트", null,
            LocalDateTime.now(), LocalDateTime.now()
        );

        given(commentService.createComment(anyString(), anyLong(), any(CommentCreateRequest.class))).willReturn(expectedResponse);

        // when & then
        mockMvc.perform(post("/api/tasks/{taskId}/comments", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.id").value(1L))
            .andExpect(jsonPath("$.data.taskId").value(1L))
            .andExpect(jsonPath("$.data.content").value("댓글 생성 테스트"));
    }

    @Test
    @DisplayName("GET /api/tasks/{taskId}/comments 댓글 목록 조회 테스트")
    void getCommentListApi_success() throws Exception {

        // given
        int page = 0;

        // when & then

    }

    @Test
    void updateCommentApi_success() throws Exception {
    }

    @Test
    void deleteCommentApi_success() throws Exception {
    }
}