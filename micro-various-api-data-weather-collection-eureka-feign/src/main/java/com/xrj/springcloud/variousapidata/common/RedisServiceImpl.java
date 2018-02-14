package com.xrj.springcloud.variousapidata.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisServiceImpl implements RedisService {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate;
	
	/*---------------redis List 类型,存储的value为String---------------*/
	@Override
	public Long push(String key, String value) {
		return redisTemplate.opsForList().leftPush(key, value);
	}

	@Override
	public String pop(String key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	@Override
	public Long in(String key, String value) {
		return redisTemplate.opsForList().rightPush(key, value);
	}

	@Override
	public String out(String key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	@Override
	public Long length(String key) {
		return redisTemplate.opsForList().size(key);
	}

	@Override
	public List<String> range(String key, int start, int end) {
		return redisTemplate.opsForList().range(key, start, end);
	}

	@Override
	public void remove(String key, long i, String value) {
		redisTemplate.opsForList().remove(key, i, value);
	}

	@Override
	public String index(String key, long index) {
		return redisTemplate.opsForList().index(key, index);
	}

	@Override
	public void set(String key, long index, String value) {
		redisTemplate.opsForList().set(key, index, value);

	}

	@Override
	public void trim(String key, long start, int end) {
		redisTemplate.opsForList().trim(key, start, end);

	}

	
	
	/*--------------- ----------------*/
	@Override
	public int size(String key) {
		return redisTemplate.opsForList().size(key).intValue();
	}

	@Override
	public <T> List<T> rangeTranJson(String key, long start, long end, Class<T> clazz) {
		List<String> list = redisTemplate.opsForList().range(key, start, end);
		return parseJsonList(list, clazz);
	}

	/*-------------------redis List 类型,存储的value为对象--------------------*/
	@Override
	public <T> void leftPush(String key, T obj) {
		if (obj == null) {
			return;
		}

		redisTemplate.opsForList().leftPush(key, toJson(obj));
	}
	

	@Override
	public <T> void rightPush(String key, T obj) {
		if (obj == null) {
			return;
		}

		redisTemplate.opsForList().rightPush(key, toJson(obj));
	}

	@Override
	public <T> T leftPop(String key, Class<T> clazz) {
		String value = redisTemplate.opsForList().leftPop(key);
		return parseJson(value, clazz);
	}

	@Override
	public <T> T rightPop(String key, Class<T> clazz) {
		String value = redisTemplate.opsForList().rightPop(key);
		return parseJson(value, clazz);
	}
	
	@Override
	public <T> T  rightPop(String key, Class<T> clazz, Long timeout, TimeUnit unit){
		String value = redisTemplate.opsForList().rightPop(key, timeout, unit);
		return parseJson(value, clazz);
	}

	@Override
	public String rightPop(String key, Long timeout, TimeUnit unit){
		String value = redisTemplate.opsForList().rightPop(key, timeout, unit);
		return value;
	}
	
	@Override
	public void rightPushAll(String key, Collection<?> values, Long timeout, TimeUnit unit) {
		if (values == null || values.isEmpty()) {
			return;
		}

		redisTemplate.opsForList().rightPushAll(key, toJsonList(values));
		if (timeout != null) {
			redisTemplate.expire(key, timeout, unit);
		}
	}
	
	@Override
	public String rightPopLeftPush(String sourceKey, String destinationKey){
		String value = redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
		return value;
	}
	
	@Override
	public <T> T rightPopLeftPush(String sourceKey, String destinationKey, Class<T> clazz){
		String value = redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
		return parseJson(value, clazz);
	}
	
	@Override
	public <T> T rightPopLeftPush(String sourceKey, String destinationKey, Class<T> clazz, long timeout, TimeUnit unit){
		String value = redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
		return parseJson(value, clazz);
		
	}

	@Override
	public <T> T getForList(Collection<String> keys, Class<T> clazz) {
		String value = redisTemplate.opsForValue().get(keys);
        return parseJson(value, clazz);
	}

	@Override
	public void remove(String key, int count, Object obj) {
        if (obj == null) {
            return;
        }
         
        redisTemplate.opsForList().remove(key, count, toJson(obj));
	}


	/*---------------common---------------*/
	@Override
	public boolean expire(String key, Long timeout, TimeUnit unit){
		return redisTemplate.expire(key, timeout, unit);
	}
	
	@Override
	public boolean exists(String key){
		return redisTemplate.hasKey(key);
	}
	
	@Override
	public Collection<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	@Override
	public void delete(String key) {
		redisTemplate.delete(key);
	}

	@Override
	public void delete(Collection<String> keys) {
		redisTemplate.delete(keys);
	}

	/* -----------redis 为 string 类型--------- */
	@Override
	public void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public <T> void set(String key, T obj, Long timeout, TimeUnit unit) {
		if (obj == null) {
			return;
		}

		String value = toJson(obj);
		if (timeout != null) {
			redisTemplate.opsForValue().set(key, value, timeout, unit);
		} else {
			set(key, value);
		}
	}
	
	@Override
	public boolean setIfAbsent(String key, String value){
		return redisTemplate.opsForValue().setIfAbsent(key, value);
	}
	
	@Override
	public int decrement(String key, int delta) {
		Long value = redisTemplate.opsForValue().increment(key, -delta);
		return value.intValue();
	}

	@Override
	public int increment(String key, int delta) {
		Long value = redisTemplate.opsForValue().increment(key, delta);
		return value.intValue();
	}
	
	@Override
	public String get(String key) {
		return redisTemplate.opsForValue().get(key);
	}
	
	@Override
    public void mset(java.util.Map<String,String> map) {
		redisTemplate.opsForValue().multiSet(map);
	};
	
	
	@Override
	public <T> List<T> mget(Collection<String> keys, Class<T> clazz) {
		List<String> values = redisTemplate.opsForValue().multiGet(keys);
        return parseJsonList(values, clazz);
	}
	

	@Override
	public List<String> mget(Collection<String> keys) {
		return redisTemplate.opsForValue().multiGet(keys);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAndSet(String key, T obj) {
		if (obj == null) {
			return (T) get(key);
		}
		return (T) redisTemplate.opsForValue().getAndSet(key, toJson(obj));
	}
	
	@Override
	public String getAndSet(String key, String value) {
		if (StringUtils.isBlank(value)) {
			return get(key);
		}
		return redisTemplate.opsForValue().getAndSet(key, value);
	}
	
	/*------------redis为 散列数据类型------------*/
	@Override
	public void hset(String key, Object field, Object value){
		redisTemplate.opsForHash().put(key, field, value);
	}
	
	@Override
	public void hmset(String key, Map<? extends Object, ? extends Object> values){
		redisTemplate.opsForHash().putAll(key, values);
	}
	
	@Override
	public Object hget(String key, Object field){
		return redisTemplate.opsForHash().get(key, field);
	}
	
	@Override
	public List<Object> hmget(String key,  Set<Object> fields){
		return redisTemplate.opsForHash().multiGet(key, fields);
	}
	
	@Override
	public List<Object> hmgetRetListType(String key, List<Object> fields){
		return redisTemplate.opsForHash().multiGet(key, fields);
		
	}
	@Override
	public Map<Object, Object> hmget(String key, List<Object> fields){
		List<Object> valList = hmgetRetListType(key, fields);
		Map<Object, Object> values = new ConcurrentHashMap<Object, Object>();
		for(int i=0, j=0; i<fields.size() && j<valList.size() ;i++, j++){
			values.put(fields.get(i), valList.get(j));
		}
		return values;
	}
	
	@Override
	public boolean hexists(String key, Object field){
		return redisTemplate.opsForHash().hasKey(key, field);
	}
	
	/* 
	 * 显示的是某个key值对应的所有的fields字段
	 */
	@Override
	public Set<Object> hkeys(String key){
		return redisTemplate.opsForHash().keys(key);
	}

	@Override
	public void hdelete(String key, Object... fields){
		redisTemplate.opsForHash().delete(key, fields);
	}

	/* -----------redis为 有序集合类型 --------- */
	@Override
	public int zcard(String key) {
		return redisTemplate.opsForZSet().zCard(key).intValue();
	}

	@Override
	public List<String> zrange(String key, long start, long end) {
		Set<String> set = redisTemplate.opsForZSet().range(key, start, end);
		return setToList(set);
	}

	private List<String> setToList(Set<String> set) {
		if (set == null) {
			return null;
		}
		return new CopyOnWriteArrayList<String>(set);
	}

	@Override
	public void zadd(String key, Object obj, double score) {
		if (obj == null) {
			return;
		}
		redisTemplate.opsForZSet().add(key, toJson(obj), score);
	}
	
	@Override
	public Double zscore(String key, Object obj) {
		Double score = redisTemplate.opsForZSet().score(key, toJson(obj));
		return score;
		
	};

	@Override
	public void zaddAll(String key, List<TypedTuple<?>> tupleList, Long timeout, TimeUnit unit) {
		if (tupleList == null || tupleList.isEmpty()) {
			return;
		}

		Set<TypedTuple<String>> tupleSet = toTupleSet(tupleList);
		redisTemplate.opsForZSet().add(key, tupleSet);
		if (timeout != null) {
			redisTemplate.expire(key, timeout, unit);
		}

	}

	private Set<TypedTuple<String>> toTupleSet(List<TypedTuple<?>> tupleList) {
		Set<TypedTuple<String>> tupleSet = new LinkedHashSet<TypedTuple<String>>();
		for (TypedTuple<?> t : tupleList) {
			tupleSet.add(new DefaultTypedTuple<String>(toJson(t.getValue()), t.getScore()));
		}
		return tupleSet;
	}

	@Override
	public void zrem(String key, Object obj) {
		if (obj == null) {
			return;
		}
		redisTemplate.opsForZSet().remove(key, toJson(obj));

	}

	@Override
	public void unionStore(String destKey, Collection<String> keys, Long timeout, TimeUnit unit) {
		if (keys == null || keys.isEmpty()) {
			return;
		}

		Object[] keyArr = keys.toArray();
		String key = (String) keyArr[0];

		Collection<String> otherKeys = new ArrayList<String>(keys.size() - 1);
		for (int i = 1; i < keyArr.length; i++) {
			otherKeys.add((String) keyArr[i]);
		}

		redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
		if (timeout != null) {
			redisTemplate.expire(destKey, timeout, unit);
		}

	}

	/*-----tools------*/
	/**
	 * SortField:按字段名称排序后输出。默认为false 这里使用的是fastjson：为了更好使用sort field
	 * martch优化算法提升parser的性能，fastjson序列化的时候，
	 * 缺省把SerializerFeature.SortField特性打开了。 反序列化的时候也缺省把SortFeidFastMatch的选项打开了。
	 * 这样，如果你用fastjson序列化的文本，输出的结果是按照fieldName排序输出的，parser时也能利用这个顺序进行优化读取。
	 * 这种情况下，parser能够获得非常好的性能。
	 */
	private static <T> String toJson(T obj) {
		ObjectMapper objectMapper = new ObjectMapper();
		String value = null;
		try {
			value = objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			logger.error("RedisServiceImpl toJson method throw a exception: {}", e);
		}
		return value;

	}

	private static <T> T parseJson(String json, Class<T> clazz) {
		ObjectMapper objectMapper = new ObjectMapper();
		T obj = null;
		try {
			obj = objectMapper.readValue(json, clazz);
		} catch (IOException e) {
			logger.error("RedisServiceImpl parseJson method throw a exception: {}", e);
		}
		return obj;
	}

	private static <T> List<T> parseJsonList(List<String> list, Class<T> clazz) {
		if (list == null) {
			return null;
		}

		List<T> result = new CopyOnWriteArrayList<T>();
		for (String s : list) {
			result.add(parseJson(s, clazz));
		}
		return result;
	}

	private static List<String> toJsonList(Collection<?> values) {
		if (values == null) {
			return null;
		}

		List<String> result = new CopyOnWriteArrayList<String>();
		for (Object obj : values) {
			result.add(toJson(obj));
		}
		return result;
	}

	/**
	 * 检查某些key值是否存在值
	 * @param cacheService
	 * @param key
	 * @param fields
	 * @return
	 */
//	public static boolean checkIfExistsTheseValsAndAreAllNotBlank(CacheService cacheService, String key, String... fields) {
//		boolean flag = true;
//		for (String field : fields) {
//			if (!cacheService.hexists(key, field) || StringUtil.isEmpty(cacheService.hget(key, field))) {
//				return !flag;
//			}
//		}
//		return flag;
//	}
	
	public static void main(String[] args) {
		String score = "score";
		String val  = toJson(score);
		System.out.println(val);
	}

}
