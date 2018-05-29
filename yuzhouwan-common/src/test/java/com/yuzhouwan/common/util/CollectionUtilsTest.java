package com.yuzhouwan.common.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.util.ByteBufferInputStream;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.*;

import static com.yuzhouwan.common.util.CollectionUtils.intersection;
import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: Collection Util Tester
 *
 * @author Benedict Jin
 * @since 2016/6/12
 */
public class CollectionUtilsTest {

    private static final Logger _log = LoggerFactory.getLogger(CollectionUtilsTest.class);

    @Test
    public void removeAllByStrWithSeparator() {
        assertEquals(Collections.singletonList("bbb"),
                CollectionUtils.removeAllByStrWithSeparator(Arrays.asList("aaa", "abc", "bbb"), "a,c", ","));
        assertEquals(Collections.singletonList("bbb"),
                CollectionUtils.removeAllByStrWithSeparator(Arrays.asList("aaa", "abc", "bbb"), "a", ","));
        assertEquals(Arrays.asList("aaa", "bbb"),
                CollectionUtils.removeAllByStrWithSeparator(Arrays.asList("aaa", "abc", "bbb"), "c", ","));
        assertEquals(Collections.emptyList(),
                CollectionUtils.removeAllByStrWithSeparator(Arrays.asList("aaa", "abc", "bbb"), "", ","));

        assertEquals(Collections.singletonList(""),
                CollectionUtils.removeAllByStrWithSeparator(Collections.singletonList(""), "a", ","));
    }

    @Test
    @SuppressWarnings("all")
    public void duplicate() throws Exception {
        {
            Integer[] a = new Integer[]{1, 2, 3};
            Integer[] b = new Integer[]{3};
            Integer[] c = new Integer[]{4};
            Integer[] d = null;
            assertEquals(3, intersection(a, b)[0]);
            assertEquals(0, intersection(b, c).length);
            assertEquals(true, intersection(c, d) == null);
        }
        {
            List<String> a = new LinkedList<>();
            a.add("a");
            a.add("c");
            a.add("d");
            List<String> b = new LinkedList<>();
            b.add("a");
            b.add("b");
            b.add("c");
            Collection<String> result = intersection(a, b);
            Iterator iterator = result.iterator();
            assertEquals(2, result.size());
            assertEquals("a", iterator.next());
            assertEquals("c", iterator.next());
            assertEquals(3, a.size());
            assertEquals(3, b.size());
        }
    }

    @Test
    public void getDuplicate() {
        A _a = new A(1, "2", 3);
        A _b = new A(1, "a", 5);
        A _c = new A(4, "!", 3);

        A _d = new A(4, "2", 5);

        LinkedList<A> aList = new LinkedList<>();
        aList.add(_a);
        aList.add(_b);
        aList.add(_c);
        assertEquals(_a, CollectionUtils.getDuplicate(aList, _d, "b", String.class));
        assertEquals(true, 2 == aList.size());
        assertEquals(_c, CollectionUtils.getDuplicate(aList, _d, "a", Integer.class));
        assertEquals(true, 1 == aList.size());
        assertEquals(_b, CollectionUtils.getDuplicate(aList, _d, "c", Object.class));
        assertEquals(true, 0 == aList.size());
        assertEquals(null, CollectionUtils.getDuplicate(aList, _d, "d", Object.class));
        assertEquals(true, 0 == aList.size());
        aList.add(_a);
        aList.add(_b);
        aList.add(_c);
        assertEquals(null, CollectionUtils.getDuplicate(aList, _d, "d", Object.class));
        assertEquals(true, 3 == aList.size());
    }

    @Test
    public void removeCollTest() {
        A _a = new A(1, "2", 3);
        A _b = new A(1, "a", 3);
        A _c = new A(1, "!", 3);

        LinkedList<A> aList = new LinkedList<>();
        aList.add(_a);
        aList.add(_b);
        aList.add(_c);
        try {
            aList.remove(0);
            aList.remove(2);
            aList.remove(1);
            assertEquals(true, aList.size() == 0);
        } catch (Exception e) {
            assertEquals(true, e instanceof IndexOutOfBoundsException);
        }
    }

    @Test
    public void forTest() {
        LinkedList<String> l = new LinkedList<>();
        l.add("a");
        l.add("b");
        l.add("c");
        int len, count = 0;
        String s;
        // l.size() will be called 4 times
        for (int i = 0; i < (len = l.size()); i++) {
            s = String.format("%s [%d/%d]", l.get(i), i + 1, len);
            if ("a [1/3]".equals(s) || "b [2/3]".equals(s) || "c [3/3]".equals(s)) count++;
        }
        assertEquals(3, count);
    }

    private class A {
        int a;
        String b;
        Object c;

