package cir.cirviz.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Paper {

  private static Logger logger = LoggerFactory.getLogger(Paper.class);

  @JsonProperty("authors")
  private List<Author> authors = new ArrayList<>();
  @JsonProperty("id")
  private String id;
  @JsonProperty("inCitations")
  private List<String> inCitations = new ArrayList<>();
  @JsonProperty("keyPhrases")
  private List<String> keyPhrases = new ArrayList<>();
  @JsonProperty("outCitations")
  private List<String> outCitations = new ArrayList<>();
  @JsonProperty("paperAbstract")
  private String paperAbstract;
  @JsonProperty("pdfUrls")
  private List<String> pdfUrls = new ArrayList<>();
  @JsonProperty("s2Url")
  private String s2Url;
  @JsonProperty("title")
  private String title;
  @JsonProperty("venue")
  private String venue;
  @JsonProperty("year")
  private int year;

  protected Paper() {
  }

  public Paper(List<Author> authors, String id, List<String> inCitations, List<String> keyPhrases,
      List<String> outCitations, String paperAbstract, List<String> pdfUrls, String s2Url,
      String title, String venue, int year) {
    this.authors = authors;
    this.id = id;
    this.inCitations = inCitations;
    this.keyPhrases = keyPhrases;
    this.outCitations = outCitations;
    this.paperAbstract = paperAbstract;
    this.pdfUrls = pdfUrls;
    this.s2Url = s2Url;
    this.title = title;
    this.venue = venue;
    this.year = year;
  }

  @JsonProperty("authors")
  public List<Author> getAuthors() {
    return authors;
  }

  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("inCitations")
  public List<String> getInCitations() {
    return inCitations;
  }

  @JsonProperty("keyPhrases")
  public List<String> getKeyPhrases() {
    return keyPhrases;
  }

  @JsonProperty("outCitations")
  public List<String> getOutCitations() {
    return outCitations;
  }

  @JsonProperty("paperAbstract")
  public String getPaperAbstract() {
    return paperAbstract;
  }

  @JsonProperty("pdfUrls")
  public List<String> getPdfUrls() {
    return pdfUrls;
  }

  @JsonProperty("s2Url")
  public String getS2Url() {
    return s2Url;
  }

  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  @JsonProperty("venue")
  public String getVenue() {
    return venue;
  }

  @JsonProperty("year")
  public int getYear() {
    return year;
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
}