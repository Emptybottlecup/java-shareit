package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateItemInformation {

    private String name;
    @Size(max = 200)
    private String description;
    private Boolean available;

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasDescription() {
        return description != null && !description.isBlank();
    }

    public boolean hasAvailable() {
        return available != null;
    }

}
