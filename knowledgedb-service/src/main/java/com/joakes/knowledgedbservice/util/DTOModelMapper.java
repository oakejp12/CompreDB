package com.joakes.knowledgedbservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;

/**
 * See: https://auth0.com/blog/automatically-mapping-dto-to-entity-on-spring-boot-apis/
 * <p>
 * RequestResponseBodyMethodProcessor is used to avoid having to write the whole process of converting requests
 * into classes. This class processes and populates @RequestBody parameters (i.e. takes JSON body and transforms
 * on an instance of a class). In our case, we tweak the base class to populate an instance of the DTO instead.
 * Overriding the supportsParameter method allows us to focus on @DTO annotations only instead of @RequestBody annotations.
 * Overriding validateIfApplicable allows us to run bean validations on all DTOs instead of those marked with @Valid/@Validated.
 *
 * In short, this class populates an instance of a DTO, defined in the @DTO annotation, maps the properties of this DTO
 * into an entity, but first it checks if there is an @Id property in the DTO to see if it needs to fetch a pre-existing entity
 */
public class DTOModelMapper extends RequestResponseBodyMethodProcessor {
    private static final ModelMapper mapper = new ModelMapper();

    private EntityManager entityManager; // Inject an EntityManager to be able to query the DB for existing entities

    public DTOModelMapper(ObjectMapper objectMapper, EntityManager entityManager) {
        super(Collections.singletonList(new MappingJackson2HttpMessageConverter(objectMapper)));
        this.entityManager = entityManager;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(DTO.class);
    }

    @Override
    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        binder.validate();
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object dto = super.resolveArgument(parameter, container, webRequest, binderFactory);
        Object id = getEntityId(dto);
        if (id == null) {
            return mapper.map(dto, parameter.getParameterType());
        } else {
            Object persistedObject = entityManager.find(parameter.getParameterType(), id);
            mapper.map(dto, persistedObject);
            return persistedObject;
        }
    }

    @Override
    protected Object readWithMessageConverters(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType) throws IOException, HttpMediaTypeNotSupportedException {
        for (Annotation annotation : parameter.getParameterAnnotations()) {
            DTO dtoType = AnnotationUtils.getAnnotation(annotation, DTO.class);
            if (dtoType != null) {
                return super.readWithMessageConverters(inputMessage, parameter, dtoType.value());
            }
        }
        throw new RuntimeException();
    }

    private Object getEntityId(Object dto) {
        for (Field field : dto.getClass().getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null) {
                try {
                    field.setAccessible(true);
                    return field.get(dto);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
}
