package org.ywb.ono.toolkit;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author yuwenbo1
 * @version 1.0.0
 * <p>
 * desc:  json 转换 obj & 格式化工具
 * </p>
 * @date 2019/7/10 15:41
 */
public final class GsonUtils {

    private static final String DEFAULT_DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    private static final String DEFAULT_DATE_FORMATTER = "yyyy-MM-dd";

    private static final String DEFAULT_TIME_FORMATTER = "HH:mm:ss";

    private static final Gson GSON;

    private static final JsonParser JSON_PARSER = new JsonParser();

    private static final Gson PRETTY_GSON;

    private GsonUtils() {
    }

    /**
     * Json美化输出工具
     *
     * @param json json string
     * @return beauty json string
     */
    public static String toPrettyFormat(String json) {
        JsonElement jsonElement = JSON_PARSER.parse(json);
        return PRETTY_GSON.toJson(jsonElement);
    }

    /**
     * 美化json
     *
     * @param object obj
     * @return beauty json string
     */
    public static String toPrettyFormat(Object object) {
        return toPrettyFormat(object2Json(object));
    }

    /**
     * Json转换成对象
     *
     * @param json json string
     * @param <T>  对象类型
     * @return T
     */
    public static <T> T json2Object(String json, Class<T> t) {
        return GSON.fromJson(json, t);
    }

    /**
     * Json转换成对象
     * 该方式适用于泛型内包含泛型，如想转换的返回值为：User<String>
     * 使用方式
     * <code>
     * Type type = new TypeToken<User<String>>(){}.getType();
     * User<String> user = JsonHelper.json2Object(json,type);
     * </code>
     *
     * @param json json
     * @param type type
     * @param <T>  T
     * @return T
     * @since 1.0.1
     */
    public static <T> T json2Object(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    /**
     * 对象转换成Json字符串
     * json分为对象和数组两种类型，需要分别处理
     *
     * @param object obj
     * @return json string
     */
    public static String object2Json(Object object) {
        return GSON.toJson(object);
    }

    /**
     * json转换成JsonElement
     *
     * @param json jsonStr
     * @return {@link JsonElement}
     */
    public static JsonElement json2JsonElement(String json) {
        return JSON_PARSER.parse(json);
    }

    /**
     * json转map
     * value的类型为v
     *
     * @param json json str
     * @param v    map value type
     * @param <V>  map value type
     * @return Map<String, V>
     * @since 1.0.1
     */
    public static <V> Map<String, V> json2Map(String json, Class<V> v) {
        return json2Map(json, String.class, v);
    }

    /**
     * json转Map
     *
     * @param json json str
     * @param k    map key type
     * @param v    map value type
     * @param <K>  map key type
     * @param <V>  map value type
     * @return Map<K, V>
     * @since 1.0.1
     */
    public static <K, V> Map<K, V> json2Map(String json, Class<K> k, Class<V> v) {
        JsonElement jsonElement = json2JsonElement(json);
        assertTrue(jsonElement.isJsonObject(), "json str must be a json object.");
        Type type = new TypeToken<Map<K, V>>() {
        }.getType();
        return GSON.fromJson(json, type);
    }


    /**
     * json转List
     *
     * @param json json str
     * @param e    element type
     * @param <E>  element type
     * @return List<E>
     * @since 1.0.1
     */
    public static <E> List<E> json2List(String json, Class<E> e) {
        JsonElement jsonElement = json2JsonElement(json);
        assertTrue(jsonElement.isJsonArray(), "json str must be a json array.");
        Type type = new TypeToken<List<E>>() {
        }.getType();
        return GSON.fromJson(json, type);
    }

    /**
     * json转Set
     *
     * @param json json str
     * @param e    element type
     * @param <E>  element type
     * @return Set<E>
     * @since 1.0.1
     */
    public static <E> Set<E> json2Set(String json, Class<E> e) {
        JsonElement jsonElement = json2JsonElement(json);
        assertTrue(jsonElement.isJsonArray(), "json str must be a json array.");
        Type type = new TypeToken<Set<E>>() {
        }.getType();
        return GSON.fromJson(json, type);
    }

    static {
        GSON = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (localDateTime, typeOfSrc, context) -> {
                    if (Objects.isNull(localDateTime)) {
                        return new JsonPrimitive("");
                    }
                    return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMATTER)));
                })
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json1, typeOfT, context) -> {
                    String string = json1.getAsJsonPrimitive().getAsString();
                    if (isNullOrEmpty(string)) {
                        return null;
                    }
                    return LocalDateTime.parse(string, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMATTER));
                })
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (localDate, type, jsonSerializationContext) -> {
                    if (Objects.isNull(localDate)) {
                        return new JsonPrimitive("");
                    }
                    return new JsonPrimitive(localDate.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMATTER)));
                })
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) -> {
                    String string = json.getAsJsonPrimitive().getAsString();
                    if (isNullOrEmpty(string)) {
                        return null;
                    }
                    return LocalDate.parse(string, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMATTER));
                })
                .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (localTime, type, jsonSerializationContext) -> {
                    if (Objects.isNull(localTime)) {
                        return new JsonPrimitive("");
                    }
                    return new JsonPrimitive(localTime.format(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMATTER)));
                })
                .registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (json, typeOfT, context) -> {
                    String string = json.getAsJsonPrimitive().getAsString();
                    if (isNullOrEmpty(string)) {
                        return null;
                    }
                    return LocalTime.parse(string, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMATTER));
                })
                .create();

        PRETTY_GSON = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (localDateTime, typeOfSrc, context) -> {
                    if (Objects.isNull(localDateTime)) {
                        return new JsonPrimitive("");
                    }
                    return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMATTER)));
                })
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json1, typeOfT, context) -> {
                    String string = json1.getAsJsonPrimitive().getAsString();
                    if (isNullOrEmpty(string)) {
                        return null;
                    }
                    return LocalDateTime.parse(string, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMATTER));
                })
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (localDate, type, jsonSerializationContext) -> {
                    if (Objects.isNull(localDate)) {
                        return new JsonPrimitive("");
                    }
                    return new JsonPrimitive(localDate.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMATTER)));
                })
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) -> {
                    String string = json.getAsJsonPrimitive().getAsString();
                    if (isNullOrEmpty(string)) {
                        return null;
                    }
                    return LocalDate.parse(string, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMATTER));
                })
                .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (localTime, type, jsonSerializationContext) -> {
                    if (Objects.isNull(localTime)) {
                        return new JsonPrimitive("");
                    }
                    return new JsonPrimitive(localTime.format(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMATTER)));
                })
                .registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (json, typeOfT, context) -> {
                    String string = json.getAsJsonPrimitive().getAsString();
                    if (isNullOrEmpty(string)) {
                        return null;
                    }
                    return LocalTime.parse(string, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMATTER));
                })
                .setPrettyPrinting()
                .create();
    }

    private static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    private static void assertTrue(boolean bool, String errMsg) {
        if (!bool) {
            throw new IllegalArgumentException(errMsg);
        }
    }
}
















