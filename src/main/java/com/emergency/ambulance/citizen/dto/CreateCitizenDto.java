package com.emergency.ambulance.citizen.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCitizenDto {

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Mobile number cannot be empty")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Mobile number must contain 10 to 15 digits")
    private String mobileNumber;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "Adhar number cannot be empty")
    @Size(max = 20, message = "Adhar number cannot exceed 20 characters")
    private String adharNumber;

    @NotBlank(message = "PAN number cannot be empty")
    @Size(max = 20, message = "PAN number cannot exceed 20 characters")
    private String panNumber;

    @NotBlank(message = "Blood group cannot be empty")
    @Size(max = 5, message = "Blood group cannot exceed 5 characters")
    private String bloodGroup;

    @NotBlank(message = "Address cannot be empty")
    @Size(max = 300, message = "Address cannot exceed 300 characters")
    private String address;

    @NotBlank(message = "Password cannot be empty")
    @Size(max = 255, message = "Password cannot exceed 255 characters")
    private String password;
}
