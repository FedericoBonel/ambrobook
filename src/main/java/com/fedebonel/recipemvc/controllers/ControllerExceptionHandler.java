package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Handles exceptions application wide
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    // Todo find replacement for this

//    /**
//     * Handles Not Found Exceptions
//     */
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(NotFoundException.class)
//    public ModelAndView notFound(Exception exception) {
//        log.debug("Handling not found exception in Recipe Controller");
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("title", "404 - Not found!");
//        modelAndView.addObject("error", exception);
//        modelAndView.setViewName("error/show");
//        return modelAndView;
//    }
//
//    /**
//     * Handles bad requests
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public ModelAndView badRequest(Exception exception) {
//        log.debug("Handling bad request in Recipe Controller");
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("title", "400 - Bad request!");
//        modelAndView.addObject("error", exception);
//        modelAndView.setViewName("error/show");
//        return modelAndView;
//    }
}
