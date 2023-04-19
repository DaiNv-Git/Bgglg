package com.example.itspower.model.entity;

import com.example.itspower.model.resultset.ReportDto;
import com.example.itspower.response.view.ViewDetailResponse;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "report")
@Data
@SqlResultSetMapping(
        name = "report_dto",
        classes = @ConstructorResult(
                targetClass = ReportDto.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "groupId", type = Integer.class),
                        @ColumnResult(name = "demarcation", type = Integer.class),
                        @ColumnResult(name = "laborProductivity", type = Integer.class),
                        @ColumnResult(name = "transferNum", type = Integer.class),
                        @ColumnResult(name = "supportNum", type = Integer.class),
                        @ColumnResult(name = "restNum", type = Integer.class),
                        @ColumnResult(name = "partTimeNum", type = Integer.class),
                        @ColumnResult(name = "studentNum", type = Integer.class),
                        @ColumnResult(name = "totalRice", type = Integer.class),
                        @ColumnResult(name = "reportDate", type = Date.class),
                }
        )
)
@SqlResultSetMapping(
        name = "ViewDetailResponse",
        classes = @ConstructorResult(
                targetClass = ViewDetailResponse.class,
                columns = {
                        @ColumnResult(name = "totalEmp", type = Integer.class),
                        @ColumnResult(name = "laborProductivityTeam", type = Integer.class),
                        @ColumnResult(name = "restEmp", type = Integer.class),
                        @ColumnResult(name = "partTimeEmp", type = Integer.class),
                        @ColumnResult(name = "student", type = Integer.class),
                        @ColumnResult(name = "riceCus", type = Integer.class),
                        @ColumnResult(name = "riceVip", type = Integer.class),
                        @ColumnResult(name = "riceEmp", type = Integer.class),

                }
        )
)

@NamedNativeQuery(
        name = "find_by_report",
        query = " select r.id,r.group_id as groupId,r.demarcation as demarcation ," +
                "(r.demarcation -r.student_num -r.rest_num- " +
                "(select tr.transfer_num from transfer tr where tr.report_id = r.id and tr.`type` = 1) " +
                "- (select tr1.transfer_num from transfer tr1 where tr1.report_id = r.id and tr1.`type` = 2)) as laborProductivity , " +
                "(select tr.transfer_num from transfer tr where tr.report_id = r.id and tr.`type` = 1) as transferNum,  " +
                "(select tr1.transfer_num from transfer tr1 where tr1.report_id = r.id and tr1.`type` = 2) as supportNum, " +
                "r.rest_num  as restNum, r.part_time_num  as partTimeNum, r.student_num  as studentNum," +
                "(IFNULL(r3.rice_cus,0) + IFNULL(r3.rice_emp,0) + IFNULL(r3.rice_vip,0)) as totalRice,r.report_date as reportDate " +
                "from report r  " +
                "left join rice r3 on r3.report_id = r.id  " +
                "where DATE_FORMAT(r.report_date, '%Y%m%d') = DATE_FORMAT(:reportDate, '%Y%m%d') AND r.group_id = :groupId ",
        resultSetMapping = "report_dto"
)

@NamedNativeQuery(
        name = "get_view_report",
        query = " SELECT  sum(ifNull(demarcation,0)) as totalEmp,sum(ifNull(labor_productivity,0)) as laborProductivityTeam," +
                "sum(ifNull(rest_num,0)) as restEmp, " +
                "sum(ifNull(part_time_num,0)) as partTimeEmp,  " +
                "sum(student_num) as student ," +
                "sum(ifNull(ri.rice_Cus,0)) as riceCus,sum(ifNull(rice_vip,0)) as riceVip,sum(ifNull(rice_emp,0)) as riceEmp" +
                " FROM report  r  left join rice ri on ri.report_id=r.id" +
                " where group_id in (SELECT gr.id FROM group_role gr where parent_id =:parentId or gr.id=:parentId ) " +
                "and DATE_FORMAT(r.report_date, '%Y%m%d') = DATE_FORMAT(:reportDate, '%Y%m%d')",
        resultSetMapping = "ViewDetailResponse"
)
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = 0;
    @Column(name = "report_date")
    private Date reportDate;
    @Column(name = "part_time_num")
    private Integer partTimeNum = 0;
    @Column(name = "student_num")
    private Integer studentNum = 0;
    @Column(name = "rest_num")
    private Integer restNum = 0;
    @Column(name = "group_id")
    private Integer groupId;
    @Column(name = "demarcation")
    private Integer demarcation = 0;
    @Column(name = "professionLabor")
    private Integer professionLabor = 0;
    @Column(name = "professionNotLabor")
    private Integer professionNotLabor = 0;
    @Column(name = "labor_productivity")
    private Integer laborProductivity = 0;
}
