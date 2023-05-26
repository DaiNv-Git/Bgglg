package com.example.itspower.model.entity;

import com.example.itspower.model.resultset.ReportDto;
import com.example.itspower.response.ReportNangsuatResponse;
import com.example.itspower.response.export.ExportExcelDtoReport;
import com.example.itspower.response.export.ExportExcelEmpRest;
import com.example.itspower.response.report.ReportSearchResponse;
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
                        @ColumnResult(name = "laborProductivity", type = Double.class),
                        @ColumnResult(name = "transferNum", type = Integer.class),
                        @ColumnResult(name = "supportNum", type = Integer.class),
                        @ColumnResult(name = "restNum", type = Double.class),
                        @ColumnResult(name = "partTimeNum", type = Integer.class),
                        @ColumnResult(name = "studentNum", type = Integer.class),
                        @ColumnResult(name = "totalRice", type = Integer.class),
                        @ColumnResult(name = "reportDate", type = Date.class),
                        @ColumnResult(name = "professionNotLabor", type = Integer.class),
                        @ColumnResult(name = "professionLabor", type = Integer.class),
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
@SqlResultSetMapping(
        name = "reportSearch",
        classes = {
                @ConstructorResult(
                        targetClass = ReportSearchResponse.class,
                        columns = {
                                @ColumnResult(name = "id", type = Integer.class),
                                @ColumnResult(name = "groupId", type = Integer.class),
                                @ColumnResult(name = "demarcation", type = Integer.class),
                                @ColumnResult(name = "demarcationAvailable", type = Integer.class),
                                @ColumnResult(name = "unproductiveLabor", type = Float.class),
                                @ColumnResult(name = "laborProductivity", type = Double.class),
                                @ColumnResult(name = "numberStop", type = Integer.class),
                                @ColumnResult(name = "restNum", type = Double.class),
                                @ColumnResult(name = "partTimeNum", type = Double.class),
                                @ColumnResult(name = "transferReceive", type = Integer.class),
                                @ColumnResult(name = "transferTo", type = Integer.class),
                                @ColumnResult(name = "studentNum", type = Integer.class),
                                @ColumnResult(name = "totalRice", type = Integer.class),
                                @ColumnResult(name = "reportDate", type = Date.class),
                                @ColumnResult(name = "professionNotLabor", type = Integer.class),
                                @ColumnResult(name = "professionLabor", type = Integer.class),
                                @ColumnResult(name = "riceCus", type = Integer.class),
                                @ColumnResult(name = "riceEmployee", type = Integer.class),
                                @ColumnResult(name = "riceVip", type = Integer.class),
                                @ColumnResult(name = "riceID", type = Integer.class),
                        }
                )
        }
)

@NamedNativeQuery(
        name = "report_Search",
        query = "SELECT r.id as id,r.demarcation as demarcation,r.group_id as groupId,r.demarcation_available as demarcationAvailable, " +
                "r.unproductive_labor as unproductiveLabor, " +
                "r.labor_productivity as laborProductivity, r.rest_num  as restNum, " +
                "r.part_time_num  as partTimeNum, r.student_num  as studentNum , " +
                "(IFNULL(r3.rice_cus,0) + IFNULL(r3.rice_emp,0) + IFNULL(r3.rice_vip,0)) as totalRice, " +
                "r.report_date as reportDate,IFNULL(r.profession_not_labor,0) as professionNotLabor, " +
                "IFNULL(r.profession_labor,0) as professionLabor , " +
                "IFNULL((SELECT sum(transfer_num)  from transfer t where t.group_id =:groupId and " +
                "DATE_FORMAT(t.transfer_date, '%Y%m%d') = DATE_FORMAT(:reportDate, '%Y%m%d')  ),0)  as transferReceive, " +
                "IFNULL((SELECT sum(transfer_num) from transfer t where report_id = " +
                "(SELECT id  from report r where " +
                "DATE_FORMAT(r.report_date, '%Y%m%d') = DATE_FORMAT(:reportDate, '%Y%m%d') AND r.group_id = :groupId)),0) " +
                "as transferTo , " +
                "IFNULL((SELECT COUNT(employee_labor)  from emp_termination_contract etc where group_id =:groupId and " +
                "DATE_FORMAT(etc.start_date, '%Y%m%d') = DATE_FORMAT(:reportDate, '%Y%m%d')),0) as numberStop ," +
                " r3.rice_cus as riceCus,r3.rice_emp as riceEmployee,r3.rice_vip as riceVip,r3.id as riceID " +
                "from report r  " +
                "left join rice r3 on r3.report_id = r.id " +
                "where DATE_FORMAT(r.report_date, '%Y%m%d') = DATE_FORMAT(:reportDate, '%Y%m%d') AND r.group_id = :groupId",
        resultSetMapping = "reportSearch"
)
@NamedNativeQuery(
        name = "find_by_report",
        query = " select r.id,r.group_id as groupId,r.demarcation as demarcation ," +
                "r.labor_productivity as laborProductivity , " +
                "(select ifNull(tr.transfer_num,0) from transfer tr where tr.report_id = r.id and tr.`type` = 1) as transferNum,  " +
                "(select ifNull(tr1.transfer_num,0) from transfer tr1 where tr1.report_id = r.id and tr1.`type` = 2) as supportNum, " +
                "r.rest_num  as restNum, r.part_time_num  as partTimeNum, r.student_num  as studentNum," +
                "(IFNULL(r3.rice_cus,0) + IFNULL(r3.rice_emp,0) + IFNULL(r3.rice_vip,0)) as totalRice,r.report_date as reportDate, " +
                "IFNULL(r.profession_not_labor,0) as professionNotLabor,IFNULL(r.profession_labor,0) as professionLabor   " +
                "from report r  " +
                "left join rice r3 on r3.report_id = r.id  " +
                "where DATE_FORMAT(r.report_date, '%Y%m%d') = DATE_FORMAT(:reportDate, '%Y%m%d') AND r.group_id = :groupId  ",
        resultSetMapping = "report_dto"
)

