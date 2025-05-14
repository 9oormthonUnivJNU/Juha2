package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.CommentDTO;
import com.example.board.service.BoardService;
import com.example.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
// Lombok 애노테이션 (@NoArgsConstructor[기본 생성자 생성, (force = true)옵션 넣으면 final 변수를 0,false,null 로 초기화]/@RequiredArgsConstructor[선언만 되어있는 final 필드나 @NotNull 필드에 대한 초기화 생성자 생성]/@AllArgsConstructor[모든 필드에 대한 초기화 생성자 생성])
@RequiredArgsConstructor
@RequestMapping("/board")  // /board 에 대한 주소를 먼저 받고 후에 path parameter(/save 등등) 주소 처리
public class BoardController {
    private final BoardService boardService;  // @RequiredArgsConstructor에 의해 생성자 의존성 주입됨
    private final CommentService commentService;  // 댓글을 가져오기 위해 CommentService 주입

    @GetMapping("/save") // save read
    public String save() {
        return "save";
    }

    // 스프링이 파라미터로 받은 BoardDTO의 필드와 save.html의 input 항목의 동일한 name을 알아서 매핑함
    @PostMapping("/save") // save write
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        System.out.println("boardDTO: " + boardDTO);
        boardService.save(boardDTO);

        return "index";
    }

    @GetMapping("/")
    public String findAll(Model model) { // DB에서 데이터 전체 목록을 가져와야 할 때 Model 객체 사용
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여주는 과정
        List<BoardDTO> boardDTOList =  boardService.findAll();
        model.addAttribute("boardList", boardDTOList);

        return "list";
    }

    @GetMapping("/{id}")  // 경로상의 있는 값을 파라미터로 받는 애노테이션 @PathVariable
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page=1) Pageable pageable) {
        /*
        * 해당 게시글의 조회수를 하나 증가 시키고
        * 게시글 데이터를 가져와서 detail.html에 출력
        * */
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        /* 댓글 목록 가져오기 */
        List<CommentDTO> commentDTOList = commentService.findAll(id);

        model.addAttribute("commentList", commentDTOList);
        model.addAttribute("board", boardDTO);
        // 게시글 상세 페이지(detail)에서 경로에 몇 페이지의 게시물인지 포함 (리펙터링)
        model.addAttribute("page", pageable.getPageNumber());

        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);

        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        /*
        * 수정 처리하고 수정이 적용된 detail 페이지를 반환
        * */
        BoardDTO board = boardService.update(boardDTO);  // 수정이 된 객체
        model.addAttribute("board", board);

        return "detail";
        // return "redirect:/board/" + board.getId();  => 리다이렉트로도 할 수 있지만 조회수가 올라가버려서 안 씀
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/";
    }

    // /board/pageing?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
        /*
            Pageable은 qeury param으로 들어온 page 값을 받아주는 객체
            page= 값이 없을 경우 (애노테이션으로 기본 page=1을 미리 설정)
            paging 처리된 데이터를 가지고 화면으로 넘어가야 되므로 Model 객체 사용
        */
        // pageable.getPageNumber();

        // paging 처리하여 DTO Page 객체를 반환
        Page<BoardDTO> boardList = boardService.paging(pageable);

        /*
        * page 갯수가 20개라면
        * 현재 사용자가 3페이지를 보고 있을 경우
        * 1 2 3  이런 페이지 선택 버튼에서 3 번만 다르게 보이도록 설정
        * 보여지는 페이지 개수는 3개로 설정했을 때
        * 현재 페이지가 7이라면 7 8 9
        * */
        int blockLimit = 3; // 보여지는 페이지 갯수

        // 보여지는 페이지 계산 알고리즘
        int startPage = (((int) (Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;  // 1 4 7 10 ...
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages() ?  // 뒤로 남은 페이지가 limit 수보다 적을 경우
                startPage + blockLimit - 1 : boardList.getTotalPages());

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "paging";
    }
}