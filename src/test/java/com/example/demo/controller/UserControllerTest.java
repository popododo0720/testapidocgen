package com.example.demo.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.example.demo.dto.UserCreateRequest;
import com.example.demo.dto.UserUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void getAllUsers() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/users"))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("users-get-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("User")
                                .description("모든 사용자 목록 조회")
                                .responseSchema(com.epages.restdocs.apispec.Schema.schema("UserList"))
                                .responseFields(
                                        fieldWithPath("[].id").description("사용자 ID"),
                                        fieldWithPath("[].username").description("사용자명"),
                                        fieldWithPath("[].email").description("이메일"),
                                        fieldWithPath("[].age").description("나이"),
                                        fieldWithPath("[].createdAt").description("생성 시각")
                                )
                                .build()
                        )
                ));
    }

    @Test
    void getUserById() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/users/{id}", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("users-get-by-id",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("User")
                                .description("특정 ID의 사용자 조회")
                                .responseSchema(com.epages.restdocs.apispec.Schema.schema("UserResponse"))
                                .pathParameters(
                                        com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName("id").description("사용자 ID")
                                )
                                .responseFields(
                                        fieldWithPath("id").description("사용자 ID"),
                                        fieldWithPath("username").description("사용자명"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("age").description("나이"),
                                        fieldWithPath("createdAt").description("생성 시각")
                                )
                                .build()
                        )
                ));
    }

    @Test
    void searchUsers() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/users/search")
                        .param("username", "alice"))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("users-search",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("User")
                                .description("사용자명으로 검색 (부분 일치)")
                                .responseSchema(com.epages.restdocs.apispec.Schema.schema("UserList"))
                                .queryParameters(
                                        com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName("username").description("검색할 사용자명")
                                )
                                .responseFields(
                                        fieldWithPath("[].id").description("사용자 ID"),
                                        fieldWithPath("[].username").description("사용자명"),
                                        fieldWithPath("[].email").description("이메일"),
                                        fieldWithPath("[].age").description("나이"),
                                        fieldWithPath("[].createdAt").description("생성 시각")
                                )
                                .build()
                        )
                ));
    }

    @Test
    void filterUsersByAge() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/users/filter")
                        .param("minAge", "30"))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("users-filter",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("User")
                                .description("최소 나이로 사용자 필터링")
                                .responseSchema(com.epages.restdocs.apispec.Schema.schema("UserList"))
                                .queryParameters(
                                        com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName("minAge").description("최소 나이")
                                )
                                .responseFields(
                                        fieldWithPath("[].id").description("사용자 ID"),
                                        fieldWithPath("[].username").description("사용자명"),
                                        fieldWithPath("[].email").description("이메일"),
                                        fieldWithPath("[].age").description("나이"),
                                        fieldWithPath("[].createdAt").description("생성 시각")
                                )
                                .build()
                        )
                ));
    }

    @Test
    void createUser() throws Exception {
        UserCreateRequest request = new UserCreateRequest("david", "david@example.com", 28);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(MockMvcRestDocumentationWrapper.document("users-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("User")
                                .description("새 사용자 생성")
                                .requestSchema(com.epages.restdocs.apispec.Schema.schema("UserCreateRequest"))
                                .responseSchema(com.epages.restdocs.apispec.Schema.schema("UserResponse"))
                                .requestFields(
                                        fieldWithPath("username").description("사용자명"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("age").description("나이")
                                )
                                .responseFields(
                                        fieldWithPath("id").description("생성된 사용자 ID"),
                                        fieldWithPath("username").description("사용자명"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("age").description("나이"),
                                        fieldWithPath("createdAt").description("생성 시각")
                                )
                                .build()
                        )
                ));
    }

    @Test
    void updateUser() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest("alice.updated@example.com", 26);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("users-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("User")
                                .description("사용자 정보 전체 수정")
                                .requestSchema(com.epages.restdocs.apispec.Schema.schema("UserUpdateRequest"))
                                .responseSchema(com.epages.restdocs.apispec.Schema.schema("UserResponse"))
                                .pathParameters(
                                        com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName("id").description("사용자 ID")
                                )
                                .requestFields(
                                        fieldWithPath("email").description("새 이메일"),
                                        fieldWithPath("age").description("새 나이")
                                )
                                .responseFields(
                                        fieldWithPath("id").description("사용자 ID"),
                                        fieldWithPath("username").description("사용자명"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("age").description("나이"),
                                        fieldWithPath("createdAt").description("생성 시각")
                                )
                                .build()
                        )
                ));
    }

    @Test
    void partialUpdateUser() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest("bob.new@example.com", 31);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/users/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("users-partial-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("User")
                                .description("사용자 정보 부분 수정")
                                .requestSchema(com.epages.restdocs.apispec.Schema.schema("UserUpdateRequest"))
                                .responseSchema(com.epages.restdocs.apispec.Schema.schema("UserResponse"))
                                .pathParameters(
                                        com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName("id").description("사용자 ID")
                                )
                                .requestFields(
                                        fieldWithPath("email").description("새 이메일"),
                                        fieldWithPath("age").description("새 나이")
                                )
                                .responseFields(
                                        fieldWithPath("id").description("사용자 ID"),
                                        fieldWithPath("username").description("사용자명"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("age").description("나이"),
                                        fieldWithPath("createdAt").description("생성 시각")
                                )
                                .build()
                        )
                ));
    }

    @Test
    void deleteUser() throws Exception {
        // 먼저 사용자 생성
        UserCreateRequest createRequest = new UserCreateRequest("todelete", "delete@example.com", 25);
        String response = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = objectMapper.readTree(response).get("id").asLong();

        // 삭제
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent())
                .andDo(MockMvcRestDocumentationWrapper.document("users-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("User")
                                .description("사용자 삭제")
                                .pathParameters(
                                        com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName("id").description("삭제할 사용자 ID")
                                )
                                .build()
                        )
                ));
    }
}