package com.example.itspower.model.entity;

import com.example.itspower.model.resultset.UserDto;
import com.example.itspower.response.user.ListUserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user")
@SqlResultSetMapping(
        name = "user_dto",
        classes = @ConstructorResult(
                targetClass = UserDto.class,
                columns = {
                        @ColumnResult(name = "userId", type = Integer.class),
                        @ColumnResult(name = "groupName", type = String.class),
                        @ColumnResult(name = "isAdmin", type = Boolean.class),
                        @ColumnResult(name = "isEdit", type = Boolean.class),
                        @ColumnResult(name = "isReport", type = Boolean.class),
                        @ColumnResult(name = "isView", type = Boolean.class),
                        @ColumnResult(name = "groupId", type = Integer.class),

                }
        )
)

@NamedNativeQuery(
        name = "loginInfor",
        query = "select u.id as userId, u.is_admin as isAdmin ,IFNULL(gr.group_name,'') as groupName, " +
                "u.is_edit as isEdit,u.is_report as isReport, " +
                "u.is_view as isView, IFNULL(ug.group_id,0) as groupId " +
                "from `user` u " +
                "left join user_group ug on ug.user_id = u.id  " +
                "left join group_role gr on ug.group_id = gr .id where u.user_login = :userLogin ",
        resultSetMapping = "user_dto"
)
@SqlResultSetMapping(
        name = "list_user",
        classes = @ConstructorResult(
                targetClass = ListUserResponse.class,
                columns = {
                        @ColumnResult(name = "userId", type = Integer.class),
                        @ColumnResult(name = "userName", type = String.class),
                        @ColumnResult(name = "password", type = String.class),
                        @ColumnResult(name = "isView", type = Boolean.class),
                        @ColumnResult(name = "isEdit", type = Boolean.class),
                        @ColumnResult(name = "isReport", type = Boolean.class),
                        @ColumnResult(name = "isAdmin", type = Boolean.class),
                        @ColumnResult(name = "groupName", type = String.class),

                }
        )
)

@NamedNativeQuery(
        name = "list_infor_user",
        query = "SELECT u.id as userId,u.user_login as userName,u.password as password,u.is_view as isView,\n" +
                "u.is_edit as isEdit , u.is_report as isReport,u.is_admin as isAdmin ,gr.group_name as groupName" +
                " from user u left join  user_group ug on u.id =ug.user_id " +
                "left join group_role gr on ug.group_id = gr.id " +
                " where (:groupName IS NULL OR gr.group_name LIKE CONCAT('%',:groupName,'%'))" +
                "and (:loginName IS NULL OR u.user_login LIKE CONCAT('%',:loginName,'%'))" +
                "ORDER BY u.id ASC LIMIT :pageSize OFFSET :pageNo ",
        resultSetMapping = "list_user"
)

public class UserEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_login")
    private String userLogin;
    @Column(name = "password")
    private String password;
    @Column(name = "is_admin")
    private boolean isAdmin;
    @Column(name = "is_report")
    private boolean isReport;
    @Column(name = "is_edit")
    private boolean isEdit;
    @Column(name = "is_view")
    private boolean isView;
}
