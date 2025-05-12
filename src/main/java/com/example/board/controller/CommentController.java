package com.example.board.controller;

import com.example.board.dto.CommentDTO;
import com.example.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    @ResponseBody
    public String save(@ModelAttribute CommentDTO commentDTO) {
        System.out.println("commentDTO: " + commentDTO);
    // 출력: commentDTO: CommentDTO(id=null, commentWriter=댓글작성자1, commentContents=댓글내용1, boardId=1, commentCreatedTime=null)
        return "요청 성공";
    }
}
