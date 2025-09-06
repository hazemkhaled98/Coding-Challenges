package coding.challenges.redis.resp;


import coding.challenges.redis.resp.types.BulkString;
import coding.challenges.redis.resp.types.RespArray;
import coding.challenges.redis.resp.types.RespException;
import coding.challenges.redis.resp.types.SimpleString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RespDeserializerTest {

    @Autowired
    private RespDeserializer deserializer;

    @Test
    void deserializeSimpleString() {
        Object result = deserializer.deserialize("+OK\r\n");
        assertThat(result).isEqualTo(new SimpleString("OK"));
    }

    @Test
    void deserializeError() {
        Object result = deserializer.deserialize("-ERR something\r\n");
        assertThat(result).isInstanceOf(Exception.class);
        assertThat(((Exception) result).getMessage()).isEqualTo("ERR something");
    }


    @ParameterizedTest(name = "{2}")
    @MethodSource("deserializeIntegers")
    void deserializeInteger(String input, long expected, String description) {
        Object result = deserializer.deserialize(input);
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("deserializeBulkStrings")
    void deserializeBulkString(String input, String expected, String description) {
        Object result = deserializer.deserialize(input);
        assertThat(result).isEqualTo(new BulkString(expected));
    }


    @ParameterizedTest(name = "{2}")
    @MethodSource("deserializeArray")
    void deserializeArray(String input, RespArray<?> expected, String description) {
        Object result = deserializer.deserialize(input);
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> deserializeIntegers() {
        return Stream.of(
                Arguments.of(":123\r\n", 123L, "Positive integer"),
                Arguments.of(":-123\r\n", -123L, "Negative integer")
        );
    }

    private static Stream<Arguments> deserializeBulkStrings() {
        return Stream.of(
                Arguments.of("$7\r\nfoo bar\r\n", "foo bar", "Regular string"),
                Arguments.of("$0\r\n\r\n", "", "Empty String"),
                Arguments.of("$-1\r\n", null, "Null")
        );
    }

    private static Stream<Arguments> deserializeArray() {
        return Stream.of(

                Arguments.of("*0\r\n", new RespArray<>(Collections.emptyList()), "Empty array"),


                Arguments.of("*-1\r\n", new RespArray<>(null), "Null array"),

                Arguments.of(
                        "*1\r\n+OK\r\n",
                        new RespArray<>(List.of(new SimpleString("OK"))),
                        "Single simple string"
                ),
                Arguments.of(
                        "*1\r\n$5\r\nhello\r\n",
                        new RespArray<>(List.of(new BulkString("hello"))),
                        "Single bulk string"
                ),
                Arguments.of(
                        "*1\r\n:42\r\n",
                        new RespArray<>(List.of(42L)),
                        "Single integer"
                ),
                Arguments.of(
                        "*1\r\n-ERR something went wrong\r\n",
                        new RespArray<>(List.of(new RespException("ERR something went wrong"))),
                        "Single error"
                ),


                Arguments.of(
                        "*2\r\n+OK\r\n$5\r\nworld\r\n",
                        new RespArray<>(List.of(new SimpleString("OK"), new BulkString("world"))),
                        "Mixed simple string and bulk string"
                ),
                Arguments.of(
                        "*3\r\n$3\r\nfoo\r\n$3\r\nbar\r\n$3\r\nbaz\r\n",
                        new RespArray<>(List.of(new BulkString("foo"), new BulkString("bar"), new BulkString("baz"))),
                        "Multiple bulk strings"
                ),
                Arguments.of(
                        "*3\r\n:1\r\n:2\r\n:3\r\n",
                        new RespArray<>(List.of(1L, 2L, 3L)),
                        "Multiple integers"
                ),


                Arguments.of(
                        "*4\r\n+SET\r\n$3\r\nkey\r\n$5\r\nvalue\r\n:100\r\n",
                        new RespArray<>(List.of(
                                new SimpleString("SET"),
                                new BulkString("key"),
                                new BulkString("value"),
                                100L
                        )),
                        "Mixed types (SET command simulation)"
                ),


                Arguments.of(
                        "*1\r\n$-1\r\n",
                        new RespArray<>(List.of(new BulkString(null))),
                        "RespArray with null bulk string"
                ),
                Arguments.of(
                        "*1\r\n$0\r\n\r\n",
                        new RespArray<>(List.of(new BulkString(""))),
                        "RespArray with empty bulk string"
                ),
                Arguments.of(
                        "*1\r\n$12\r\nhello\r\nworld\r\n",
                        new RespArray<>(List.of(new BulkString("hello\r\nworld"))),
                        "Bulk string with CRLF in content"
                ),
                Arguments.of(
                        "*1\r\n$15\r\n+OK-ERR:123$5*2\r\n",
                        new RespArray<>(List.of(new BulkString("+OK-ERR:123$5*2"))),
                        "Bulk string with RESP-like prefixes"
                ),

                Arguments.of(
                        "*2\r\n$5\r\nouter\r\n*1\r\n$5\r\ninner\r\n",
                        new RespArray<>(List.of(
                                new BulkString("outer"),
                                new RespArray<>(List.of(new BulkString("inner")))
                        )),
                        "Nested array"
                ),
                Arguments.of(
                        "*2\r\n*2\r\n$1\r\na\r\n$1\r\nb\r\n*2\r\n$1\r\nc\r\n$1\r\nd\r\n",
                        new RespArray<>(List.of(
                                new RespArray<>(List.of(new BulkString("a"), new BulkString("b"))),
                                new RespArray<>(List.of(new BulkString("c"), new BulkString("d")))
                        )),
                        "Multiple nested arrays"
                ),
                Arguments.of(
                        "*1\r\n*-1\r\n",
                        new RespArray<>(List.of(new RespArray<>(null))),
                        "RespArray containing null array"
                ),
                Arguments.of(
                        "*1\r\n*0\r\n",
                        new RespArray<>(List.of(new RespArray<>(Collections.emptyList()))),
                        "RespArray containing empty array"
                ),

                Arguments.of(
                        "*1\r\n:0\r\n",
                        new RespArray<>(List.of(0L)),
                        "Zero integer"
                ),
                Arguments.of(
                        "*1\r\n:-1\r\n",
                        new RespArray<>(List.of(-1L)),
                        "Negative integer"
                )
        );
    }
}
