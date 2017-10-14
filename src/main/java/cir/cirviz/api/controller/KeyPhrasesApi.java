package cir.cirviz.api.controller;

import cir.cirviz.data.model.Author;
import cir.cirviz.data.model.Paper;
import java.util.List;

public interface KeyPhrasesApi {

  List<String> getKeyPhrases();

  String getKeyPhrase(String keyPhrase);

  List<Paper> getPapersByKeyPhrase(String keyPhrase);

  List<Author> getAuthorsByKeyPhrase(String keyPhrase);

  List<Integer> getYearsByKeyPhrase(String keyPhrase);

  List<Paper> getCitationsByKeyPhrase(String keyPhrase);

  List<Paper> getPapersCitingPapersWithKeyPhrase(String keyPhrase);
}
