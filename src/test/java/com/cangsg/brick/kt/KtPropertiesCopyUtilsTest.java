package com.cangsg.brick.kt;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.cangsg.brick.kt.entry.KtTest11;
import com.cangsg.brick.kt.entry.KtTest11Item;
import com.cangsg.brick.kt.entry.KtTest12;
import com.cangsg.brick.kt.entry.KtTest13;
import com.cangsg.brick.kt.entry.KtTest14;
import com.cangsg.brick.kt.entry.KtTest15;
import com.cangsg.brick.kt.entry.KtTest16;
import com.cangsg.brick.kt.entry.KtTest17;
import com.cangsg.brick.kt.entry.KtTest17Item;
import com.cangsg.brick.kt.entry.KtTest18;
import com.cangsg.brick.kt.entry.KtTest18Item;
import com.cangsg.brick.kt.entry.KtTest19;
import com.cangsg.brick.kt.entry.KtTest20;
import com.cangsg.brick.kt.entry.KtTest21;
import com.cangsg.brick.kt.entry.KtTest22;
import com.cangsg.brick.kt.entry.KtTest23;
import com.cangsg.brick.kt.entry.KtTest24;
import com.cangsg.brick.kt.entry.KtTest25;
import com.cangsg.brick.kt.entry.KtTest26;
import com.cangsg.brick.kt.entry.KtTest27;
import com.cangsg.brick.kt.entry.KtTest28;
import com.cangsg.brick.kt.entry.KtTest29;
import com.cangsg.brick.kt.entry.KtTest30;
import com.cangsg.brick.kt.entry.KtTest31;
import com.cangsg.brick.kt.entry.KtTest32;
import com.cangsg.brick.kt.entry.KtTest33;
import com.cangsg.brick.kt.entry.KtTest34;
import com.cangsg.brick.kt.entry.KtTest35;
import com.cangsg.brick.kt.entry.KtTest36;
import com.cangsg.brick.kt.entry.KtTest37;
import com.cangsg.brick.kt.entry.KtTest37Item;
import com.cangsg.brick.kt.entry.KtTest38;
import com.cangsg.brick.kt.entry.KtTest38Item;
import com.cangsg.brick.kt.entry.KtTest39;
import com.cangsg.brick.kt.entry.KtTest40;
import com.cangsg.brick.kt.entry.KtTestEnum;

public class KtPropertiesCopyUtilsTest {
    @Test
    void test1() {
        KtTest11 test11 = new KtTest11();
        test11.setI1(10);
        test11.setI2(11);
        test11.setName("hi");
        test11.setItem(new KtTest11Item());
        test11.getItem().setI1(99);
        test11.getItem().setS1("ok");
        test11.setMap1(new HashMap<>());

        test11.getMap1().put("M1", "test11-1");
        test11.getMap1().put("M2", "test11-2");
        test11.getMap1().put("M3", "test11-3");

        KtTest12 test12 = KtPropertiesUtils.copyProperties(test11, new KtTest12()).build();
        assertEquals(10, test12.getI1());
        assertEquals(11, test12.getI2());
        assertEquals("hi", test12.getName());
        assertEquals(99, test12.getItem().getI1());
        assertEquals("ok", test12.getItem().getS1());
        assertEquals("test11-1", test12.getMap1().get("M1"));
        assertEquals("test11-2", test12.getMap1().get("M2"));
        assertEquals("test11-3", test12.getMap1().get("M3"));
    }

    @Test
    void test2() {
        KtTest13 test13 = new KtTest13();
        test13.setMap1(new HashMap<>());
        test13.getMap1().put("M1", new KtTest11Item());
        test13.getMap1().get("M1").setI1(1);
        test13.getMap1().get("M1").setS1("h1");
        test13.getMap1().put("M2", new KtTest11Item());
        test13.getMap1().get("M2").setI1(2);
        test13.getMap1().get("M2").setS1("h2");

        KtTest14 test14 = KtPropertiesUtils.copyProperties(test13, new KtTest14()).build();
        assertEquals(1, test14.getMap1().get("M1").getI1());
        assertEquals(2, test14.getMap1().get("M2").getI1());
    }

    @Test
    void test3() {
        KtTest15 test15 = new KtTest15();
        test15.setB1(true);
        test15.setI1(1);
        test15.setS1("hi");
        test15.setMb1(false);
        test15.setMi1(2);
        test15.setMs1("ok");

        KtTest16 test16 = new KtTest16();
        test16.setSs1("here");

        test16 = KtPropertiesUtils.copyProperties(test15, test16).build();
        assertEquals("here", test16.getSs1());
        assertEquals("hi", test16.getS1());
        assertEquals(1, test16.getI1());
        assertEquals(true, test16.isB1());
    }

