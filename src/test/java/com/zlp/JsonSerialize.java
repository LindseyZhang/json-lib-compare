package com.zlp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

public class JsonSerialize {

  private Book book = new Book("nothing", BookType.CULTURE, 20.5d,
      true, true, null);

  @Test
  public void should_serialize_object() throws JsonProcessingException {
    System.out.println("=========jackson json result========");
    ObjectMapper mapper = new ObjectMapper();
    System.out.println(mapper.writeValueAsString(book));

    System.out.println("=====Gson result=======");
    Gson gson = new Gson();
    System.out.println(gson.toJson(book));

    System.out.println("=====fast json result=======");
    System.out.println(JSON.toJSONString(book));
  }

  @Getter
  @AllArgsConstructor
  class DateCollection {
    Date date;
    Time time;
    LocalDate localDate;
    LocalDateTime localDateTime;
    LocalTime localTime;
  }

  @Test
  public void should_serialize_date() throws JsonProcessingException {
    DateCollection dateCollection = new DateCollection(new Date(2019, 4, 3),
        new Time(2019, 4, 3),
        LocalDate.of(2019, 4, 3),
        LocalDateTime.of(2019, 4, 3, 8, 40, 35, 3),
        LocalTime.of(12, 40, 3, 34));

    System.out.println("=========jackson json result========");
    ObjectMapper mapper = new ObjectMapper();
    System.out.println(mapper.writeValueAsString(dateCollection));

    System.out.println("=====Gson result=======");
    Gson gson = new Gson();
    System.out.println(gson.toJson(dateCollection));

    System.out.println("=====fast json result=======");
    System.out.println(JSON.toJSONString(dateCollection));
  }

  // 修改默认配置
  @Test
  public void fast_json_enable_null_output() {
    final String defaultResult = JSON.toJSONString(book);
    System.out.println("default:" + defaultResult);
    assertFalse(defaultResult.contains("optional"));

    final String allowedNullStr = JSON.toJSONString(book, SerializerFeature.WriteMapNullValue);
    System.out.println("null allow:" + allowedNullStr);
    assertTrue(allowedNullStr.contains("optional"));
  }

  @Test
  public void gson_enable_null_output() {
    Gson gson = new GsonBuilder().serializeNulls().create();
    final String result = gson.toJson(book);
    System.out.println(result);
    assertTrue(result.contains("optional"));
  }

  @Test
  public void jackson_disable_null_output() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
//    final String defaultResult = mapper.writeValueAsString(book);
//    System.out.println("default:" + defaultResult);
//    assertTrue(defaultResult.contains("optional"));

    mapper.setSerializationInclusion(Include.NON_NULL);
    final String disableNullStr = mapper.writeValueAsString(book);
    System.out.println("disable null:" + disableNullStr);
    assertFalse(disableNullStr.contains("optional"));
  }

  @Test
  public void json_simple_json_object_serializer() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", "zhang san");
    jsonObject.put("age", 15);

    assertThat(jsonObject.toJSONString(), is("{\"name\":\"zhang san\",\"age\":15}"));
  }

  @Test
  public void json_simple_json_array_serializer() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", "zhang san");
    jsonObject.put("age", 15);
    JSONArray jsonArray = new JSONArray();
    jsonArray.add(jsonObject);

    assertThat(jsonArray.toJSONString(), is("[{\"name\":\"zhang san\",\"age\":15}]"));
  }

}
