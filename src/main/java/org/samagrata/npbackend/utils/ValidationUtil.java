package org.samagrata.npbackend.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.samagrata.npbackend.entity.BaseEntity;

public class ValidationUtil {

  // For manual validation
  public static ArrayList<Map<String, String>> validateEntity(BaseEntity entity) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    Set<ConstraintViolation<BaseEntity>> violations = validator.validate(entity);

    ArrayList<Map<String, String>> errorsList = new ArrayList<>();

    for (ConstraintViolation<BaseEntity> violation : violations) {
      Map<String, String> errorMap = new HashMap<>();
      String fieldName = null;
      for (Path.Node node : violation.getPropertyPath()) {
        fieldName = node.getName();
      }
      errorMap.put(fieldName, violation.getMessage());
      errorsList.add(errorMap);
    }

    return errorsList;
  }

  public static Map<String, String> validatePassword(String password) {
    Map<String, String> errorMap = new HashMap<>();
    String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,16}$";
    String message = "must be 6-16 characters, include uppercase, lowercase, and a digit, and no spaces";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(password);

    if (!matcher.matches()) {
      errorMap.put("password", message);
    }

    return errorMap;
  }
}
