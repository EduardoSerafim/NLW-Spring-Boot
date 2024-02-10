package com.rocketseat.certification_nlw.modules.students.controllers;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocketseat.certification_nlw.modules.students.dto.StudentCertificationAnswersDTO;
import com.rocketseat.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.rocketseat.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.rocketseat.certification_nlw.modules.students.useCases.StudentCertificationAnswerUseCase;
import com.rocketseat.certification_nlw.modules.students.useCases.VerifyIfHasCertificationsUseCase;

@RestController
@RequestMapping("/students")
public class StudentController {
    
    @Autowired
    private VerifyIfHasCertificationsUseCase verifyIfHasCertificationsUseCase;
    
    @Autowired
    private StudentCertificationAnswerUseCase studentCertificationAnswerUseCase;

    @PostMapping("/verifyIfHasCertification")
    public String verifyIfHasCertification(@RequestBody VerifyHasCertificationDTO dto){
        var result = verifyIfHasCertificationsUseCase.execute(dto);
        if(result)
            return "Usuário já fez a prova";

        return "Usuário pode fazer a prova";
    }

    @PostMapping("certification/answer")
    public ResponseEntity<Object> certificationAnswer(@RequestBody StudentCertificationAnswersDTO dto)  {
        try {
            var result = studentCertificationAnswerUseCase.execute(dto);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()); 
        }
        
    }

}
