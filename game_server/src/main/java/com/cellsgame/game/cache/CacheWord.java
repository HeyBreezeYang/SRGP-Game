package com.cellsgame.game.cache;

import com.cellsgame.common.util.collection.CharFilter;
import com.cellsgame.common.util.collection.Trie;
import com.cellsgame.common.util.collection.TrieNode;
import com.cellsgame.game.module.sys.csv.DirtyWordsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

public class CacheWord {
    private static final byte[] UNUSE = new byte[0];
    private static Logger log = LoggerFactory.getLogger(CacheWord.class);
    private static Field f;
    private static char[] KEY_WORDS = /*·*/ "!\"#$%&'()*+,-./:;<=>?@[\\]^`{|}~【】【】［］".toCharArray();
    private static CharFilter filter = (char t) -> t <= ' ';
    private static Trie<byte[]> dirtyWords = new Trie<>(false, true, filter);

    static {
        try {
            CacheWord.f = String.class.getDeclaredField("value");
            CacheWord.f.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            log.error("", e);
        }
    }

    public static boolean haveKeyWord(String key) {
        for (char c : KEY_WORDS) {
            int p = key.indexOf(c);
            if (p >= 0)
                return true;
        }
        return false;
    }

    public static boolean haveDirtyWord(String key) {
        return dirtyWords.anyKeyIn(key);
    }

    /**
     * 是否都是英文单词  [A-Za-z]\w{3,19}
     * 第一位不能为数字 长度限制为 4到20位
     */
    public static boolean isWorld_en(String key) {
        return null != key && key.matches("[A-Za-z]\\w{3,19}");
    }

    public static void init() {
        Map<Integer, DirtyWordsConfig> clz = CacheConfig.getCfgsByClz(DirtyWordsConfig.class);
        if (null != clz) {
            for (DirtyWordsConfig cfg : clz.values()) {
                dirtyWords.put(cfg.getDirtyWord(), UNUSE);
            }
        }
    }

    /**
     * 替换 屏蔽词
     */
    public static String replaceDirtyWords(String src) {
        StringBuilder sb = new StringBuilder();
        TrieNode<byte[]> r = dirtyWords.getRoot();
        boolean start = true;
        for (int i = 0; i < src.length(); i++) {
            char charAt = src.charAt(i);
            if (filter.isFilter(charAt)) {
                if (!start)
                    sb.append(charAt);
                continue;
            }
            start = false;
            if (null != getChildNode(r, charAt)) {
                int end = nodeHaveWord(r, src, i);
                if (end >= 0) {
                    sb.append('*');
                    i = end;
                } else {
                    sb.append(charAt);
                }
            } else {
                sb.append(charAt);
            }
        }
        return sb.toString().trim();
    }

    private static void replaceWords(String src, int star, int end) {
        try {
            char[] value = (char[]) f.get(src);
            for (int i = star; i < end && i < src.length(); i++) {
                value[i] = '*';
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.error("", e);
        }
    }

    private static TrieNode<byte[]> getChildNode(TrieNode<byte[]> node, char key) {
        return node.children.get(key);
    }

    private static int nodeHaveWord(TrieNode<byte[]> node, String src, int ix) {
        if (null == node) return -1;
        int x = -1;
        for (int i = ix; i < src.length(); ) {
            //
            for (int j = i; j < src.length(); j++) {
                if (!filter.isFilter(src.charAt(j))) {
                    i = j;
                    break;
                }
            }
            char key = src.charAt(i);
            node = getChildNode(node, key);
            if (null == node) break;
            if (node.terminable) {
                x = i;
                break;
            }
            i++;
        }
        return x;
    }
}
