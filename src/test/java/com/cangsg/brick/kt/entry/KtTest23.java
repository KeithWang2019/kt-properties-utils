package com.cangsg.brick.kt.entry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KtTest23 {
    private int[] arrInt;
    private Integer[] arrInteger;
    private byte[] arrByte;
    private Byte[] arrBByte;
    private long[] arrLong;
    private Long[] arrLLong;
    private float[] arrFloat;
    private Float[] arrFFloat;
    private BigDecimal[] arrBigDecimal;
    private boolean[] arrBoolean;
    private Boolean[] arrBBoolean;
    private double[] arrDouble;
    private Double[] arrDDouble;    
    private String[] arrString;
    private Date[] arrDate;
    private LocalDate[] arrLocalDate;
    private LocalDateTime[] arrLocalDateTime;
}
