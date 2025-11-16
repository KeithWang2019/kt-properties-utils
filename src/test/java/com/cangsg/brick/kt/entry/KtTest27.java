package com.cangsg.brick.kt.entry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KtTest27 {
    private List<Integer> listIntegerItems;
    private List<Byte> listByteItems;
    private List<Long> listLongItems;
    private List<Float> listFloatItems;
    private List<BigDecimal> listBigDecimalItems;
    private List<Boolean> listBooleanItems;
    private List<Double> listDoubleItems;
    private List<String> listStringItems;
    private List<Date> listDateItems;
    private List<LocalDate> listLocalDateItems;
    private List<LocalDateTime> listLocalDateTimeItems;
}
