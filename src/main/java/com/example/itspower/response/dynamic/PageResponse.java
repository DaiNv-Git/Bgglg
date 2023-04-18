package com.example.itspower.response.dynamic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T>{
    private List<T> content;
    private Integer totalPages;
    private Long totalElements;
    private Integer currentPage;
    private Integer pageSize;

    public PageResponse(Page<T> page,Long totalElements){
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements =totalElements;
        this.currentPage = page.getNumber() + 1;
        this.pageSize = page.getPageable().getPageSize();
    }
}