@NamedNativeQuery(
        name = "find_by_excel",
        query = "select gr.group_name as groupName," +
                "IFNULL(ri.rice_emp,0) as riceEmp," +
                "IFNULL(ri.rice_cus,0) as riceCus," +
                "DATE_FORMAT(r.report_date ,'%m-%d-%Y') as reportDate " +
                "from rice ri " +
                "join report r on r.id= ri.report_id " +
                "join group_role gr on r.group_id = gr.id " +
                "where DATE_FORMAT(r.report_date ,'%Y%m%d') =DATE_FORMAT(:reportDate ,'%Y%m%d')  " +
                " order by gr.sort  asc",
        resultSetMapping = "Export_Excel_Report"
)
@SqlResultSetMapping(
        name = "Export_Excel_Report",
        classes = @ConstructorResult(
                targetClass = ExportExcelDtoReport.class,
                columns = {
                        @ColumnResult(name = "groupName", type = String.class),
                        @ColumnResult(name = "riceEmp", type = Integer.class),
                        @ColumnResult(name = "riceCus", type = Integer.class),
                        @ColumnResult(name = "reportDate", type = String.class)
                }
        )
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


@SqlResultSetMapping(
        name = "ExportExcelEmpRest",
        classes = @ConstructorResult(
                targetClass = ExportExcelEmpRest.class,
                columns = {
                        @ColumnResult(name = "reportDate", type = String.class),
                        @ColumnResult(name = "restName", type = String.class),
                        @ColumnResult(name = "labor", type = String.class),
                        @ColumnResult(name = "groupName", type = String.class),
                        @ColumnResult(name = "reasonName", type = String.class),

                }
        )
)

@NamedNativeQuery(
        name = "find_by_employee_rest",
        query = "select DATE_FORMAT(r.report_date ,'%Y-%m-%d') as reportDate,r2.rest_name as restName,\n" +
                "r2.employee_labor as labor,\n" +
                "gr.group_name as groupName,r3.name as reasonName\n" +
                "from report r join rest r2 on r.id = r2.report_id\n" +
                "left join group_role gr on gr.id = r.group_id\n" +
                "left join reason r3 on r2.reason_id = r3.id\n" +
                "where DATE_FORMAT(r.report_date ,'%Y%m%d') = DATE_FORMAT(:reportDate ,'%Y%m%d')",
         resultSetMapping = "ExportExcelEmpRest"
)


@SqlResultSetMapping(
        name = "GroupReportMapping",
        classes = @ConstructorResult(
                targetClass = ReportNangsuatResponse.class,
                columns = {
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "demarcation", type = Double.class),
                        @ColumnResult(name = "restNum", type = Integer.class),
                        @ColumnResult(name = "laborProductivity", type = Double.class),
                        @ColumnResult(name = "stopNumber", type = Integer.class),
                        @ColumnResult(name = "newNumber", type = Integer.class),
                        @ColumnResult(name = "student", type = Double.class),
                        @ColumnResult(name = "partTime", type = Double.class),
                }
        )
)
@NamedNativeQuery(
        name = "GroupReport.findAll",
        query = "SELECT gr.group_name AS name, r.demarcation AS demarcation, r.rest_num AS restNum, r.labor_productivity AS laborProductivity, \n" +
                "    COUNT(etc.id) AS stopNumber, COUNT(ge.id) AS newNumber ,r.student_num as student,r.part_time_num as partTime " +
                " FROM report r\n" +
                "INNER JOIN group_role gr ON r.group_id = gr.id\n" +
                "LEFT JOIN emp_termination_contract etc ON etc.group_id = gr.id AND DATE_FORMAT(etc.start_date, '%Y%m%d') = DATE_FORMAT(:reportDate, '%Y%m%d')\n" +
                "LEFT JOIN group_employee ge ON ge.group_id = gr.id AND DATE_FORMAT(ge.createDate, '%Y%m%d') = DATE_FORMAT(:reportDate, '%Y%m%d') \n" +
                "WHERE DATE_FORMAT(r.report_date, '%Y%m%d') = DATE_FORMAT(:reportDate, '%Y%m%d')\n" +
                "GROUP BY gr.group_name, r.demarcation, r.rest_num, r.labor_productivity,r.student_num,r.part_time_num, gr.sort\n" +
                "ORDER BY gr.sort ASC",
        resultSetMapping = "GroupReportMapping"
)
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = 0;
    @Column(name = "report_date")
    private Date reportDate;
    @Column(name = "part_time_num")
    private Float partTimeNum ;
    @Column(name = "student_num")
    private Integer studentNum = 0;
    @Column(name = "rest_num")
    private Float restNum ;
    @Column(name = "group_id")
    private Integer groupId;
    @Column(name = "demarcation")
    private Float demarcation ;
    @Column(name = "unproductiveLabor")
    private Float unproductiveLabor ;
    @Column(name = "demarcationAvailable")
    private Float demarcationAvailable ;
    @Column(name = "professionLabor")
    private Integer professionLabor = 0;
    @Column(name = "professionNotLabor")
    private Integer professionNotLabor = 0;
    @Column(name = "labor_productivity")
    private Float laborProductivity ;
}
