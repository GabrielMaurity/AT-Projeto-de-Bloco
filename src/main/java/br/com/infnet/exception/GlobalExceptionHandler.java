package br.com.infnet.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Captura erros de negócio previstos
    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException ex, Model model) {
        model.addAttribute("errorMessage", "Erro de Regra: " + ex.getMessage());
        return "error-page"; // Página amigável
    }

    // Captura erros inesperados (Fail Gracefully)
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "Ocorreu um erro inesperado. Nossa equipe foi notificada.");
        model.addAttribute("technicalDetails", ex.getMessage()); // Ocultar em produção real
        return "error-page";
    }
}