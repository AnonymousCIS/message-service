package org.anonymous.message.controllers;

import lombok.Data;
import org.anonymous.global.paging.CommonSearch;

import java.util.List;

@Data
public class MessageSearch extends CommonSearch {
    private List<String> sender;
    private String mode; // receive이거나 값이 없으면 받은 쪽지, send : 보낸 쪽지
}
