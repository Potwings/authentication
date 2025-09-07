package org.potwings.authentication.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.potwings.authentication.board.entity.Board;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRequestDto {

  @NotBlank(message = "제목을 입력해주세요.")
  @Size(max = 100, message = "제목은 100자 이내로 입력해주세요.")
  private String title;

  @NotBlank(message = "내용을 입력해주세요.")
  private String content;

  @NotBlank(message = "작성자를 입력해주세요.")
  @Size(max = 50, message = "작성자는 50자 이내로 입력해주세요.")
  private String writer;

  public Board toEntity() {
    return Board.builder()
        .title(title)
        .content(content)
        .writer(writer)
        .build();
  }
}