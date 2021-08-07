package com.github.heliommsfilho.foodapi.exceptionhandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.github.heliommsfilho.foodapi.domain.exception.EntidadeEmUsoException;
import com.github.heliommsfilho.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.github.heliommsfilho.foodapi.domain.exception.NegocioException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final String MSG_ERRO_INTERNO_SERVIDOR = "Erro interno do servidor. Caso o problema persista, contacte o administrador do sistema";
    
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleNotCaughtException(Exception ex, WebRequest request) {
        Rfc7807 problem = createProblemBuilder(HttpStatus.INTERNAL_SERVER_ERROR, ProblemType.ERRO_INTERNO_SERVIDOR, MSG_ERRO_INTERNO_SERVIDOR)
                            .userMessage(MSG_ERRO_INTERNO_SERVIDOR).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e, WebRequest webRequest) {
        Rfc7807 problem = createProblemBuilder(HttpStatus.NOT_FOUND, ProblemType.RECURSO_NAO_ENCONTRADO, e.getMessage())
                            .userMessage(MSG_ERRO_INTERNO_SERVIDOR).build();

        return handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocioException(NegocioException e, WebRequest webRequest) {
        Rfc7807 problem = createProblemBuilder(HttpStatus.BAD_REQUEST, ProblemType.ERRO_NEGOCIO, e.getMessage())
                            .userMessage(e.getMessage()).build();

        return handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException e, WebRequest webRequest) {
        Rfc7807 problem = createProblemBuilder(HttpStatus.CONFLICT, ProblemType.ENTIDADE_EM_USO, e.getMessage())
                            .userMessage(e.getMessage()).build();

        return handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.CONFLICT, webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handlerMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        }

        if (rootCause instanceof UnrecognizedPropertyException) {
            return handleUnrecognizedPropertyException((UnrecognizedPropertyException) rootCause, headers, status, request);
        }

        String detail = "Corpo de requisição inválido. Verifique a sintaxe da requisição.";
        Rfc7807 problem = createProblemBuilder(status, ProblemType.MENSAGEM_INVALIDA, detail)
                            .userMessage(MSG_ERRO_INTERNO_SERVIDOR).build();

        return super.handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String detail = String.format("O recurso '%s' não existe", ex.getRequestURL());
        Rfc7807 problem = createProblemBuilder(status, ProblemType.RECURSO_NAO_ENCONTRADO, detail)
                            .userMessage(MSG_ERRO_INTERNO_SERVIDOR).build();

        return super.handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final String detail = "Um ou mais campos estão inválidos. Preencha os dados corretamente e tente novamente";

        final BindingResult bindingResult = ex.getBindingResult();
        List<Rfc7807.Field> problemFields = bindingResult.getFieldErrors().stream()
                                                            .map(fe -> {
                                                                final String message = messageSource.getMessage(fe, LocaleContextHolder.getLocale());
                                                                return Rfc7807.Field.builder()
                                                                        .name(fe.getField())
                                                                        .userMessage(message)
                                                                        .build();
                                                            })
                                                            .collect(Collectors.toList());

        final Rfc7807 problem = createProblemBuilder(status, ProblemType.DADOS_INVALIDOS, detail)
                .userMessage(detail)
                .fields(problemFields)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);

    }

    private  ResponseEntity<Object> handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String requiredType = Objects.nonNull(ex.getRequiredType()) ? ex.getRequiredType().getSimpleName() : "Unknown Type";
        String detail = String.format("O parâmetro de URL '%s' recebeu o valor inváliod '%s'. Informe um valor compatível com o tipo '%s'",
                                      ex.getName(), ex.getValue(), requiredType);

        Rfc7807 problem = createProblemBuilder(status, ProblemType.PARAMETRO_INVALIDO, detail).userMessage(MSG_ERRO_INTERNO_SERVIDOR).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleUnrecognizedPropertyException(PropertyBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String detail = String.format("Requisição inválida. A propriedade '%s' não existe.", referenceToStringPath(ex.getPath()));
        Rfc7807 problem = createProblemBuilder(status, ProblemType.MENSAGEM_INVALIDA, detail).userMessage(MSG_ERRO_INTERNO_SERVIDOR).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String detail = String.format("A propriedade '%s' recebeu o valor '%s' que é de um tipo incompatível. Informe um valor compatível com o tipo '%s'",
                                      referenceToStringPath(ex.getPath()), ex.getValue(), ex.getTargetType().getSimpleName());

        Rfc7807 problem = createProblemBuilder(status, ProblemType.MENSAGEM_INVALIDA, detail).userMessage(MSG_ERRO_INTERNO_SERVIDOR).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (Objects.isNull(body)){
            body = Rfc7807.builder()
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .build();
        } else if (body instanceof String) {
            body = Rfc7807.builder()
                    .title((String) body)
                    .status(status.value())
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Rfc7807.Rfc7807Builder createProblemBuilder(HttpStatus httpStatus, ProblemType problemType, String detail) {
        return Rfc7807.builder()
                .status(httpStatus.value())
                .type(problemType.getPath())
                .title(problemType.getTitle())
                .detail(detail)
                .timestamp(LocalDateTime.now());
    }

    private static String referenceToStringPath(List<JsonMappingException.Reference> referenceList) {
        return referenceList.stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("."));
    }
}
