package com.rocketseat.certification_nlw.modules.students.useCases;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocketseat.certification_nlw.modules.questions.entities.QuestionEntity;
import com.rocketseat.certification_nlw.modules.questions.repositories.QuestionRepository;
import com.rocketseat.certification_nlw.modules.students.dto.StudentCertificationAnswersDTO;
import com.rocketseat.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.rocketseat.certification_nlw.modules.students.entities.AnswersCertificationsEntity;
import com.rocketseat.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.rocketseat.certification_nlw.modules.students.entities.StudentEntity;
import com.rocketseat.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import com.rocketseat.certification_nlw.modules.students.repositories.StudentRepository;

@Service
public class StudentCertificationAnswerUseCase {

 
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CertificationStudentRepository certificationStudentRepository;

    @Autowired
    private VerifyIfHasCertificationsUseCase verifyIfHasCertificationsUseCase;

    
    public CertificationStudentEntity execute(StudentCertificationAnswersDTO dto) throws Exception {
    
        var hasCertifiication = verifyIfHasCertificationsUseCase.execute(new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));

        if(hasCertifiication){
            throw new Exception("Você já tirou sua certificação");
        }

        List<QuestionEntity> questionsEntity = questionRepository.findByTechnology(dto.getTechnology());
        List<AnswersCertificationsEntity> answersCertifications = new ArrayList<>();

        AtomicInteger correctAnswers = new AtomicInteger(0);

        dto.getQuestionsAnswers()
        .stream().forEach(questionAnswer -> {
            var question = questionsEntity.stream().filter(q -> q.getId().equals(questionAnswer.getQuestionId()));

            var findCorrectAlternativce = question.findFirst().get().getAlternatives().stream().filter(alternative -> alternative.isCorrect())
            .findFirst().get();

            if(findCorrectAlternativce.getId().equals(questionAnswer.getAlternativeId())){
                questionAnswer.setCorrect(true);
                correctAnswers.incrementAndGet();
            }else{
                questionAnswer.setCorrect(false);
            }
            var answersCertificationsEntity = AnswersCertificationsEntity.builder()
            .answerID(questionAnswer.getAlternativeId())
            .questionID(questionAnswer.getQuestionId())
            .isCorrect(questionAnswer.isCorrect()).build();
            answersCertifications.add(answersCertificationsEntity);

            

        });

        var student = studentRepository.findByEmail(dto.getEmail());
        UUID studentID;
        if(student.isEmpty()){
            var studentCreated =
            StudentEntity.builder()
            .email(dto.getEmail()).build();
            studentCreated = studentRepository.save(studentCreated);
            studentID = studentCreated.getId();
        }else{
            studentID = student.get().getId();
        }


        CertificationStudentEntity certificationStudentEntity = 
        CertificationStudentEntity.builder()
        .technology(dto.getTechnology())
        .studentID(studentID)
        .grade(correctAnswers.get())
        .build(); 
        
        var certificationStudentCreated = certificationStudentRepository.save(certificationStudentEntity);

        answersCertifications.stream().forEach(answersCertification -> {
            answersCertification.setCertificationID(certificationStudentEntity.getId());
            answersCertification.setCertificationStudentEntity(certificationStudentEntity);
        });
        certificationStudentEntity.setAnswersCertificationsEntity(answersCertifications);
        certificationStudentRepository.save(certificationStudentEntity);

        return certificationStudentCreated ;
    }
    
}