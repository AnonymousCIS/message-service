package org.anonymous.member.test.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.rests.JSONData;
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
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
//@ActiveProfiles({"default"})
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Utils utils;

    private RequestMessage form;

    private String token;


    @BeforeEach
    void init() throws JsonProcessingException {

        Map<String, String> loginForm = new HashMap<>();

        loginForm.put("email", "user01@test.org");
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
    @MockMember
    @DisplayName("쪽지 작성 테스트")
    void writeTest() throws Exception {

        for (int i = 0; i < 5; i++) {
            form = new RequestMessage();
            form.setReceiverEmail("user02@test.org");
            form.setSubject("제목" + i);
            form.setContent("내용내용내용내용" + i);

            String body = om.writeValueAsString(form);

            mockMvc.perform(post("/write")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)).andDo(print());
        }




//        sendService.process(form);

        /*String body = om.writeValueAsString(form);

        String res = mockMvc.perform(post("/write")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andDo(print())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONData jsonData = om.readValue(res, JSONData.class);
        Message data = om.readValue(om.writeValueAsString(jsonData.getData()), Message.class);
//        System.out.println("data : " + data);

        MessageSearch search = new MessageSearch();
        search.setMode("send");
        ListData<Message> data = infoService.getList(search);
        List<Message> items = data.getItems();*/

    }

    @Test
    @MockMember(email = "user02@test.org")
    @DisplayName("쪽지 단일 조회 테스트")
    void viewTest() throws Exception {

        mockMvc.perform(get("/view/1")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());


    }

    @Test
    @MockMember
    @DisplayName("쪽지 목록 조회 테스트")
    void listTest() throws Exception {

        mockMvc.perform(get("/list")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());
    }


    @Test
    @MockMember
    @DisplayName("쪽지 미열람 개수 테스트")
    void countTest() throws Exception {

        mockMvc.perform(get("/count")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());
    }

    @Test
    @MockMember
    @DisplayName("쪽지 단일 & 목록 삭제 테스트")
    void deletesTest() throws Exception {

        mockMvc.perform(patch("/deletes")
                        .param("seq", "2052")
                        .param("mode", "receive")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());
    }
}
