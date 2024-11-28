package com.example.sampleproject

import java.math.BigDecimal

data class SampleRequest(
    val sample1: String?,
    val sample2: BigDecimal?,
    val sample3: BigDecimal?
) {
    init {
        if (sample1 == "A") {
            require(sample2 != null && sample3 != null) {
                "에러에러"
            }
        }
    }
}