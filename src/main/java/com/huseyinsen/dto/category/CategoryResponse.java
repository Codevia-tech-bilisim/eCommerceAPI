package com.huseyinsen.dto.category;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private boolean active;
    private Long parentId;
    private List<CategoryResponse> children;
}