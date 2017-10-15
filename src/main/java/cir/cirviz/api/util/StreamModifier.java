package cir.cirviz.api.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

public class StreamModifier<T> {

  private Map<String, Comparator<T>> comparatorMap = Collections.emptyMap();

  public StreamModifier(Map<String, Comparator<T>> comparatorMap) {
    this.comparatorMap = comparatorMap;
  }

  public Stream<T> modify(Stream<T> stream, String orderBy, boolean asc, long limit) {
    if (orderBy == null || orderBy.isEmpty()) {
      return stream.limit(limit);
    }
    Comparator<T> comparator = comparatorMap.get(orderBy);
    if (comparator != null) {
      stream = stream.sorted(comparator);
    }
    if (!asc) {
      stream = stream.sorted(Collections.reverseOrder());
    }
    return stream.limit(limit);
  }
}
