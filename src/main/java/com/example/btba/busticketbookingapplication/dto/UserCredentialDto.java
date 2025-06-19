package com.example.btba.busticketbookingapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class UserCredentialDto {
    private String username;
    private String password;
}
