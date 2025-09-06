package coding.challenges.redis.resp.types;

import lombok.NonNull;

public record SimpleString(String value) {


    @Override
    public @NonNull String toString() {
        return value;
    }
}