    @Test
    void test4() {
        KtTest17<KtTest17Item> test17 = new KtTest17<>();
        test17.setT1(new KtTest17Item());
        test17.getT1().setS1("hi");
        test17.setE1(KtTestEnum.t2);

        KtTest18<KtTest18Item> test18 = KtPropertiesUtils.copyProperties(test17, new KtTest18<KtTest18Item>())
                .mapType(KtTest17Item.class, KtTest18Item.class).build();
        System.out.println(test18);
    }

    @Test
    void test5() {
        KtTest19 test19 = new KtTest19();
        test19.setMap1(new HashMap<>());
        test19.getMap1().put("m1", new KtTest17Item("s1"));
        test19.getMap1().put("m2", new KtTest17Item("s2"));
        test19.getMap1().put("m3", new KtTest17Item("s3"));

        KtTest20 test20 = KtPropertiesUtils.copyProperties(test19, new KtTest20()).build();
        System.out.println(test20);
    }

    @Test
    void test6() {
        KtTest21<KtTest17Item> test21 = new KtTest21<>();
        test21.setMp1(new HashMap<>());
        test21.getMp1().put("m1", new KtTest17Item("h1"));

        KtTest22<KtTest18Item> test22 = KtPropertiesUtils.copyProperties(test21, new KtTest22<KtTest18Item>())
                .mapType(KtTest17Item.class, KtTest18Item.class).build();
        System.out.println(test22);
    }

    @Test
    void test7() {
        KtTest23 test23 = new KtTest23();
        test23.setArrInt(new int[] { 10, 20, 30, 40 });
        test23.setArrInteger(new Integer[] { 1, 2, 3, 4 });
        test23.setArrByte(new byte[] { 1, 2, 3, 4 });
        test23.setArrBByte(new Byte[] { 5, 6, 7, 8 });
        test23.setArrLong(new long[] { 1L, 2L, 3L, 4L });
        test23.setArrLLong(new Long[] { 5L, 6L, 7L, 8L });
        test23.setArrFloat(new float[] { 1.1f, 1.2f, 1.3f, 1.4f });
        test23.setArrFFloat(new Float[] { 1.1f, 1.2f, 1.3f, 1.4f });
        test23.setArrBigDecimal(new BigDecimal[] { new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3"),
                new BigDecimal("4") });
        test23.setArrBoolean(new boolean[] { true, false, true, false });
        test23.setArrBBoolean(new Boolean[] { true, false, true, false });
        test23.setArrDouble(new double[] { 0.1d, 0.2d, 0.3d, 0.4d });
        test23.setArrDDouble(new Double[] { 0.1d, 0.2d, 0.3d, 0.4d });
        test23.setArrString(new String[] { "1", "2", "2" });
        test23.setArrDate(new Date[] { new Date(2025, 1, 1) });
        test23.setArrLocalDate(
                new LocalDate[] { LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 2), LocalDate.of(2025, 1, 3) });
        test23.setArrLocalDateTime(
                new LocalDateTime[] { LocalDateTime.of(2025, 1, 1, 1, 1, 1), LocalDateTime.of(2025, 1, 1, 1, 1, 2) });

