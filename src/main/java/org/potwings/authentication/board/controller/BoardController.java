package org.potwings.authentication.board.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.potwings.authentication.board.dto.BoardRequestDto;
import org.potwings.authentication.board.dto.BoardResponseDto;
import org.potwings.authentication.board.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;

  @GetMapping
  public String list(Model model) {
    List<BoardResponseDto> boards = boardService.getAllBoards();
    model.addAttribute("boards", boards);
    return "board/list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("boardRequestDto", new BoardRequestDto());
    return "board/form";
  }

  @PostMapping("/new")
  @ResponseBody
  public ResponseEntity<?> create(@Valid @ModelAttribute BoardRequestDto boardRequestDto,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().body("validation_error");
    }
    boardService.createBoard(boardRequestDto);
    return ResponseEntity.ok().body("success");
  }

  @GetMapping("/{id}")
  public String detail(@PathVariable Long id, Model model) {
    BoardResponseDto board = boardService.getBoardById(id);
    model.addAttribute("board", board);
    return "board/detail";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    BoardResponseDto board = boardService.getBoardById(id);
    BoardRequestDto boardRequestDto = BoardRequestDto.builder()
        .title(board.getTitle())
        .content(board.getContent())
        .writer(board.getWriter())
        .build();
    model.addAttribute("boardRequestDto", boardRequestDto);
    model.addAttribute("boardId", id);
    return "board/edit";
  }

  @PostMapping("/{id}/edit")
  public String edit(@PathVariable Long id,
      @Valid @ModelAttribute BoardRequestDto boardRequestDto,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("boardId", id);
      return "board/edit";
    }
    boardService.updateBoard(id, boardRequestDto);
    return "redirect:/board/" + id;
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    boardService.deleteBoard(id);
    return "redirect:/board";
  }
}