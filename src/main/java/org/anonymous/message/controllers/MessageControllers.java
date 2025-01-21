package org.anonymous.message.controllers;

import lombok.RequiredArgsConstructor;
import org.anonymous.global.rests.JSONData;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MessageControllers {
    /*
    * - POST /write : 쪽지 작성
- GET /view/{seq} : 쪽지 단일 조회
- GET /list : 쪽지 목록
- GET /count : 쪽지 미열람 개수 확인
- PATCH /deletes : 쪽지 단일 | 목록 삭제
    * */

    /**
     * 쪽지 작성
     *
     * @return
     */
    @PostMapping("/write")
    public JSONData write() {

        return null;
    }

    /**
     * 쪽지 단일 조회
     *
     * @param seq
     * @return
     */
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {

        return null;
    }

    /**
     * 쪽지 목록 조회
     *
     * @return
     */
    @GetMapping("/list")
    public JSONData list() {

        return null;
    }

    /**
     * 쪽지 미열람 개수 확인
     *
     * @return
     */
    @GetMapping("/count")
    public JSONData count() {

        return null;
    }

    /**
     * 쪽지 단일 | 목록 삭제
     *
     * @return
     */
    @PatchMapping("/deletes")
    public JSONData deletes() {

        return null;
    }
}
