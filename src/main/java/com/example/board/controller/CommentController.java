package com.example.board.controller;

import com.example.board.dto.CommentDTO;
import com.example.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    // @ResponseBody 에서 -> ResponseEntity (header와 body를 같이 다루는 객체)로 교체
    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO) {
        System.out.println("commentDTO: " + commentDTO);
        // 출력: commentDTO: CommentDTO(id=null, commentWriter=댓글작성자1, commentContents=댓글내용1, boardId=1, commentCreatedTime=null)
        Long saveResult = commentService.save(commentDTO);
        if (saveResult != null) {  // 작성 성
            /*
                작성 성공하면 save는 끝나면 안된다.
                작성하면 기존 작성 댓글 목록에 새 댓글이 추가되어 출력되어야 하기 때문
                -> 즉, 다시 전체 댓글을 조회해서 가지고 와서 반복문 형태로 출력하여야 함
            * */
            // 댓글 목록 가져와서 반환 -> 해당 게시글(board) id 기준의 댓글 전체
            List<CommentDTO> commentDTOList =  commentService.findAll(commentDTO.getBoardId());

            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        } else {
            // 작성 실패
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.(에러 메시지)", HttpStatus.NOT_FOUND);
        }
    }  // 댓글 작성이 제대로 됐으면 목록을 가져와서 res로 반환
}
