package org.potwings.authentication.board.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.potwings.authentication.board.dto.BoardRequestDto;
import org.potwings.authentication.board.dto.BoardResponseDto;
import org.potwings.authentication.board.entity.Board;
import org.potwings.authentication.board.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

  private final BoardRepository boardRepository;

  @Transactional
  public BoardResponseDto createBoard(BoardRequestDto requestDto) {
    Board board = requestDto.toEntity();
    Board savedBoard = boardRepository.save(board);
    return BoardResponseDto.from(savedBoard);
  }

  public List<BoardResponseDto> getAllBoards() {
    return boardRepository.findAllByOrderByCreatedAtDesc()
        .stream()
        .map(BoardResponseDto::from)
        .collect(Collectors.toList());
  }

  public BoardResponseDto getBoardById(Long id) {
    Board board = boardRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));
    return BoardResponseDto.from(board);
  }

  @Transactional
  public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto) {
    Board board = boardRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

    board.update(requestDto.getTitle(), requestDto.getContent());
    return BoardResponseDto.from(board);
  }

  @Transactional
  public void deleteBoard(Long id) {
    Board board = boardRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));
    boardRepository.delete(board);
  }
}