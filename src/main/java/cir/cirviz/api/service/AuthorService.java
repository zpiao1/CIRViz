package cir.cirviz.api.service;

import cir.cirviz.data.entity.Author;
import cir.cirviz.data.entity.Paper;
import java.util.List;

public interface AuthorService {

  List<Author> getAuthors();

  Author getAuthorById(String authorId);

  List<Paper> getPapersByAuthor(Author author);

  List<Paper> getPapersByAuthorId(String authorId);
}
