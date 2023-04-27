package com.example.itspower.service.impl;

import com.example.itspower.model.entity.GroupEntity;
import com.example.itspower.model.resultset.RootNameDto;
import com.example.itspower.model.resultset.ViewAllDto;
import com.example.itspower.repository.GroupRoleRepository;
import com.example.itspower.repository.ReportRepository;
import com.example.itspower.repository.repositoryjpa.EmpTerminationContractRepository;
import com.example.itspower.repository.repositoryjpa.GroupJpaRepository;
import com.example.itspower.repository.repositoryjpa.ReportJpaRepository;
import com.example.itspower.response.export.EmployeeExportExcelContractEnd;
import com.example.itspower.response.export.ExportExcelDtoReport;
import com.example.itspower.response.export.ExportExcelEmpRest;
import com.example.itspower.response.group.GroupRoleResponse;
import com.example.itspower.response.group.ViewDetailGroups;
import com.example.itspower.response.view.ListNameRestResponse;
import com.example.itspower.response.view.ReasonResponse;
import com.example.itspower.response.view.RestObjectResponse;
import com.example.itspower.service.ViewDetailService;
import com.example.itspower.service.exportexcel.ExportExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ViewDetailSerivceImpl implements ViewDetailService {
    @Autowired
    ReportJpaRepository repository;
    @Autowired
    GroupJpaRepository groupJpaRepository;
    @Autowired
    private GroupRoleRepository groupRoleRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private EmpTerminationContractRepository empTerminationContractRepository;
    @Autowired
    private ExportExcel exportExcel;
    public static DecimalFormat decimalFormat = new DecimalFormat("#.#");

    @Override
    public List<ViewDetailGroups> searchAllView(String reportDate) {
        List<ReasonResponse> reasonResponseList = groupJpaRepository.getReasonResponse(reportDate);
        List<ListNameRestResponse> listNameReasonList = groupJpaRepository.getListNameReason(reportDate);
        List<RootNameDto> idRootList = groupJpaRepository.getAllRoot();
        List<ViewAllDto> viewAllDtoList = groupRoleRepository.searchAllView(reportDate);
        List<ViewAllDto> response = getLogicParent(viewAllDtoList, idRootList);

        for (ViewAllDto viewAllDto : viewAllDtoList) {
            List listReason = reasonResponseList.stream()
                    .filter(j -> j.getGroupId().equals(viewAllDto.getGroupId()))
                    .collect(Collectors.toList());
            List listNameReason = listNameReasonList.stream()
                    .filter(j -> j.getGroupId().equals(viewAllDto.getGroupId()))
                    .collect(Collectors.toList());
            viewAllDto.setRestObjectResponse(new RestObjectResponse(viewAllDto.getRestNum(), listReason, listNameReason));
        }

        int officeId = response.stream()
                .filter(i -> i.getGroupName().equalsIgnoreCase("văn phòng"))
                .findFirst()
                .map(ViewAllDto::getGroupId)
                .orElse(0);

        List<ViewDetailGroups> viewDetailsRes = response.stream()
                .map(i -> new ViewDetailGroups(i, officeId))
                .collect(Collectors.toList());

        int studentSum = viewAllDtoList.stream()
                .collect(Collectors.summingInt(ViewAllDto::getStudentNum));

        int partTimeToMaySum = viewAllDtoList.stream()
                .filter(i -> i.getGroupName().trim().equalsIgnoreCase("Tổ may"))
                .mapToInt(ViewAllDto::getPartTimeNum)
                .findFirst()
                .orElse(0);

        int partTimeDonViLeSum = viewAllDtoList.stream()
                .filter(i -> i.getGroupName().trim().equalsIgnoreCase("Đơn vị lẻ"))
                .mapToInt(ViewAllDto::getPartTimeNum)
                .findFirst()
                .orElse(0);
        ViewDetailGroups studentNangsuat =
                new ViewDetailGroups(new ViewAllDto(-1, 0, "Học sinh chưa báo năng suất", studentSum, 0
                        , 0, 0, 0, 0, 0, 0, 0f, 0f, 0f), 0);
        ViewDetailGroups thoiVuToMay =
                new ViewDetailGroups(new ViewAllDto(-2, 0, "Thời vụ tổ may", partTimeToMaySum, partTimeDonViLeSum
                        , 0, 0, 0, 0, 0, 0, 0f, 0f, 0f), 0);
        ViewDetailGroups thoiVuDonViLe = new ViewDetailGroups(new ViewAllDto(-3, 0, "Thời vụ đơn vị lẻ ", partTimeToMaySum, partTimeDonViLeSum
                , 0, 0, 0, 0, 0, 0, 0f, 0f, 0f), 0);

        List<ViewDetailGroups> res = children(viewDetailsRes);
        res.add(studentNangsuat);
        res.add(thoiVuToMay);
        res.add(thoiVuDonViLe);
        return res;
    }

    private List<GroupRoleResponse> getDetailsReport(String reportDate) {
        List<ViewAllDto> mapReport = groupRoleRepository.searchAllView(reportDate);
        List<GroupRoleResponse> mapData = new ArrayList<>();
        for (ViewAllDto mapChildren : mapReport) {
            mapData.add(new GroupRoleResponse(mapChildren));
        }
        Map<Integer, List<GroupRoleResponse>> parentIdToChildren =
                mapData.stream().collect(Collectors.groupingBy(GroupRoleResponse::getParentId));
        mapData.forEach(p -> p.setChildren(parentIdToChildren.get(p.getValue())));
        return parentIdToChildren.get(0);
    }

    public void removeGroupRoleById(List<GroupRoleResponse> list) {
        List<String> groupName = Arrays.asList("Đơn vị lẻ");
        List<GroupEntity> entities = groupRoleRepository.findByGroupNameIn(groupName);
        for (GroupRoleResponse groupRole : list) {
            for (GroupEntity entity : entities) {
                if (groupRole.getValue() == entity.getId()) {
                    list.remove(groupRole);
                }
                if (groupRole.getChildren() != null) {
                    removeGroupRoleById(groupRole.getChildren());
                }
            }
        }

    }

    @Override
    public List<Integer> searchDvl(String reportDate) {

        List<GroupRoleResponse> a = getDetailsReport(reportDate);
        List<GroupRoleResponse> responses = a.stream()
                .filter(i -> i.getLabel().equalsIgnoreCase("Đơn vị lẻ"))
                .collect(Collectors.toList());
            List<Integer> key = getAllValues(responses, 0);
        return key;
    }

    public List<Integer> getAllValues(List<GroupRoleResponse> list, int parentId) {
        List<Integer> values = new ArrayList<>();
        for (GroupRoleResponse item : list) {
            if (item.getParentId() == parentId) {
                values.add(item.getValue());
                values.addAll(getAllValues(list, item.getValue()));
            }
        }
        return values;
    }

    List<ViewAllDto> getLogicParent(List<ViewAllDto> viewAllDtoList, List<RootNameDto> getIdRoot) {
        Float totalLaborProductivity = (float) viewAllDtoList.stream().mapToInt(ViewAllDto::getLaborProductivity).sum();
        Float totalRatioOfOfficeAndDonvile = 0.0f;
        for (RootNameDto id : getIdRoot) {
            List<ViewAllDto> parent = viewAllDtoList.stream()
                    .filter(i -> i.getGroupId() == id.getId())
                    .collect(Collectors.toList());
            List<ViewAllDto> child = viewAllDtoList.stream()
                    .filter(i -> i.getGroupParentId() == id.getId() || i.getGroupId() == id.getId())
                    .collect(Collectors.toList());
            Integer groupID = parent.get(0).getGroupId();
            Integer groupParentId = parent.get(0).getGroupParentId();
            String groupName = parent.get(0).getGroupName();
            Integer reportDemarcation = child.stream().map(i -> i.getReportDemarcation()).mapToInt(Integer::intValue).sum();

            Float laborProductivity2 = Float.valueOf(String.valueOf(child.stream().map(i ->
                    i.getLaborProductivity()).mapToInt(Integer::intValue).sum()));
            Integer laborProductivity1 = child.stream().map(i ->
                    i.getLaborProductivity()).mapToInt(Integer::intValue).sum();
            int partTimeNumber = child.stream()
                    .mapToInt(ViewAllDto::getPartTimeNum)
                    .sum();
            int studentNum = child.stream()
                    .mapToInt(ViewAllDto::getStudentNum)
                    .sum();
            int restNum = child.stream()
                    .mapToInt(ViewAllDto::getRestNum)
                    .sum();
            int riceCus = child.stream()
                    .mapToInt(ViewAllDto::getRiceCus)
                    .sum();
            int riceEmp = child.stream()
                    .mapToInt(ViewAllDto::getRiceEmp)
                    .sum();
            int riceVip = child.stream()
                    .mapToInt(ViewAllDto::getRiceVip)
                    .sum();

            Float ratio = (laborProductivity2 / totalLaborProductivity) * 100;
            ratio = Float.valueOf(decimalFormat.format(ratio));
            if (ratio.isNaN() == true) {
                ratio = 0.0f;
            }
            viewAllDtoList.removeIf(i -> i.getGroupId() == id.getId());
            viewAllDtoList.add(new ViewAllDto(groupID, groupParentId, groupName, reportDemarcation, laborProductivity1
                    , partTimeNumber, restNum, studentNum, riceCus, riceEmp, riceVip, ratio, totalLaborProductivity, totalRatioOfOfficeAndDonvile));
        }

        float officeLaborProductivity = viewAllDtoList.stream()
                .filter(i -> i.getGroupName().equalsIgnoreCase("văn phòng"))
                .mapToInt(ViewAllDto::getLaborProductivity)
                .sum();
        float donViLeLaborProductivity = viewAllDtoList.stream()
                .filter(i -> i.getGroupName().equalsIgnoreCase("đơn vị lẻ"))
                .mapToInt(ViewAllDto::getLaborProductivity)
                .sum();
        if (officeLaborProductivity > 0 && donViLeLaborProductivity > 0) {
            float officeRatio = officeLaborProductivity / totalLaborProductivity * 100;
            float donViLeRatio = donViLeLaborProductivity / totalLaborProductivity * 100;
            totalRatioOfOfficeAndDonvile = officeRatio + donViLeRatio;
            totalRatioOfOfficeAndDonvile = Float.parseFloat(decimalFormat.format(totalRatioOfOfficeAndDonvile));
        }
        for (ViewAllDto viewAllDto : viewAllDtoList) {
            if (viewAllDto.getGroupName().equalsIgnoreCase("văn phòng")
                    || viewAllDto.getGroupName().equalsIgnoreCase("Đơn vị lẻ")) {
                viewAllDto.setTotalRatioOfOfficeAndDonvile(totalRatioOfOfficeAndDonvile);
            }
            float laborProductivity2 = viewAllDto.getLaborProductivity();
            float ratio = (laborProductivity2 / totalLaborProductivity) * 100;
            ratio = Float.parseFloat(decimalFormat.format(ratio));
            if (Float.isNaN(ratio)) {
                ratio = 0.0f;
            }
            viewAllDto.setRatio(ratio);
        }
        return viewAllDtoList;
    }

    List<ViewDetailGroups> children(List<ViewDetailGroups> viewDetailsRes) {
        Map<Integer, List<ViewDetailGroups>> parentIdToChildren = viewDetailsRes.stream()
                .collect(Collectors.groupingBy(ViewDetailGroups::getParentId));
        viewDetailsRes.forEach(p -> p.setChildren(parentIdToChildren.get(p.getKey())));
        return parentIdToChildren.get(0);
    }

    public byte[] exportExcel(String reportDate) throws IOException {
        List<ExportExcelDtoReport> reportExcel = reportRepository.findByReportExcel(reportDate);
        List<EmployeeExportExcelContractEnd> employeeExportExcelContractEnds = empTerminationContractRepository.findByEmployee(reportDate);
        List<ExportExcelEmpRest> exportExcelEmpRests = reportRepository.findByReportExcelEmpRest(reportDate);
        exportExcel.initializeData(reportExcel, employeeExportExcelContractEnds, exportExcelEmpRests);
        return exportExcel.export();
    }
}
