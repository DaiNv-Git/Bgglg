package com.example.itspower.response.export;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferExcel {
    private String transferDate;

    private String name;

    private String labor;

    private String oldGroup;

    private String newGroup;

    private String statusTransfer = "Đã xác nhận";
}
