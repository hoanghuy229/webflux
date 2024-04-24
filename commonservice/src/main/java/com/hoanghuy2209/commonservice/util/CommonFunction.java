package com.hoanghuy2209.commonservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoanghuy2209.commonservice.common.ValidateException;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@NoArgsConstructor
public class CommonFunction {

    @SneakyThrows
    public static void jsonValidate(InputStream inputStream,String json) throws JsonProcessingException {
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(inputStream);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        Set<ValidationMessage> errors = schema.validate(jsonNode);
        Map<String,String> stringSetMap = new HashMap<>();
        for(ValidationMessage error: errors){
            if(stringSetMap.containsKey(formatStringValidate(String.valueOf(error.getEvaluationPath())))){
                String message = stringSetMap.get(formatStringValidate(String.valueOf(error.getEvaluationPath())));
                stringSetMap.put(formatStringValidate(String.valueOf(error.getEvaluationPath())),message + ", "+formatStringValidate(error.getMessage()));
            }else{
                stringSetMap.put(formatStringValidate(String.valueOf(error.getEvaluationPath())),formatStringValidate(error.getMessage()));
            }
        }
        if(!errors.isEmpty()){
            throw new ValidateException("RQ01",stringSetMap, HttpStatus.BAD_REQUEST);
        }
    }

    public static String formatStringValidate(String message){
        return message.replaceAll("\\$.","");
    }
}
