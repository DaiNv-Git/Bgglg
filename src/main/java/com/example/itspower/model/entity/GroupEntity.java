package com.example.itspower.model.entity;
import com.example.itspower.model.resultset.GroupRoleDto;
import com.example.itspower.model.resultset.RootNameDto;
import com.example.itspower.model.resultset.ViewAllDto;
import com.example.itspower.response.group.GroupRoleDemarcationRes;
import com.example.itspower.response.group.ViewDetailGroupResponse;
import com.example.itspower.response.group.ViewGroupRoot;
import com.example.itspower.response.view.ListNameRestResponse;
import com.example.itspower.response.view.ReasonResponse;
import com.example.itspower.response.view.RootResponse;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "group_role")
@Data
@SqlResultSetMapping(
        name = "RootNameDto",
        classes = @ConstructorResult(
                targetClass = RootNameDto.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),}
        )
)

@NamedNativeQuery(
        name = "findAllRoot",
        query = "select DISTINCT parent_id as id from group_role gr2 where parent_id  is not null  order by parent_id desc",
        resultSetMapping = "RootNameDto"
)
@SqlResultSetMapping(
        name = "rootResponse",
        classes = @ConstructorResult(
                targetClass = RootResponse.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                }
        )
)

@NamedNativeQuery(
        name = "findRoot",
        query = "SELECT gr.id as id ,gr.group_name as name  from group_role gr where parent_id is  null",
        resultSetMapping = "rootResponse"
)
@SqlResultSetMapping(
        name = "GroupRoleDto",
        classes = @ConstructorResult(targetClass = GroupRoleDto.class, columns = {
                @ColumnResult(name = "id", type = int.class),
                @ColumnResult(name = "parentId", type = int.class),
                @ColumnResult(name = "demarcationAvailable", type = Integer.class),
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "label", type = String.class),
        }
        )
)
@NamedNativeQuery(name = "findAllRole", query = "select gr.id  as id,\n" +
        "       gr.group_name                               as name,\n" +
        "       gr.group_name                               as label,\n" +
        "       (IF(gr.parent_id is null, 0, gr.parent_id)) as parentId,\n" +
        "        gr.demarcation_available        as demarcationAvailable\n" +
        "from group_role gr;",
        resultSetMapping = "GroupRoleDto"
)
@SqlResultSetMapping(
        name = "getAllDinhBien",
        classes = @ConstructorResult(targetClass = GroupRoleDemarcationRes.class, columns = {
                @ColumnResult(name = "groupId", type = int.class),
                @ColumnResult(name = "dinhBien", type = Integer.class),
                @ColumnResult(name = "groupName", type = String.class)
        }
        )
)


@NamedNativeQuery(name = "findAllDinhBien", query = "SELECT gr.id as groupId," +
        "gr.demarcation_available as dinhBien," +
        "gr .group_name as groupName\n" +
        "from group_role gr",
        resultSetMapping = "getAllDinhBien"
)

@SqlResultSetMapping(
        name = "ViewAllDto",
        classes = @ConstructorResult(targetClass = ViewAllDto.class, columns = {
                @ColumnResult(name = "groupId", type = Integer.class),
                @ColumnResult(name = "groupParentId", type = Integer.class),
                @ColumnResult(name = "groupName", type = String.class),
                @ColumnResult(name = "reportDemarcation", type = Integer.class),
                @ColumnResult(name = "laborProductivity", type = Double.class),
                @ColumnResult(name = "partTimeNum", type = Integer.class),
                @ColumnResult(name = "restNum", type = Double.class),
                @ColumnResult(name = "studentNum", type = Integer.class),
                @ColumnResult(name = "riceCus", type = Integer.class),
                @ColumnResult(name = "riceEmp", type = Integer.class),
                @ColumnResult(name = "riceVip", type = Integer.class),
        }
        )
)

@NamedNativeQuery(name = "findAllRoleView", query = "SELECT groupId,groupName,groupParentId,reportDemarcation,\n" +
        "laborProductivity,partTimeNum,restNum,studentNum,riceCus,riceEmp,riceVip\n" +
        "FROM (\n" +
        "    SELECT gr.id AS groupId,\n" +
        "           gr.group_name AS groupName, " +
        "           IF(gr.parent_id IS NULL, 0, gr.parent_id) AS groupParentId, " +
        "           rp.demarcation AS reportDemarcation, " +
        "           rp.labor_productivity AS laborProductivity, " +
        "           rp.part_time_num AS partTimeNum, " +
        "           rp.rest_num AS restNum, " +
        "           rp.student_num AS studentNum, " +
        "           r.rice_cus AS riceCus, " +
        "           r.rice_emp AS riceEmp,  " +
        "           r.rice_vip AS riceVip, " +
        "           rp.report_date AS reportDate  " +
        "    FROM group_role gr\n" +
        "    LEFT JOIN report rp ON gr.id = rp.group_id \n" +
        "    LEFT JOIN rice r ON rp.id = r.report_id \n" +
        ") subq -- add an alias for the subquery\n" +
        "WHERE DATE_FORMAT(subq.reportDate, '%Y%m%d') = DATE_FORMAT(:reportDate,'%Y%m%d')\n" +
        "or subq.reportDate is null  ",
        resultSetMapping = "ViewAllDto"
)
@SqlResultSetMapping(
        name = "viewDetailDto",
        classes = @ConstructorResult(
                targetClass = ViewDetailGroupResponse.class,
                columns = {
                        @ColumnResult(name = "groupKey", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "parentId", type = Integer.class),
                        @ColumnResult(name = "demarcation", type = Integer.class),
                        @ColumnResult(name = "laborProductivity", type = Integer.class),
                        @ColumnResult(name = "restEmp", type = Integer.class),
                        @ColumnResult(name = "partTimeEmp", type = Integer.class),
                        @ColumnResult(name = "studentNum", type = Integer.class),
                        @ColumnResult(name = "riceCus", type = Integer.class),
                        @ColumnResult(name = "riceVip", type = Integer.class),
                        @ColumnResult(name = "riceEmp", type = Integer.class),
                        @ColumnResult(name = "totalRiceNum", type = Integer.class),
                }
        )
)

