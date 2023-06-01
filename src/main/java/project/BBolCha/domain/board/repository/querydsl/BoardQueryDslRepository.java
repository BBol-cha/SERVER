package project.BBolCha.domain.board.repository.querydsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.BBolCha.domain.board.dto.service.response.BoardResponse;

import java.util.Optional;

public interface BoardQueryDslRepository {
    Page<BoardResponse.Detail> getPageBoardsAsDto(Pageable pageable, String arrangeDirection, String filter);
    Optional<BoardResponse.DetailDsl> getBoardDetail(Long id);
}
