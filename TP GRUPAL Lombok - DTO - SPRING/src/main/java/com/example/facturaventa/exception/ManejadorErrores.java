package com.example.facturaventa.exception;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import jakarta.validation.ConstraintViolationException;
import java.util.Map;

@RestControllerAdvice
public class ManejadorErrores {
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String,String>> noEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error",ex.getMessage()));
    }
    @ExceptionHandler({ReglaNegocioException.class, ConstraintViolationException.class,
        MethodArgumentTypeMismatchException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String,String>> datosIncorrectos(Exception ex) {
        return ResponseEntity.badRequest().body(Map.of("error",ex.getMessage()==null?"Petición inválida":ex.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> validacion(MethodArgumentNotValidException ex) {
        String mensaje=ex.getBindingResult().getFieldErrors().stream()
            .findFirst().map(e->e.getField()+": "+e.getDefaultMessage()).orElse("Datos inválidos");
        return ResponseEntity.badRequest().body(Map.of("error",mensaje));
    }
}
