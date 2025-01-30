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
    private MessageSendService sendService;

    private RequestMessage form;

    @BeforeEach
    void init() {
        form = new RequestMessage();
        form.setSubject("제목");
        form.setContent("내용");
        form.setReceiverEmail("user01@test.org");
    }

    @Test
    @MockMember
    @DisplayName("쪽지 작성 테스트")
    void writeTest() throws Exception{

        form = new RequestMessage();
        form.setReceiverEmail("user02@test.org");
        form.setSubject("제목");
        form.setContent("내용");
        sendService.process(form);

        /*String body = om.writeValueAsString(form);

        String res = mockMvc.perform(post("/write")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andDo(print())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONData jsonData = om.readValue(res, JSONData.class);
        Message data = om.readValue(om.writeValueAsString(jsonData.getData()), Message.class);*/
//        System.out.println("data : " + data);

        /*MessageSearch search = new MessageSearch();
        search.setMode("send");
        ListData<Message> data = infoService.getList(search);
        List<Message> items = data.getItems();*/

    }

}