@NamedNativeQuery(
        name = "findByViewDetail",
        query = "SELECT gr.id as groupKey ,gr.group_name as name, gr.parent_id as parentId , " +
                "r.demarcation as demarcation ,  " +
                "(r.demarcation -r.student_num -r.rest_num- " +
                "(select tr.transfer_num from transfer tr where tr.report_id = r.id and tr.`type` = 1) " +
                "- (select tr1.transfer_num from transfer tr1 where tr1.report_id = r.id and tr1.`type` = 2)) as laborProductivity , " +
                "r.rest_num as restEmp, " +
                "r.part_time_num as partTimeEmp, r.student_num as studentNum , " +
                "ri.rice_Cus as riceCus, ri.rice_vip as riceVip, ri.rice_emp as riceEmp, " +
                "(ri.rice_Cus + ri.rice_vip + ri.rice_emp) as totalRiceNum " +
                "FROM group_role gr left join report  r on r.group_id=gr.id left join rice ri on ri.report_id=r.id " +
                "Where DATE_FORMAT(r.report_date, '%Y%m%d') = DATE_FORMAT(:reportDate, '%Y%m%d') ",
        resultSetMapping = "viewDetailDto"
)

@NamedNativeQuery(
        name = "findByViewDetailParent",
        query = "SELECT gr.id as groupKey ,gr.group_name as name, gr.parent_id as parentId , " +
                "r.demarcation as demarcation,  " +
                "r.labor_productivity as laborProductivity, r.rest_num as restEmp, " +
                "r.part_time_num as partTimeEmp, r.student_num as studentNum , " +
                "ri.rice_Cus as riceCus, ri.rice_vip as riceVip, ri.rice_emp as riceEmp, " +
                "(NULLIF(ri.rice_Cus,0) + NULLIF(ri.rice_vip,0) + NULLIF(ri.rice_emp,0)) as totalRiceNum " +
                "FROM group_role gr left join report  r on r.group_id=gr.id left join rice ri on ri.report_id=r.id " +
                "where gr.id in (SELECT DISTINCT gr2.parent_id FROM group_role gr2 ) ",
        resultSetMapping = "viewDetailDto"
)

@SqlResultSetMapping(
        name = "ViewGroupRoot",
        classes = @ConstructorResult(targetClass = ViewGroupRoot.class, columns = {
                @ColumnResult(name = "id", type = Integer.class),
                @ColumnResult(name = "groupName", type = String.class),
                @ColumnResult(name = "numberChild", type = Integer.class)
        }
        )
)
@NamedNativeQuery(name = "view_group_root", query = "SELECT gr.id,gr.group_name as groupName,(SELECT COUNT(gr2.parent_id) from " +
        "group_role gr2 where  gr2.parent_id = gr.id ) as numberChild from group_role gr " +
        "where gr.id in (SELECT DISTINCT gr1.parent_id from group_role gr1 ) and group_name <> 'Tổ may' ",
        resultSetMapping = "ViewGroupRoot"
)
@SqlResultSetMapping(
        name = "reasonResponse",
        classes = @ConstructorResult(targetClass = ReasonResponse.class, columns = {
                @ColumnResult(name = "groupId", type = Integer.class),
                @ColumnResult(name = "reasonName", type = String.class),
                @ColumnResult(name = "total", type = Integer.class),
                @ColumnResult(name = "parentId", type = Integer.class),
        }
        )
)
@NamedNativeQuery(name = "view_reason", query = "SELECT r3.group_id as groupId, r.name as reasonName,count(r2.reason_id) as total," +
        " gr.parent_id as parentId " +
        "from reason r inner join " +
        "rest r2 on r.id = r2.reason_id " +
        "INNER join report r3 on r2.report_id =r3.id inner join group_role gr on gr.id = r3.group_id " +
        "where  DATE_FORMAT(r3.report_date, '%Y%m%d') = DATE_FORMAT(:reportDate, '%Y%m%d')" +
        "GROUP by r2.reason_id,r3.group_id",
        resultSetMapping = "reasonResponse"
)
@SqlResultSetMapping(
        name = "listNameRest",
        classes = @ConstructorResult(targetClass = ListNameRestResponse.class, columns = {
                @ColumnResult(name = "groupId", type = Integer.class),
                @ColumnResult(name = "nameEmployee", type = String.class),
                @ColumnResult(name = "reasonName", type = String.class),
                @ColumnResult(name = "labor", type = String.class),
        }
        )
)
@NamedNativeQuery(name = "view_list_reason", query = "SELECT  r3.group_id as groupId,rest_name as nameEmployee,r2.name as reasonName,employee_labor as labor from \n" +
        "rest r inner join " +
        "reason r2 on r.reason_id =r2.id " +
        "inner join report r3 on r.report_id =r3.id where  DATE_FORMAT(r3.report_date, '%Y%m%d') = DATE_FORMAT(:reportDate, '%Y%m%d')",
        resultSetMapping = "listNameRest"
)
public class GroupEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "group_name")
    private String groupName = "";
    @Column(name = "parent_id")
    private Integer parentId;
    @Column(name = "demarcation_available")
    private Float demarcationAvailable;
}
