package com.example.itspower.model.entity;

import com.example.itspower.model.resultset.RestDto;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "rest")
@Data
@SqlResultSetMapping(
        name = "rest_dto",
        classes = @ConstructorResult(
                targetClass = RestDto.class,
                columns = {
                        @ColumnResult(name = "restId", type = Integer.class),
                        @ColumnResult(name = "restName", type = String.class),
                        @ColumnResult(name = "reasonId", type = Integer.class),
                        @ColumnResult(name = "reasonName", type = String.class)
                }
        )
)
@NamedNativeQuery(
        name = "find_by_rest",
        query = "select r.id as restId, r.rest_name as restName,r3.id as reasonId, r3.name as reasonName from rest r " +
                "left join report r2 on r.report_id = r2.id " +
                "left join reason r3 on r3.id =r.reason_id  where r.report_id = :reportId ",
        resultSetMapping = "rest_dto"
)
public class RestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int restId;
    @Column(name = "rest_name")
    private String restName;
    @Column(name = "employee_Labor")
    private String employeeLabor;
    @Column(name = "work_Time")
    private Float workTime;
    @Column(name = "session")
    private String session;
    @Column(name = "reason_id")
    private int reasonId = 0;
    @Column(name = "reportId")
    private int reportId = 0;

}
