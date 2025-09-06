package coding.challenges.redis.resp.types;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;

public record RespArray<T> (List<T> elements) implements Iterable<T> {


    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return elements.spliterator();
    }

    @Override
    public boolean equals(Object o) {
        return switch (o) {
            case RespArray<?> respArray -> Objects.equals(elements, respArray.elements);
            case null, default -> false;
        };
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(elements);
    }
}
