package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.thymeleaf.exceptions.TemplateInputException;

/**
 * Handles exceptions application wide
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Handles Not Found Exceptions
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class, TemplateInputException.class})
    public String notFound(Exception exception, Model model) {
        log.debug("Handling not found exception");

        model.addAttribute("title", "404 - Not found!");
        model.addAttribute("error", exception);
        return "error/show";
    }

    /**
     * Handles bad requests
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String badRequest(Exception exception, Model model) {
        log.debug("Handling bad request");

        model.addAttribute("title", "400 - Bad request!");
        model.addAttribute("error", exception);
        return "error/show";
    }
}
