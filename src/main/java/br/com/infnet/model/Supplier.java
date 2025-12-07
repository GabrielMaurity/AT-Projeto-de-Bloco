package br.com.infnet.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record Supplier(
        UUID id,
        @NotBlank(message = "O nome da empresa é obrigatório")
        String companyName,
        @NotBlank @Email(message = "Email inválido")
        String email
) {}