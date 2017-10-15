package cir.cirviz.api.controller;

import cir.cirviz.data.entity.Author;
import cir.cirviz.data.entity.Paper;
import java.util.List;

public interface AuthorApi {

  List<Author> getAuthors(
      String name,
      String orderBy,
      boolean asc,
      long limit
  );

  long getAuthorsCount();

  Author getAuthorById(String authorId);

  List<Paper> getPapersByAuthor(
      String authorId,
      String orderBy,
      boolean asc,
      long limit
  );

  long getPapersCountByAuthor(String authorId);

  List<Integer> getYearsOfPapersByAuthor(
      String authorId,
      String orderBy,
      boolean asc,
      long limit
  );

  long getYearsOfPapersCountByAuthor(String authorId);

  List<String> getKeyPhrasesOfPapersByAuthor(
      String authorId,
      String orderBy,
      boolean asc,
      long limit
  );

  long getKeyPhrasesCountOfPapersByAuthor(String authorId);

  List<String> getVenuesOfPapersByAuthor(
      String authorId,
      String orderBy,
      boolean asc,
      long limit
  );

  long getVenuesCountOfPapersByAuthor(String authorId);

  List<Paper> getCitedPapersWrittenByAuthor(
      String authorId,
      String orderBy,
      boolean asc,
      long limit
  );

  long getCitedPapersCountWrittenByAuthor(String authorId);

  List<Paper> getPapersCitedByAuthor(
      String authorId,
      String orderBy,
      boolean asc,
      long limit
  );

  long getPapersCountCitedByAuthor(String authorId);
}
