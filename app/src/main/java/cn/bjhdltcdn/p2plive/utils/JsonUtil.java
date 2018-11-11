package cn.bjhdltcdn.p2plive.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orhanobut.logger.Logger;

public class JsonUtil {

    private static ObjectMapper objectMapper;

    /**
     * 获取 ObjectMapper解析类
     *
     * @return
     */
    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        // 解析器支持解析单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //解析器支持解析结束符
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        // 当反序列化json时，未知属性会引起的反序列化被打断，这里我们禁用未知属性打断反序列化功能，
        // 因为，例如json里有10个属性，而我们的bean中只定义了2个属性，其它8个属性将被忽略
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return objectMapper;
    }

    /**
     * 通过key获取数据
     *
     * @param jsonStr json串
     * @param key     名称
     * @return
     */
    public static String getJsonValue(String jsonStr, String key) {
        try {
            ObjectMapper mapper = getObjectMapper();
            JsonNode jsonNode = mapper.readValue(jsonStr, JsonNode.class);
            JsonNode node = jsonNode.get(key);
            if (node != null) {
                return node.asText().trim();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过key获取数据
     *
     * @param jsonStr json串
     * @param key     名称
     * @return
     */
    public static <T> T getJsonValueObject(String jsonStr, String key, Class<T> object) {
        try {
            ObjectMapper mapper = getObjectMapper();
            JsonNode jsonNode = mapper.readValue(jsonStr, JsonNode.class);
            JsonNode node = jsonNode.get(key);
            if (node != null) {
                String json = null;
                if (node.isTextual()) {
                    json = node.asText().trim();
                } else if (node.isObject()) {
                    json = node.toString();
                }

                Logger.d("json == " + json);
                T t = mapper.readValue(json, object);
                Logger.d("t ===>>> " + t);
                return t;

            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param jsonStr json信息
     * @param key1    一层key
     * @param key2    二层key
     * @return string
     * @Description: 通过key获取value
     * @date 2015-10-26 下午12:31:21
     */
    public static String getJsonValue(String jsonStr, String key1, String key2) {
        try {
            ObjectMapper mapper = getObjectMapper();
            JsonNode jsonNode = mapper.readValue(jsonStr, JsonNode.class);
            JsonNode node = jsonNode.get(key1);
            if (node != null) {
                JsonNode node2 = node.get(key2);
                if (node2 != null) {
                    return node2.asText().trim();
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
