package org.anonymous.member.test.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.rests.JSONData;
import org.anonymous.member.test.annotations.MockMember;
import org.anonymous.message.constants.MessageStatus;
import org.anonymous.message.controllers.MessageSearch;
import org.anonymous.message.controllers.RequestMessage;
import org.anonymous.message.entities.Message;
import org.anonymous.message.services.MessageInfoService;
import org.anonymous.message.services.MessageSendService;
import org.anonymous.message.services.MessageStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
//@ActiveProfiles({"default"})
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MessageSendService sendService;

    @Autowired
    private MessageInfoService infoService;

    @Autowired
    private MessageStatusService statusService;

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

        form = new RequestMessage();
        form.setSeq(1L);
        form.setReceiverEmail("user02@test.org");
        form.setSubject("제목");
        form.setContent("내용");
    }

    @Test
    @MockMember
    @DisplayName("쪽지 작성 테스트")
    void writeTest() throws Exception{

        String body = om.writeValueAsString(form);

        mockMvc.perform(post("/write")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andDo(print());


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
    @MockMember
    @DisplayName("쪽지 단일 조회 테스트")
    void viewTest() throws Exception{

        MessageSearch search = new MessageSearch();
        search.setMode("send");
        ListData<Message> data = infoService.getList(search);
        List<Message> item = data.getItems();


    }

}
