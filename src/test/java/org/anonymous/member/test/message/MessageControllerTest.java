package org.anonymous.member.test.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.anonymous.global.paging.ListData;
import org.anonymous.member.test.annotations.MockMember;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@SpringBootTest
@ActiveProfiles({"default"})
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

    private RequestMessage form;

    private Message message;

    @BeforeEach
    void init() {
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



//        sendService.process(form);

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
