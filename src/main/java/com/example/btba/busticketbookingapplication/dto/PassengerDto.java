package com.example.btba.busticketbookingapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class PassengerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private int age;
    private String gender;
}
