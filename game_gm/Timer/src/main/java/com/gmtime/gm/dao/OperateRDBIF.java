package com.gmtime.gm.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gmdesign.exception.GmException;

/**
 * Created by D-work on 2016/7/22.
 */
public interface OperateRDBIF {
    boolean setValue(String key, Object value) throws GmException;

    String getValue(String key) throws GmException;

    boolean setValueOfArray(String key, Object... value) throws GmException;

    List<String> getValueOfArray(String key) throws GmException;

    List<String> getValueOfArray(String key, int start, int end) throws GmException;

    boolean setValueOfMap(String key, Map<String, String> value) throws GmException;

    Map<String, String> getValueOfMap(String key) throws GmException;

    Map<String, String> getValueOfMap(String key, String... mKey) throws GmException;

    boolean setValueOfSet(String key, Object... value) throws GmException;

    Set<String> getValueOfSet(String key) throws GmException;

    boolean deleteValue(String key) throws GmException;
}
