package org.anonymous.member.test.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.rests.JSONData;
import org.anonymous.member.contants.Authority;
import org.anonymous.member.test.annotations.MockMember;
import org.anonymous.message.controllers.RequestMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageAdminControllerTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Utils utils;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    private RequestMessage form;

    private String token;


    @BeforeEach
    void init() throws JsonProcessingException {

        Map<String, String> loginForm = new HashMap<>();

        loginForm.put("email", "user1@test.org");
        loginForm.put("password", "_aA123456");

        restTemplate = new RestTemplate();

        HttpHeaders _headers = new HttpHeaders();

        HttpEntity<Map<String, String>> request = new HttpEntity<>(loginForm, _headers);

        String apiUrl = utils.serviceUrl("member-service", "/login");

        ResponseEntity<JSONData> item = restTemplate.exchange(apiUrl, HttpMethod.POST, request, JSONData.class);

        token = item.getBody().getData().toString();

        // if (StringUtils.hasText(token)) _headers.setBearerAuth(token);

        System.out.println("token : " + token);

    }

    @Test
    @MockMember(authority = Authority.ADMIN)
    @DisplayName("쪽지 어드민 단일 & 목록 삭제")
    void deletesTest() throws Exception{

        mockMvc.perform(delete("/admin/deletes")
                .param("seq", "1")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());

    }

    @Test
    @MockMember(authority = Authority.ADMIN)
    @DisplayName("쪽지 어드민 블락, 언블락 처리")
    void statusTest() throws Exception {

        List<String> emails = List.of("user2@test.org");
        String body = om.writeValueAsString(emails);

        mockMvc.perform(patch("/admin/status")
                .param("status", "true")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andDo(print());
    }

}
