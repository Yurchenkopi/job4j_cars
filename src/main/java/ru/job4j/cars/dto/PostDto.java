package ru.job4j.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PostDto {
    private int id;

    private String description;

}
