package com.example.itspower.model.entity;

import com.example.itspower.response.export.TransferExcel;
import com.example.itspower.response.transfer.TransferNumAccept;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

@Data
@Entity
@Table(name = "transfer")
@SqlResultSetMapping(
        name = "GroupAcceptDto",
        classes = @ConstructorResult(targetClass = TransferNumAccept.class, columns = {
                @ColumnResult(name = "groupName", type = String.class),
                @ColumnResult(name = "transferNum", type = Integer.class),
                @ColumnResult(name = "id", type = Integer.class)
        }
        )
)
@NamedNativeQuery(name = "group_accept",
        query = "select gr.group_name as groupName,a.transfer_num as transferNum ,gr.id from  (select * from " +
        "transfer tr where tr.group_id =:groupId ) a " +
        "inner join report r on a.report_id= r.id inner join group_role gr on r.group_id=gr.id " +
        "and DATE_FORMAT(r.report_date,'%Y%m%d') = DATE_FORMAT(:reportDate,'%Y%m%d') ",
        resultSetMapping = "GroupAcceptDto"
)

@SqlResultSetMapping(
        name = "excelTransfer",
        classes = @ConstructorResult(targetClass = TransferExcel.class, columns = {
                @ColumnResult(name = "transferDate", type = String.class),
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "labor", type = String.class),
                @ColumnResult(name = "oldGroup", type = String.class),
                @ColumnResult(name = "newGroup", type = String.class),
                @ColumnResult(name = "statusTransfer", type = String.class)
        }
        )
)
@NamedNativeQuery(name = "export_Transfer",
        query = "SELECT let.transfer_date AS transferDate," +
                "       let.name AS name," +
                "       let.labor AS labor," +
                "       gr.group_name AS oldGroup," +
                "       gro.group_name AS newGroup," +
                "        'Đã xác nhận' AS statusTransfer " +
                " FROM list_employee_transfer let  " +
                "INNER JOIN report r ON r.id = let.transfer_type " +
                "INNER JOIN group_role gr ON gr.id = r.group_id " +
                "INNER JOIN group_role gro ON gro.id = let.groupID where  " +
                "DATE_FORMAT(r.report_date,'%Y%m%d') = DATE_FORMAT(:reportDate,'%Y%m%d') ",
        resultSetMapping = "excelTransfer"
)
public class TransferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer transferId;
    @Column(name = "transfer_num")
    private Integer transferNum = 0;
    @Column(name = "report_id")
    private Integer reportId = 0;
    @Column(name = "group_id")
    private Integer groupId ;
    @Column(name = "is_access")
    private boolean isAccess;
    @Column(name = "transfer_date")
    private Date transferDate;
    @Column(name = "type")
    @Min(1)
    private Integer type = 0;
}
