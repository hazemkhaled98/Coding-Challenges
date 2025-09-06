package coding.challenges.redis.resp.types;


import lombok.NonNull;

public record BulkString(String value) {

    @Override
    public @NonNull String toString() {
        return value;
    }

    public int length() {
        return value.length();
    }
}
