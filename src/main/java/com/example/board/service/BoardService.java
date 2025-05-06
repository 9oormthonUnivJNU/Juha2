package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.BoardEntity;
import com.example.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
* Service 에서 일반적으로 하는 일
* DTO -> Entity 변환 (controller에서 데이터를 repository로 넘겨줄 때 변환; POST)  (Entity 클래스에서 작업)
* Entity -> DTO 변환 (repository에서 데이터를 controller로 넘겨줄 때 변환; GET)   (DTO 클래스에서 작업)
* Entity는 DB와 직접적인 연관이 되기 때문에 (MVC)View 단으로 노출을 하지 않아야 함
* */

@Service
@RequiredArgsConstructor  // final 필드 변수의 생성자 의존성 주입 애노테이션
public class BoardService {  // 비즈니스 로직, 트랜잭션 관리 
    private final BoardRepository boardRepository;

    public void save(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toSaveEntity((boardDTO));

        // save() 호출해야 DB에 insert 가능
        boardRepository.save(boardEntity);  // JpaRepository.save() 내부는 Entity 클래스를 파라미터로 받고 있음
    }

    public List<BoardDTO> findAll() {
        // Repository에서 데이터를 가져오면 무조건 Entity 타입으로 반환되므로
        // Entity 타입인 리스트 객체에 DB에서 불러오는 Entity 객체들을 저장
        List<BoardEntity> boardEntityList = boardRepository.findAll();

        // Repository에서 반환 받은 Entity 리스트를 DTO 타입 리스트로 옮겨 닮아서 반환해줘야 함
        List<BoardDTO> boardDTOList = new ArrayList<>();

        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));  // Entity -> DTO 변환하고 DTOList에 삽입
        }

        return boardDTOList;
    }

    @Transactional // JPA 제공 메서드가 아닌 커스텀하는 DB에 접근하는 메서드는 트랜잭션 처리를 해줘야 함 (JPA는 알아서 트랜잭션 관리해줌)
    public void updateHits(long id) {  // JPA에서 제공하는 조건 외에 조회수 증가와 같은 특별한 목적의 메서드는 repository에 따로 정의해주어야 함
        boardRepository.updateHits(id);
    }

    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);

            return boardDTO;
        }
        else {
            return null;
        }
    }

    public BoardDTO update(BoardDTO boardDTO) {
        // JPA는 update 메서드가 없고, save 메서드로 insert와 update를 모두 수행함
        // 인자로 id를 넣어주면 update, id 없으면 insert (처음에 생성될 때 entity 객체는 id 값을 넘겨받지 않으므로)
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);  // DTO -> Entity 변환
        boardRepository.save(boardEntity);  // Entity를 DB에 저장

        return findById(boardDTO.getId());  // update한 게시글의 DB 상세 조회 값을 반환
        // (위에서 정의한 findById에서 Optional 타입으로 반환되는 데이터에 대한 예외처리를 해줌)
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    // 한 페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬하여 Page 객체로 반환
    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3;  // 한 페이지에 나오는 게시글 갯수
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
                // 페이지 요청(몇 페이지, 게시글 갯수, 정렬(내림차순, Entity 값(id) 기준))
        // boardEntities의 타입으로 List로 반환할 수도 있지만
        // 현재 boardEntities의 getter 메서드는 Page 객체의 메서드이므로 List<BoardEntity> 객체로 반환하면 아래 메서드들은 사용할 수 없음
        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부
        // Page 객체가 제공해주는 메서드들이 유용한게 많아서 Page 타입으로 사용하는 것을 추천 (Page의 구현체 또한 내부에서는 List를 사용함)

        // Page 객체는 map 메서드를 제공하여 Entity -> DTO 로 변환할 수 있음
        // 필요한 목록: id, writer, title, hits, createTime
        // 필요한 목록만 DTO 객체에 담아주면 됨 => BoardDTO에 필요한 목록을 파라미터로 받는 생성자 생성
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(  // board는 BoardEntity 객체
                board.getId(),
                board.getBoardWriter(),
                board.getBoardTitle(),
                board.getBoardHits(),
                board.getCreateTime()
        ));

        return boardDTOS;
    }
}

