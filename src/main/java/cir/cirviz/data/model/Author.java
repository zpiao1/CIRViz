package cir.cirviz.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Author {

  private static Logger logger = LoggerFactory.getLogger(Author.class);
  private static long count = 0L;
  @JsonProperty("ids")
  private List<String> ids = Collections.emptyList();
  @JsonProperty("name")
  private String name;

  protected Author() {
  }

  public Author(List<String> ids, String name) {
    this.ids = ids;
    this.name = name;
  }

  private static synchronized long nextLong() {
    return count++;
  }

  @JsonProperty("ids")
  public List<String> getIds() {
    return ids;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public String getId() {
    if (getIds().size() <= 0) {
      long id = nextLong();
      logger.warn("Author: " + getName() + " has no id, assigning one: a" + id);
      ids.add("a" + id);
    }
    return getIds().get(0);
  }

  @Override
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      logger.warn("Error in writing Author: " + e.getMessage());
    }
    return super.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Author)) {
      return false;
    }
    Author author = (Author) o;
    return Objects.equals(getIds(), author.getIds()) &&
        Objects.equals(getName(), author.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getIds(), getName());
  }
}