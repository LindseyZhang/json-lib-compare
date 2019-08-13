package com.zlp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
  String name;
  BookType type;
  Double price;
  boolean isPopular;
  boolean hot;
  String optional;

  public String getExtra() {
    return "extra";
  }
}
