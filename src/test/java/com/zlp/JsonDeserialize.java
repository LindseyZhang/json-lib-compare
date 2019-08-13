package com.zlp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class JsonDeserialize {

  private static final String STANDARD_BOOK_JSON = "{\"name\":\"nothing\",\"type\":\"CULTURE\","
      + "\"price\":20.5,\"hot\":true,\"optional\":null,\"popular\":true}";

  private static final String BOOK_JSON_WITH_EXTRA_FIELD =
      "{\"name\":\"nothing\",\"type\":\"CULTURE\","
          + "\"price\":20.5,\"hot\":true,\"optional\":null,\"popular\":true,\"extra\":\"extra\"}";

  @Test
  public void parse_to_object_when_all_field_match() throws IOException, ParseException {
    parseJsonStringToBook(STANDARD_BOOK_JSON);
  }

  private void parseJsonStringToBook(String jsonStr) throws IOException, ParseException {
    // =========jackson json deserializer========  // UnrecognizedPropertyException
    ObjectMapper mapper = new ObjectMapper();
    final Book jacksonBook = mapper.readValue(jsonStr, Book.class);
    assertBook(jacksonBook);
    assertThat(jacksonBook.isPopular(), is(true));

    // =====Gson deserializer=======
    Gson gson = new Gson();
    final Book gsonBook = gson.fromJson(jsonStr, Book.class);
    assertBook(gsonBook);
    assertThat(gsonBook.isPopular(), is(false));

    // =====fast json deserializer=======
    final Book fastJsonBook = JSON.parseObject(jsonStr, Book.class);      // 依赖 setter 方法
    assertBook(fastJsonBook);
    assertThat(fastJsonBook.isPopular(), is(true));
  }

  @Test
  public void parse_json_string_with_extra_field() throws IOException, ParseException {
    parseJsonStringToBook(BOOK_JSON_WITH_EXTRA_FIELD);
  }

  @Test
  public void parse_standard_json_list() throws IOException, ParseException {
    String jsonStr = "[" + STANDARD_BOOK_JSON + "]";

    // =========jackson json result========
    ObjectMapper mapper = new ObjectMapper();
    final List<Book> jacksonBooks = mapper.readValue(jsonStr, new TypeReference<List<Book>>() {
    });
    assertThat(jacksonBooks.size(), is(1));
    assertBook(jacksonBooks.get(0));

    // =====Gson result=======
    Gson gson = new Gson();
    final List<Book> gsonBooks = gson.fromJson(jsonStr, new TypeToken<List<Book>>() {
    }.getType());
    assertThat(gsonBooks.size(), is(1));
    assertBook(gsonBooks.get(0));

    // =====fast json result=======
    final List<Book> fastJsonBooks = JSON.parseArray(jsonStr, Book.class);
    assertThat(fastJsonBooks.size(), is(1));
    assertBook(fastJsonBooks.get(0));
  }

  private void assertBook(Book gsonBook) {
    assertThat(gsonBook.getName(), is("nothing"));
    assertThat(gsonBook.getType(), is(BookType.CULTURE));
    assertThat(gsonBook.getPrice(), is(20.5));
    assertThat(gsonBook.isHot(), is(true));
    assertThat(gsonBook.getOptional(), nullValue());
  }

  // boolean test
  @Setter
  @Getter
  public static class BooleanObj {

    private Boolean hot;
  }

  @Setter
  @Getter
  public static class IsBooleanObj {

    private Boolean isHot;
  }

  private static String IS_HOT_JSON = "{\"isHot\":true}";
  private static String HOT_JSON = "{\"hot\":true}";

  // isHot to hot
  @Test(expected = UnrecognizedPropertyException.class)
  public void jackson_parse_isHot_to_hot() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    BooleanObj jacksonResult = mapper.readValue(IS_HOT_JSON, BooleanObj.class);
    assertThat(jacksonResult.getHot(), is(true));
  }

  @Test
  public void gson_parse_isHot_to_hot() throws IOException {
    Gson gson = new Gson();
    BooleanObj gsonResult = gson.fromJson(IS_HOT_JSON, BooleanObj.class);
//    assertThat(gsonResult.getHot(), is(true));
    assertThat(gsonResult.getHot(), nullValue());
  }

  @Test
  public void fast_json_parse_isHot_to_hot() {
    final BooleanObj fastJsonResult = JSON.parseObject(IS_HOT_JSON, BooleanObj.class);
    assertThat(fastJsonResult.getHot(), is(true));
  }


  // hot to isHot
  @Test(expected = UnrecognizedPropertyException.class)
  public void jackson_parse_hot_to_isHot() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    IsBooleanObj jacksonResult = mapper.readValue(HOT_JSON, IsBooleanObj.class);
    assertThat(jacksonResult.getIsHot(), is(true));
  }

  @Test
  public void gson_parse_hot_to_isHot() throws IOException {
    Gson gson = new Gson();
    IsBooleanObj gsonResult = gson.fromJson(HOT_JSON, IsBooleanObj.class);
//    assertThat(gsonResult.getIsHot(), is(true));
    assertThat(gsonResult.getIsHot(), nullValue());
  }

  @Test
  public void fast_json_parse_hot_to_isHot() {
    final IsBooleanObj fastJsonResult = JSON.parseObject(HOT_JSON, IsBooleanObj.class);
//    assertThat(fastJsonResult.getIsHot(), is(true));
    assertThat(fastJsonResult.getIsHot(), nullValue());
  }

  // isHot to isHot
  @Test
  public void jackson_parse_isHot_to_isHot() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    IsBooleanObj jacksonResult = mapper.readValue(IS_HOT_JSON, IsBooleanObj.class);
    assertThat(jacksonResult.getIsHot(), is(true));
  }

  @Test
  public void gson_parse_isHot_to_isHot() throws IOException {
    Gson gson = new Gson();
    IsBooleanObj gsonResult = gson.fromJson(IS_HOT_JSON, IsBooleanObj.class);
    assertThat(gsonResult.getIsHot(), is(true));
  }

  @Test
  public void fast_json_parse_isHot_to_isHot() {
    final IsBooleanObj fastJsonResult = JSON.parseObject(IS_HOT_JSON, IsBooleanObj.class);
    assertThat(fastJsonResult.getIsHot(), is(true));
  }

  // hot to hot
  @Test
  public void jackson_parse_hot_to_hot() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    BooleanObj jacksonResult = mapper.readValue(HOT_JSON, BooleanObj.class);
    assertThat(jacksonResult.getHot(), is(true));
  }

  @Test
  public void gson_parse_hot_to_hot() throws IOException {
    Gson gson = new Gson();
    BooleanObj gsonResult = gson.fromJson(HOT_JSON, BooleanObj.class);
    assertThat(gsonResult.getHot(), is(true));
  }

  @Test
  public void fast_json_parse_hot_to_hot() {
    final BooleanObj fastJsonResult = JSON.parseObject(HOT_JSON, BooleanObj.class);
    assertThat(fastJsonResult.getHot(), is(true));
  }

  // date deserilize
  @Getter
  @Setter
  private static class DateObj {
    Date date;
  }

  // consistent with serialized String
  @Test
  public void parse_date() throws IOException {
    String json = "{\"date\": 61514956800000}";

    // jackson
    ObjectMapper mapper = new ObjectMapper();
    DateObj jacksonResult = mapper.readValue(json, DateObj.class);
    assertThat(jacksonResult.getDate(), is(new Date(2019, 4, 3)));

    String GsonStr = "{\"date\": \"May 3, 3919, 12:00:00 AM\"}";
    Gson gson = new Gson();
    DateObj gsonResult = gson.fromJson(GsonStr, DateObj.class);
    assertThat(gsonResult.getDate(), is(new Date(2019, 4, 3)));

    final DateObj fastJsonResult = JSON.parseObject(json, DateObj.class);
    assertThat(fastJsonResult.getDate(), is(new Date(2019, 4, 3)));
  }

  @Setter
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode
  static class DateCollection {
    Date date;
    Time time;
    LocalDate localDate;
    LocalDateTime localDateTime;
    LocalTime localTime;
  }

  @Test
  public void should_serialize_date() throws IOException {
    DateCollection dateCollection = new DateCollection(new Date(2019, 4, 3),
        new Time(2019, 4, 3),
        LocalDate.of(2019, 4, 3),
        LocalDateTime.of(2019, 4, 3, 8, 40, 35, 3),
        LocalTime.of(12, 40, 3, 34)
    );

//    System.out.println("=========jackson json result========");
//    ObjectMapper mapper = new ObjectMapper();
//    final DateCollection jacksonResult = mapper
//        .readValue(mapper.writeValueAsString(dateCollection), DateCollection.class);
//    assertDateCollection(jacksonResult, dateCollection);

    System.out.println("=====Gson result=======");
    Gson gson = new Gson();
    final DateCollection gsonResult = gson
        .fromJson(gson.toJson(dateCollection), DateCollection.class);
    assertDateCollection(gsonResult, dateCollection);

    System.out.println("=====fast json result=======");
    final DateCollection fastJsonResult = JSON
        .parseObject(JSON.toJSONString(dateCollection), DateCollection.class);
    assertDateCollection(fastJsonResult, dateCollection);

  }

  private void assertDateCollection(DateCollection result, DateCollection expected) {
    assertThat(result.getDate(), is(expected.getDate()));
    assertEquals(result.getTime().toString(), expected.getTime().toString()); // 反序列化时均自动将 date 部分设置为当前日期
    assertEquals(result.getLocalDate(), expected.getLocalDate());
    assertEquals(result.getLocalDateTime(), expected.getLocalDateTime());
    assertEquals(result.getLocalTime(), expected.getLocalTime());
  }

  @Test
  public void json_simple_parse_json() throws ParseException {
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject) jsonParser.parse(BOOK_JSON_WITH_EXTRA_FIELD);
    assertThat(jsonObject.get("name"), is("nothing"));
    assertThat(jsonObject.get("type"), is("CULTURE"));
    assertThat(jsonObject.get("price"), is(20.5));
    assertThat(jsonObject.get("hot"), is(true));
    assertThat(jsonObject.get("optional"), nullValue());
    assertThat(jsonObject.get("optional"), nullValue());
  }

}