        A(int a, String b, Object c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    @Test
    public void singletonListTest() {
        ByteBuffer bb = DecimalUtils.byteArray2byteBuffer("yuzhouwan".getBytes());
        List<ByteBuffer> bytes = Collections.singletonList(bb);
        ByteBufferInputStream inputStream = new ByteBufferInputStream(bytes);
        /*BinaryDecoder bd = */
        DecoderFactory.get().binaryDecoder(inputStream, null);
//        System.out.println(new String(DecimalUtils.byteBuffer2byteArray(
//                DecoderFactory.get().binaryDecoder(inputStream, null).readBytes(bb))));
    }

    @Test
    @SuppressWarnings("all")
    public void collectionReferenceTest() {
        LinkedList<Long> oldList = new LinkedList<>();
        oldList.add(0L);
        LinkedList<Long> newList = oldList;
        newList.add(1L);
        assertEquals("[0,1]", JSON.toJSONString(oldList));
        assertEquals("[0,1]", JSON.toJSONString(newList));
    }

    /*
    JVM: -ea -Xmx700M -Xms700M -Xmn256M -XX:+AlwaysPreTouch
    1_1111_1111 / 1024 / 1024 / 1024 = 0.1035G

    Array length: 10, Spend Time: 1048744.0ns = 1.048744ms
    Array length: 100, Spend Time: 37322.0ns = 0.037322ms
    Array length: 1000, Spend Time: 324078.0ns = 0.324078ms
    Array length: 10000, Spend Time: 2465421.0ns = 2.465421ms
    Array length: 100000, Spend Time: 7394085.0ns = 7.394085ms
    Array length: 1000000, Spend Time: 2.5203459E7ns = 25.203459ms
    Array length: 10000000, Spend Time: 6.91587931E8ns = 691.587931ms
    Array length: 100000000, Spend Time: 7.105568329E9ns = 7105.568329ms
     */
    @Test
    public void getNthNumberTest() {
        internalNthTest(10, 1);
        internalNthTest(100, 10);
        internalNthTest(1000, 100);
    }

    @Ignore
    @Test
    public void getNthNumberPerformanceTest() {
        internalNthTest(1_0000, 1000);
        internalNthTest(10_0000, 10000);
        internalNthTest(100_0000, 10_0000);
        internalNthTest(1000_0000, 100_0000);
        internalNthTest(1_0000_0000, 1000_0000);
    }

    private void internalNthTest(int len, int n) {
        int[] arr = new int[len];
        Random r = new Random(17);
        for (int i = 0; i < len - n; i++) {
            arr[i] = r.nextInt(len) + 1;
        }
        arr[len - 1] = 1;
        long startTime = System.nanoTime();
        int nth = CollectionUtils.getNthNumberMin(arr, n);
        long endTime = System.nanoTime();
        assertEquals(1, nth);
        double totalTime = endTime - startTime;
        _log.info("Array length: {}, Spend Time: {}ns = {}ms", len, totalTime, totalTime / Math.pow(10, 6));
    }

    @Test
    public void exchangeKeysTest() {
        Map<String, Map<Long, String>> map = Maps.newHashMap();
        Map<Long, String> internalMap = Maps.newHashMap();
        internalMap.put(1L, "1L");
        internalMap.put(2L, "2L");
        Map<Long, String> internalMap2 = Maps.newHashMap();
        internalMap2.put(2L, "2L");
        internalMap2.put(3L, "3L");
        map.put("1", internalMap);
        map.put("2", internalMap2);
        Map<Long, Map<String, String>> aim = CollectionUtils.exchangeKeys(map);
        assertEquals("{1:{\"1\":\"1L\"},2:{\"1\":\"2L\",\"2\":\"2L\"},3:{\"2\":\"3L\"}}", JSON.toJSONString(aim));
    }

    @Test
    public void joinTest() {
        {
            String[] arr = {"a", "b", "c"};
            assertEquals("a-b-c", CollectionUtils.join(arr, "-"));
        }
        {
            List<Integer> list = new LinkedList<>();
            assertEquals("", CollectionUtils.join(list, ", "));
            list.add(1);
            list.add(2);
            list.add(3);
            assertEquals("1, 2, 3", CollectionUtils.join(list, ", "));
            assertEquals("1null2null3", CollectionUtils.join(list, null));
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void convertTypeTest() {
        List<Object> objectList = new LinkedList<>();
        objectList.add(1);
        objectList.add(2);
        objectList.add(3);
        List<Integer> integerList = (List<Integer>) (List<?>) objectList;
        int count = 0;
        for (Integer integer : integerList) count += integer;
        assertEquals(6, count);
    }

    @Test
    public void mapPutIfAbsentTest() {
        LinkedHashMap<String, Integer> lhm = new LinkedHashMap<>();
        Integer old = lhm.putIfAbsent("a", 1);
        assertEquals(old == null, true);
        old = lhm.putIfAbsent("a", 1);
        assertEquals(old == 1, true);
        old = lhm.putIfAbsent("a", 1);
        assertEquals(old == 1, true);
        old = lhm.putIfAbsent("b", 2);
        assertEquals(old == null, true);

        old = putIfAbsent(lhm, "c", 3);
        assertEquals(old == null, true);
        old = putIfAbsent(lhm, "c", 3);
        assertEquals(old == 3, true);
        old = putIfAbsent(lhm, "d", 4);
        assertEquals(old == null, true);
    }

    private static Integer putIfAbsent(LinkedHashMap<String, Integer> lhm, String key, Integer value) {
        Integer oldValue = lhm.get(key);
        if (oldValue == null) oldValue = lhm.put(key, value);
        return oldValue;
    }
}
