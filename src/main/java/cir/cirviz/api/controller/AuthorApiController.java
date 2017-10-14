package cir.cirviz.api.controller;

import cir.cirviz.api.service.AuthorService;
import cir.cirviz.api.service.PaperService;
import cir.cirviz.api.util.NotFoundException;
import cir.cirviz.data.model.Author;
import cir.cirviz.data.model.Paper;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorApiController implements AuthorApi {

  private final AuthorService authorService;
  private final PaperService paperService;

  @Autowired
  public AuthorApiController(AuthorService authorService,
      PaperService paperService) {
    this.authorService = authorService;
    this.paperService = paperService;
  }

  @GetMapping("/api/authors")
  @Override
  public List<Author> getAuthors(
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "orderBy", required = false, defaultValue = "name") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Author> authors = authorService.getAuthors().stream();
    if (name != null && !name.isEmpty()) {
      authors = authors
          .filter(author -> author.getName().toLowerCase().contains(name.toLowerCase()))
          .distinct();
    }
    switch (orderBy) {
      case "name":
        authors = authors.sorted(Comparator.comparing(Author::getName));
      case "paper":
        authors = authors.sorted((a1, a2) -> {
          int papers1 = authorService.getPapersByAuthor(a1).size();
          int papers2 = authorService.getPapersByAuthor(a2).size();
          return papers1 - papers2;
        });
    }
    if (!asc) {
      authors = authors.sorted(Collections.reverseOrder());
    }
    authors = authors.limit(limit);
    return authors.collect(Collectors.toList());
  }

  @GetMapping("/api/authors/{id}")
  @Override
  public Author getAuthorById(@PathVariable(name = "id") String authorId) {
    Author author = authorService.getAuthorById(authorId);
    if (author == null) {
      throw new NotFoundException("Author: " + authorId + " is not found");
    }
    return author;
  }

  @GetMapping("/api/authors/{id}/papers")
  @Override
  public List<Paper> getPapersByAuthor(@PathVariable(name = "id") String authorId) {
    return authorService.getPapersByAuthorId(authorId);
  }

  @GetMapping("/api/authors/{id}/years")
  @Override
  public List<Integer> getYearsOfPapersByAuthor(@PathVariable(name = "id") String authorId) {
    return authorService.getPapersByAuthorId(authorId).stream()
        .map(Paper::getYear)
        .distinct()
        .collect(Collectors.toList());
  }

  @GetMapping("/api/authors/{id}/keyPhrases")
  @Override
  public List<String> getKeyPhrasesOfPapersByAuthor(@PathVariable(name = "id") String authorId) {
    return authorService.getPapersByAuthorId(authorId).stream()
        .flatMap(paper -> paper.getKeyPhrases().stream())
        .distinct()
        .collect(Collectors.toList());
  }

  @GetMapping("/api/authors/{id}/venues")
  @Override
  public List<String> getVenuesOfPapersByAuthor(String authorId) {
    return authorService.getPapersByAuthorId(authorId).stream()
        .map(Paper::getVenue)
        .distinct()
        .collect(Collectors.toList());
  }

  @GetMapping("/api/authors/{id}/inCitations")
  @Override
  public List<Paper> getCitedPapersWrittenByAuthor(@PathVariable(name = "id") String authorId) {
    return authorService.getPapersByAuthorId(authorId).stream()
        .flatMap(paper -> paperService.getInCitationsByPaper(paper).stream())
        .distinct()
        .collect(Collectors.toList());
  }

  @GetMapping("/api/authors/{id}/outCitations")
  @Override
  public List<Paper> getPapersCitedByAuthor(@PathVariable(name = "id") String authorId) {
    return authorService.getPapersByAuthorId(authorId).stream()
        .flatMap(paper -> paperService.getOutCitationsByPaper(paper).stream())
        .distinct()
        .collect(Collectors.toList());
  }
}
