package by.itacademy.report.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MapParser {

    private static final String PARSING_ERROR = "Map parser failed to get %s from map: %s";
    @Autowired
    private ObjectMapper mapper;

    public <T> T readValue(String key, Map<String, Object> paramsMap, Class<T> clazz) {
        try {
            Object value = paramsMap.get(key);
            if (value == null) {
                throw new IllegalArgumentException(String.format(PARSING_ERROR, key, Messages.IS_NULL.getText()));
            }
            T result = mapper.convertValue(value, clazz);
            if (result == null) {
                throw new IllegalArgumentException(String.format(PARSING_ERROR, key, Messages.IS_NULL.getText()));
            }
            return result;
        } catch (IllegalArgumentException exc) {
            throw new IllegalArgumentException(String.format(PARSING_ERROR, key, Messages.INVALID_FORMAT.getText()), exc);
        }
    }

    public <T> List<T> readList(String key, Map<String, Object> paramsMap, Class<T> clazz) {
        try {
            Object value = paramsMap.get(key);
            if (value == null) {
                throw new IllegalArgumentException(String.format(PARSING_ERROR, key, Messages.IS_NULL.getText()));
            }
            List<T> result = mapper.convertValue(
                    paramsMap.get(key), mapper.getTypeFactory().constructCollectionType(List.class, clazz));
            if (result == null) {
                throw new IllegalArgumentException(String.format(PARSING_ERROR, key, Messages.IS_NULL.getText()));
            }
            return result;
        } catch (IllegalArgumentException exc) {
            throw new IllegalArgumentException(String.format(PARSING_ERROR, key, Messages.INVALID_FORMAT.getText()), exc);
        }
    }
}
