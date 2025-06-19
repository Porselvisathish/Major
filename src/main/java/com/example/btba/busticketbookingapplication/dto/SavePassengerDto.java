package com.example.btba.busticketbookingapplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class SavePassengerDto {
    @NotBlank(message = "First name of a patient cannot be blank.")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Name can only contain letters and spaces")
    private String firstName;
    private String lastName;
    private String email;
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be numeric and exactly 10 digits long.")
    private String mobile;
    private int age;
    private String gender;
    private String password;
}
