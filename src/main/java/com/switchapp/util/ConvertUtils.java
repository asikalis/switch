package com.switchapp.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.switchapp.model.Role;
import com.switchapp.model.User;
import com.switchapp.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertUtils {

    private static final Logger logger = LoggerFactory.getLogger(ConvertUtils.class);

    public static ObjectMapper mapper = new ObjectMapper();

    public static String convertObjectToJson(Object obj) {

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        String json = "";

        try {
            if (obj instanceof Object) {
                json = mapper.writeValueAsString(obj);
                // json =
                // mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            } else if (obj instanceof List) {
                List<String> listOfStr = new ArrayList<String>();
                for (Object objeach : (List) obj) {
                    json = mapper.writeValueAsString(obj);
//					json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
                    listOfStr.add(json);
                }
                return listOfStr.toString();
            }
            // json =
            // mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            // log.info("Resulting JSON string from Obejct : \n" + json);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("Error while converting Object to Json ... {} ", e.getLocalizedMessage());
        }

        return json;
    }

    public static UserDto userDtoFromUser(User user) {

        UserDto userDTO = new UserDto();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRoles(
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList())
        );
        return userDTO;
    }
}
