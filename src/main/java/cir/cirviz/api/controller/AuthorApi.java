package cir.cirviz.api.controller;

import cir.cirviz.data.model.Author;
import cir.cirviz.data.model.Paper;
import java.util.List;

public interface AuthorApi {

  List<Author> getAuthors(
      String name,
      String orderBy,
      boolean asc,
      long limit
  );

  Author getAuthorById(String authorId);

  List<Paper> getPapersByAuthor(String authorId);

  List<Integer> getYearsOfPapersByAuthor(String authorId);

  List<String> getKeyPhrasesOfPapersByAuthor(String authorId);

  List<String> getVenuesOfPapersByAuthor(String authorId);

  List<Paper> getCitedPapersWrittenByAuthor(String authorId);

  List<Paper> getPapersCitedByAuthor(String authorId);
}
