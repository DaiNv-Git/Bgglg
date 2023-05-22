package com.example.itspower.response.employee;

import lombok.Data;

@Data
public class EmployeeExportExcel {
   private String groupName;
   private String name;
   private String labor;


   public EmployeeExportExcel(String groupName, String name, String labor) {
      this.groupName = groupName;
      this.name = name;
      this.labor = labor;
   }
}
