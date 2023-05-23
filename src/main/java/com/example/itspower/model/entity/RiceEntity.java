package com.example.itspower.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "rice")
@Data
public class RiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer riceId;
    @Column(name = "rice_emp")
    private Integer riceEmp = 0;
    @Column(name = "rice_cus")
    private Integer riceCus = 0;
    @Column(name = "rice_vip")
    private Integer riceVip = 0;
    @Column(name = "report_id")
    private Integer reportId = 0;

}
