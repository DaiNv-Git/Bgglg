package com.example.itspower.repository;
import com.example.itspower.model.entity.GroupEntity;
import com.example.itspower.model.resultset.GroupRoleDto;
import com.example.itspower.model.resultset.ViewAllDto;
import com.example.itspower.repository.repositoryjpa.GroupJpaRepository;
import com.example.itspower.request.GroupRoleRequest;
import com.example.itspower.response.SuccessResponse;
import com.example.itspower.response.group.GroupRoleDemarcationRes;
import com.example.itspower.response.group.ViewDetailGroupResponse;
import com.example.itspower.response.group.ViewGroupRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class GroupRoleRepository {
    @Autowired
    private GroupJpaRepository groupJpaRepository;

    public GroupEntity update(Integer groupRoleId, String groupName, Integer parentId, Float demarcation) {
        GroupEntity entity = new GroupEntity();
        entity.setId(groupRoleId);
        entity.setGroupName(groupName);
        entity.setParentId(parentId);
        entity.setDemarcationAvailable(demarcation);
        return groupJpaRepository.save(entity);
    }

    public void delete(Integer groupId) {
        groupJpaRepository.deleteByGroupName(groupId);
    }

    public GroupEntity save(GroupRoleRequest groupRoleRequest, Integer parentID) {
        GroupEntity entity = new GroupEntity();
        entity.setGroupName(groupRoleRequest.getGroupName());
        entity.setParentId(parentID);
        entity.setDemarcationAvailable(groupRoleRequest.getDemarcation());
        entity.setSort(groupJpaRepository.getMaxSort()+1);
        return groupJpaRepository.save(entity);
    }

    public GroupEntity save(GroupEntity groupEntity) {
        return groupJpaRepository.save(groupEntity);
    }

    public List<String> getName() {
        return groupJpaRepository.getAllByGroupName();
    }

    public Optional<GroupEntity> findById(Integer groupRoleId) {
        return groupJpaRepository.findById(groupRoleId);
    }

    public List<GroupRoleDto> searchAll() {
        return groupJpaRepository.findAllRole();
    }

    public List<ViewAllDto> searchAllView(String reportDate) {
        return groupJpaRepository.findAllViewRole(reportDate);
    }


    public List<ViewDetailGroupResponse> getDetails(String reportDate) {
        return groupJpaRepository.getDetail(reportDate);
    }

    public List<ViewDetailGroupResponse> getDetailParent() {
        return groupJpaRepository.getDetailParent();
    }

    public List<Integer> getParentId() {
        return groupJpaRepository.getAllParentId();
    }

    public List<GroupEntity> findAllByParentId(int parentId) {
        return groupJpaRepository.findAllByParentId(parentId);
    }

    public List<GroupEntity> findAllByParentIdNotNull() {
        return groupJpaRepository.findAllByParentIdIsNull();
    }

    public Object getDemarcationRes(Integer groupId) {
        Optional<GroupEntity> groupEntity = groupJpaRepository.findById(groupId);
        if (groupEntity.isPresent()) {
            return new SuccessResponse<>(HttpStatus.OK.value(), "success", new GroupRoleDemarcationRes(groupEntity.get()));
        }
        return new SuccessResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "group id is not exits", HttpStatus.INTERNAL_SERVER_ERROR.name());
    }

    public List<GroupEntity> findByGroupNameIn(List<String> groupName) {
        return groupJpaRepository.findByGroupNameIn(groupName);
    }
    public Optional<GroupEntity> findByGroupName(String groupName) {
        return groupJpaRepository.findByGroupName(groupName);
    }

    public Optional<GroupEntity> findByGroupNameAndParentId(String groupName, Integer parentId) {
        return groupJpaRepository.findByGroupNameAndParentId(groupName, parentId);
    }

    public List<ViewGroupRoot> getViewRoot() {
        return groupJpaRepository.getViewGroup();
    }
}
