package org.anonymous.member.test.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.anonymous.global.rests.JSONData;
import org.anonymous.member.test.annotations.MockMember;
import org.anonymous.message.controllers.RequestMessage;
import org.anonymous.message.entities.Message;
import org.anonymous.message.services.MessageInfoService;
import org.anonymous.message.services.MessageSendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles({"default", "test"})
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MessageInfoService infoService;

    @Autowired
    private MessageSendService sendService;

    private Message message;
    private RequestMessage form;

    @BeforeEach
    void init() {
        form = new RequestMessage();
        form.setSubject("제목");
        form.setContent("내용");
        form.setEmail("user01@test.org");
    }

    @Test
    @MockMember
    @DisplayName("쪽지 작성 테스트")
    void writeTest() throws Exception{
        createMessage();

        String body = om.writeValueAsString(form);

        String res = mockMvc.perform(post("/write")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andDo(print())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONData jsonData = om.readValue(res, JSONData.class);
        Message data = om.readValue(om.writeValueAsString(jsonData.getData()), Message.class);
        System.out.println("data : " + data);

        /*MessageSearch search = new MessageSearch();
        search.setMode("send");
        ListData<Message> data = infoService.getList(search);
        List<Message> items = data.getItems();*/

    }

    void createMessage(){
        for (int i = 0; i < 10; i++) {
            form = new RequestMessage();
            form.setEmail("user02@test.org");
            form.setSubject("제목" + i);
            form.setContent("내용" + i);
            sendService.process(form);
        }

    }

}
