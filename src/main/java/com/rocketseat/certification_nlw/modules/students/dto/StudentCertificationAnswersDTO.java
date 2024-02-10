package com.rocketseat.certification_nlw.modules.students.dto;

import java.util.List;

import com.rocketseat.certification_nlw.modules.questions.dto.QuestionAnswerDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCertificationAnswersDTO {

    private String email;
    private String technology;
    private List<QuestionAnswerDTO> questionsAnswers;
    
}