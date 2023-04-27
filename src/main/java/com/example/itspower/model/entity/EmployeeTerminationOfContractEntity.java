package com.example.itspower.model.entity;

import com.example.itspower.response.export.EmployeeExportExcelContractEnd;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
@SqlResultSetMapping(
        name = "Employee_Report",
        classes = @ConstructorResult(
                targetClass = EmployeeExportExcelContractEnd.class,
                columns = {
                        @ColumnResult(name = "groupName", type = String.class),
                        @ColumnResult(name = "startDate", type = String.class),
                        @ColumnResult(name = "employeeName", type = String.class),
                        @ColumnResult(name = "laborCode", type = String.class),
                }
        )
)

@NamedNativeQuery(
        name = "find_by_employee_report",
        query = "select gr.group_name as groupName,DATE_FORMAT(etc.start_date ,'%Y-%m-%d') as startDate," +
                "etc.employee_name as employeeName, " +
                "etc.employee_labor as laborCode from report r " +
                "join emp_termination_contract etc on r.group_id = etc.group_id " +
                "join group_role gr on r.group_id = gr.id\n" +
                "where DATE_FORMAT(r.report_date ,'%Y%m%d') =DATE_FORMAT(:reportDate ,'%Y%m%d') ",
        resultSetMapping = "Employee_Report"
)
@Data
@Entity
@Table(name = "emp_termination_contract")
public class EmployeeTerminationOfContractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "employee_name")
    private String employeeName;
    @Column(name = "employee_labor")
    private String employeeLabor;
    @Column(name = "group_id")
    private Integer groupId;
}