        KtTest24 test24 = KtPropertiesUtils.copyProperties(test23, new KtTest24()).build();
        assertArrayEquals(new int[] { 10, 20, 30, 40 }, test24.getArrInt());
        assertArrayEquals(new Integer[] { 1, 2, 3, 4 }, test24.getArrInteger());
        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, test24.getArrByte());
        assertArrayEquals(new Byte[] { 5, 6, 7, 8 }, test24.getArrBByte());
        assertArrayEquals(new long[] { 1L, 2L, 3L, 4L }, test24.getArrLong());
        assertArrayEquals(new Long[] { 5L, 6L, 7L, 8L }, test24.getArrLLong());
        assertArrayEquals(new float[] { 1.1f, 1.2f, 1.3f, 1.4f }, test24.getArrFloat());
        assertArrayEquals(new Float[] { 1.1f, 1.2f, 1.3f, 1.4f }, test24.getArrFFloat());
        assertArrayEquals(new BigDecimal[] { new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3"),
                new BigDecimal("4") }, test24.getArrBigDecimal());
        assertArrayEquals(new boolean[] { true, false, true, false }, test24.getArrBoolean());
        assertArrayEquals(new Boolean[] { true, false, true, false }, test24.getArrBBoolean());
        assertArrayEquals(new double[] { 0.1d, 0.2d, 0.3d, 0.4d }, test24.getArrDouble());
        assertArrayEquals(new Double[] { 0.1d, 0.2d, 0.3d, 0.4d }, test24.getArrDDouble());
        assertArrayEquals(new String[] { "1", "2", "2" }, test24.getArrString());
        assertArrayEquals(new Date[] { new Date(2025, 1, 1) }, test24.getArrDate());
        assertArrayEquals(
                new LocalDate[] { LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 2), LocalDate.of(2025, 1, 3) },
                test24.getArrLocalDate());
        assertArrayEquals(
                new LocalDateTime[] { LocalDateTime.of(2025, 1, 1, 1, 1, 1), LocalDateTime.of(2025, 1, 1, 1, 1, 2) },
                test24.getArrLocalDateTime());
    }

    @Test
    void test8() {
        KtTest25 test25 = new KtTest25();
        test25.setArrItems(
                new KtTest17Item[] { new KtTest17Item("s1"), new KtTest17Item("s2"), new KtTest17Item("s3") });

        KtTest26 test26 = KtPropertiesUtils.copyProperties(test25, new KtTest26()).build();

        for (int i = 0; i < test25.getArrItems().length; i++) {
            KtTest17Item item25 = test25.getArrItems()[i];
            KtTest18Item item26 = test26.getArrItems()[i];

            assertEquals(item25.getS1(), item26.getS1());
        }
    }

    @Test
    void test9() {
        KtTest27 test27 = new KtTest27();
        test27.setListIntegerItems(new ArrayList<>());
        test27.getListIntegerItems().add(1);
        test27.getListIntegerItems().add(2);
        test27.getListIntegerItems().add(3);
        test27.setListByteItems(new ArrayList<>());
        test27.getListByteItems().add((byte) 1);
        test27.getListByteItems().add((byte) 2);
        test27.setListLongItems(new ArrayList<>());
        test27.getListLongItems().add(4L);
        test27.getListLongItems().add(5L);
        test27.setListFloatItems(new ArrayList<>());
        test27.getListFloatItems().add(2.3f);
        test27.getListFloatItems().add(3.5f);
        test27.getListFloatItems().add(5.5f);
        test27.setListBigDecimalItems(new ArrayList<>());
        test27.getListBigDecimalItems().add(new BigDecimal(22L));
        test27.getListBigDecimalItems().add(new BigDecimal(23L));
        test27.setListBooleanItems(new ArrayList<>());
        test27.getListBooleanItems().add(true);
        test27.getListBooleanItems().add(false);
        test27.getListBooleanItems().add(false);
        test27.getListBooleanItems().add(true);
        test27.setListDoubleItems(new ArrayList<>());
        test27.getListDoubleItems().add(1.2);
        test27.getListDoubleItems().add(2.2);
        test27.getListDoubleItems().add(4.2);
        test27.setListStringItems(new ArrayList<>());
        test27.getListStringItems().add("s1");
        test27.getListStringItems().add("s2");
        test27.getListStringItems().add("s3");
        test27.setListDateItems(new ArrayList<>());
        test27.getListDateItems().add(new Date(2025, 1, 1));
        test27.getListDateItems().add(new Date(2025, 2, 1));
        test27.getListDateItems().add(new Date(2025, 3, 1));
        test27.setListLocalDateItems(new ArrayList<>());
        test27.getListLocalDateItems().add(LocalDate.of(2025, 1, 1));
        test27.getListLocalDateItems().add(LocalDate.of(2025, 1, 2));
        test27.setListLocalDateTimeItems(new ArrayList<>());
        test27.getListLocalDateTimeItems().add(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        test27.getListLocalDateTimeItems().add(LocalDateTime.of(2025, 1, 2, 1, 1, 1));

        KtTest28 test28 = KtPropertiesUtils.copyProperties(test27, new KtTest28()).build();
        System.out.println(test28);

        for (int i = 0; i < test27.getListIntegerItems().size(); i++) {
            Integer item27 = test27.getListIntegerItems().get(i);
            Integer item28 = test28.getListIntegerItems().get(i);

            assertEquals(item27, item28);
        }
        for (int i = 0; i < test27.getListByteItems().size(); i++) {
            Byte item27 = test27.getListByteItems().get(i);
            Byte item28 = test28.getListByteItems().get(i);

            assertEquals(item27, item28);
        }
        for (int i = 0; i < test27.getListLongItems().size(); i++) {
            Long item27 = test27.getListLongItems().get(i);
            Long item28 = test28.getListLongItems().get(i);

            assertEquals(item27, item28);
        }
        for (int i = 0; i < test27.getListFloatItems().size(); i++) {
            Float item27 = test27.getListFloatItems().get(i);
            Float item28 = test28.getListFloatItems().get(i);

            assertEquals(item27, item28);
        }
        for (int i = 0; i < test27.getListBigDecimalItems().size(); i++) {
            BigDecimal item27 = test27.getListBigDecimalItems().get(i);
            BigDecimal item28 = test28.getListBigDecimalItems().get(i);

            assertEquals(item27, item28);
        }
        for (int i = 0; i < test27.getListBooleanItems().size(); i++) {
            Boolean item27 = test27.getListBooleanItems().get(i);
            Boolean item28 = test28.getListBooleanItems().get(i);

            assertEquals(item27, item28);
        }
        for (int i = 0; i < test27.getListDoubleItems().size(); i++) {
            Double item27 = test27.getListDoubleItems().get(i);
            Double item28 = test28.getListDoubleItems().get(i);

            assertEquals(item27, item28);
        }
        for (int i = 0; i < test27.getListStringItems().size(); i++) {
            String item27 = test27.getListStringItems().get(i);
            String item28 = test28.getListStringItems().get(i);

            assertEquals(item27, item28);
        }
        for (int i = 0; i < test27.getListDateItems().size(); i++) {
            Date item27 = test27.getListDateItems().get(i);
            Date item28 = test28.getListDateItems().get(i);

            assertEquals(item27, item28);
        }
        for (int i = 0; i < test27.getListLocalDateItems().size(); i++) {
            LocalDate item27 = test27.getListLocalDateItems().get(i);
            LocalDate item28 = test28.getListLocalDateItems().get(i);

            assertEquals(item27, item28);
        }
        for (int i = 0; i < test27.getListLocalDateTimeItems().size(); i++) {
            LocalDateTime item27 = test27.getListLocalDateTimeItems().get(i);
            LocalDateTime item28 = test28.getListLocalDateTimeItems().get(i);

            assertEquals(item27, item28);
        }
    }

    @Test
    void test10() {
        KtTest29 test29 = new KtTest29();
        test29.setItems(new ArrayList<>());
        test29.getItems().add(new KtTest17Item("s1"));
        test29.getItems().add(new KtTest17Item("s2"));

        KtTest30 test30 = KtPropertiesUtils.copyProperties(test29, new KtTest30()).build();
        System.out.println(test30);

        for (int i = 0; i < test29.getItems().size(); i++) {
            KtTest17Item item29 = test29.getItems().get(i);
            KtTest18Item item30 = test30.getItems().get(i);

            assertEquals(item29.getS1(), item30.getS1());
        }

        KtTest30 test30_2 = KtPropertiesUtils.copyProperties(test29, KtTest30.class).build();
        System.out.println(test30_2);

        for (int i = 0; i < test29.getItems().size(); i++) {
            KtTest17Item item29 = test29.getItems().get(i);
            KtTest18Item item30 = test30_2.getItems().get(i);

            assertEquals(item29.getS1(), item30.getS1());
        }
    }

    @Test
    void test11() {
        KtTest31<KtTest17Item> test31 = new KtTest31<>();
        test31.setItems(new ArrayList<>());
        test31.getItems().add(new KtTest17Item("s1"));
        test31.getItems().add(new KtTest17Item("s2"));

        KtTest32<KtTest18Item> test32 = KtPropertiesUtils.copyProperties(test31, new KtTest32<KtTest18Item>())
                .mapType(KtTest17Item.class, KtTest18Item.class).build();
        System.out.println(test32);

        assertEquals(test31.getItems().get(0).getS1(), test32.getItems().get(0).getS1());
        assertEquals(test31.getItems().get(1).getS1(), test32.getItems().get(1).getS1());
    }

    @Test
    void test12() {
        KtTest33 test33 = new KtTest33("33");

        KtTest34 test34 = KtPropertiesUtils.copyProperties(test33, new KtTest34()).mapValue((destType, name, val) -> {
            switch (name) {
                case "s1":
                    return Integer.valueOf((String) val);
                default:
                    return null;
            }

        }).build();
        assertEquals(33, test34.getS1());
    }

    @Test
    void test13() {
        List<KtTest33> test33 = new ArrayList<>();
        test33.add(new KtTest33("11"));
        test33.add(new KtTest33("11"));

        List<KtTest34> test34 = KtPropertiesUtils.copyProperties(test33, new ArrayList<KtTest34>())
                .mapType(KtTest33.class, KtTest34.class).mapValue((destType, name, value) -> {
                    switch (name) {
                        case "s1":
                            return Integer.valueOf((String) value);
                        default:
                            return null;
                    }
                }).build();
        System.out.println(test34);

        for (int i = 0; i < test33.size(); i++) {
            // KtTest33 item33 = test33.get(i);
            KtTest34 item34 = test34.get(i);

            assertEquals(11, item34.getS1());
        }
    }

    @Test
    void test14() {
        List<KtTest17Item> test1 = new ArrayList<>();
        test1.add(new KtTest17Item("h1"));
        test1.add(new KtTest17Item("h2"));

        List<KtTest18Item> test2 = KtPropertiesUtils.copyProperties(test1, KtTest18Item.class)
                .mapType(KtTest17Item.class, KtTest18Item.class).build();

        for (int i = 0; i < test1.size(); i++) {
            KtTest17Item item1 = test1.get(i);
            KtTest18Item item2 = test2.get(i);

            assertEquals(item1.getS1(), item2.getS1());
        }
    }

    @Test
    void test15() {
        KtTest35 test35 = new KtTest35();
        test35.setM1(new HashMap<>());
        test35.getM1().put("m1", new KtTest17Item("s1"));
        KtTest36 test36 = new KtTest36();
        test36.setM1(new HashMap<>());
        test36.getM1().put("m2", new KtTest18Item("s2"));

        test36 = KtPropertiesUtils.copyProperties(test35, test36).build();

        assertEquals("s1", test36.getM1().get("m1").getS1());
        assertEquals(null, test36.getM1().get("m2"));
    }

    @Test
    void test16() {
        KtTest37 test37 = new KtTest37();
        test37.setI1(11);
        test37.setS1("s2");
        test37.setItem(new KtTest37Item("ss1", "ss2"));
        test37.setItem_2(new KtTest37Item("ok_ss2", null));

        KtTest38 test38 = new KtTest38();
        test38.setF1(12.2f);
        test38.setS1("s3");
        test38.setItem(new KtTest38Item("xx1", "xx2"));
        test38.setItem_2(new KtTest38Item("ok", "xx2"));

        test38 = KtPropertiesUtils.copyProperties(test37, test38).build();

        assertEquals(12.2f, test38.getF1());
        assertEquals("s2", test38.getS1());
        assertEquals("ss1", test38.getItem().getS1());
        assertEquals("ss2", test38.getItem().getS2());
        assertEquals("ok_ss2", test38.getItem_2().getS1());
        assertEquals(null, test38.getItem_2().getS2());
    }

    @Test
    void test17() {
        Map<String, KtTest39> test39 = new HashMap<>();
        test39.put("m1", new KtTest39("o1", "o2"));
        test39.put("m2", new KtTest39("o3", "o4"));

        Map<String, KtTest40> test40 = KtPropertiesUtils.copyProperties(test39, new HashMap<String, KtTest40>())
                .mapType(KtTest39.class, KtTest40.class)
                .build();

        assertEquals("o1", test40.get("m1").getS1());
        assertEquals("o2", test40.get("m1").getS2());
        assertEquals("o3", test40.get("m2").getS1());
        assertEquals("o4", test40.get("m2").getS2());

        test40 = KtPropertiesUtils.copyProperties(test39, KtTest40.class)
                .build();

        assertEquals("o1", test40.get("m1").getS1());
        assertEquals("o2", test40.get("m1").getS2());
        assertEquals("o3", test40.get("m2").getS1());
        assertEquals("o4", test40.get("m2").getS2());
    }

    @Test
    void test18() {
        Map<String, KtTest39> test39 = new HashMap<>();

        Map<String, KtTest40> test40 = KtPropertiesUtils.copyProperties(test39, new HashMap<String, KtTest40>())
                .mapType(KtTest39.class, KtTest40.class)
                .build();

        assertEquals(0, test40.size());

        test40 = KtPropertiesUtils.copyProperties(test39, KtTest40.class)
                .build();

        assertEquals(0, test40.size());

    }
}
