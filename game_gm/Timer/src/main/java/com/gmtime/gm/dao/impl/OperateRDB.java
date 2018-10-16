package com.gmtime.gm.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gmdesign.exception.GmException;
import com.gmtime.gm.dao.OperateRDBIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;


@Repository("operateRDB")
public class OperateRDB implements OperateRDBIF {
    private final ShardedJedisPool shardedJedisPool;
    private ShardedJedis jedis;

    @Autowired
    public OperateRDB(@Qualifier("shardedJedisPool") ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    public void disconnect() {
        this.shardedJedisPool.getResource().disconnect();
    }

    @Override
    public boolean setValue(String key, Object value) throws GmException {
        jedis = this.shardedJedisPool.getResource();
        boolean flag ;
        try {
            String rl = jedis.set(key, value.toString());
            flag = rl.equals("OK");
        } finally {
            jedis.close();
        }
        return flag;
    }

    @Override
    public String getValue(String key) throws GmException {
        String value ;
        try {
            jedis = this.shardedJedisPool.getResource();
            value = jedis.get(key);
        } finally {
            jedis.close();
        }
        return value;
    }

    @Override
    public boolean setValueOfArray(String key, Object... value) throws GmException {
        long flag = -1;
        try {
            jedis = this.shardedJedisPool.getResource();
            for (Object aValue : value) {
                flag = jedis.lpush(key,aValue.toString());
            }
        } finally {
            jedis.close();
        }
        return flag > 0;
    }

    @Override
    public List<String> getValueOfArray(String key) throws GmException {
        return getValueOfArray(key, 0, -1);
    }

    @Override
    public List<String> getValueOfArray(String key, int start, int end) throws GmException {
        List<String> list ;
        try {
            jedis = this.shardedJedisPool.getResource();
            list = jedis.lrange(key, start, end);
        } finally {
            jedis.close();
        }
        return list;
    }

    @Override
    public boolean setValueOfMap(String key, Map<String, String> value) throws GmException {
        boolean flag ;
        try {
            jedis = this.shardedJedisPool.getResource();
            String rl = jedis.hmset(key, value);
            flag = rl.equals("OK");
        } finally {
            jedis.close();
        }
        return flag;
    }

    @Override
    public Map<String, String> getValueOfMap(String key) throws GmException {
        Map<String, String> map ;
        try {
            jedis = this.shardedJedisPool.getResource();
            map = jedis.hgetAll(key);
        } finally {
            jedis.close();
        }
        return map;
    }

    @Override
    public Map<String, String> getValueOfMap(String key, String... mKey) throws GmException {
        Map<String, String> map = new HashMap<>();
        try {
            List<String> list = jedis.hmget(key, mKey);
            for (int i = 0; i < list.size(); i++) {
                map.put(mKey[i], list.get(i));
            }
        } finally {
            jedis.close();
        }
        return map;
    }

    @Override
    public boolean setValueOfSet(String key, Object... value) throws GmException {
        long flag = -1;
        try {
            jedis = this.shardedJedisPool.getResource();
            for (Object aValue : value) {
                flag = jedis.sadd(key,aValue.toString());
            }
        } finally {
            jedis.close();
        }
        return flag > 0;
    }

    @Override
    public Set<String> getValueOfSet(String key) throws GmException {
        Set<String> set ;
        try {
            jedis = this.shardedJedisPool.getResource();
            set = jedis.smembers(key);
        } finally {
            jedis.close();
        }
        return set;
    }

    @Override
    public boolean deleteValue(String key) throws GmException {
        long flag;
        try {
            jedis = this.shardedJedisPool.getResource();
            flag=jedis.del(key);
        } finally {
            jedis.close();
        }
        return flag>0;
    }

}
