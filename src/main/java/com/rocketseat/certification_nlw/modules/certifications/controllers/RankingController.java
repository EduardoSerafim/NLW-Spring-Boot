package com.rocketseat.certification_nlw.modules.certifications.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocketseat.certification_nlw.modules.certifications.useCase.Top10RankingUseCase;
import com.rocketseat.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.rocketseat.certification_nlw.modules.students.repositories.CertificationStudentRepository;

@RestController
@RequestMapping("/ranking")
public class RankingController {
    
    @Autowired
    private Top10RankingUseCase top10RankingUseCase;
    @GetMapping
    public List<CertificationStudentEntity> top10(){
        return top10RankingUseCase.execute();
    }

}
