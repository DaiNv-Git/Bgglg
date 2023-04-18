package com.example.itspower.model.entity;

import com.example.itspower.response.employee.EmployeeGroupResponse;
import lombok.Data;

import javax.persistence.*;
@SqlResultSetMapping(
        name = "getEmployee",
        classes = @ConstructorResult(targetClass = EmployeeGroupResponse.class, columns = {
                @ColumnResult(name = "employeeId", type = Integer.class),
                @ColumnResult(name = "laborCode", type = String.class),
                @ColumnResult(name = "employeeName", type = String.class),
                @ColumnResult(name = "groupId", type = Integer.class),
                @ColumnResult(name = "groupName", type = String.class),
        }
        )
)
@NamedNativeQuery(name = "view_all_employee", query = "SELECT ge.id as employeeId,ge.labor_code as laborCode" +
        ",ge.name as employeeName,ge.group_id as groupId,\n" +
        "gr.group_name as groupName\n" +
        "from group_employee ge inner join group_role gr on ge.group_id =gr.id" +
        " where (:groupId IS NULL OR ge.group_id LIKE CONCAT(:groupId)) and " +
        "(:groupName IS NULL OR gr.group_name LIKE CONCAT('%',:groupName,'%')) and " +
        "(:laborCode IS NULL OR ge.labor_code LIKE CONCAT('%',:laborCode,'%')) and " +
        "(:employeeName is null or ge.name like CONCAT('%',:employeeName,'%'))"+
        " ORDER BY gr.group_name ASC LIMIT :pageSize OFFSET :pageNo ",
        resultSetMapping = "getEmployee"
        )
@Entity
@Table(name = "group_Employee")
@Data
public class EmployeeGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "laborCode")
    private String laborCode;
    @Column(name = "group_id")
    private Integer groupId;

}
