package com.coder.starwars.util

import com.coder.starwars.util.UnitUtils.cmToFeet
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Class contains test case for Unit conversion.
 */
@RunWith(JUnit4::class)
class UnitUtilsTest {
    /**
     * Verify Centimeter to Ft and Inches conversion success
     */
    @Test
    fun testCmToFeetValid() {
        val expected = Pair("5.65", "67.72")
        Assert.assertEquals(expected, cmToFeet("172"))
        println("Height conversion success.")
    }

    /**
     * Verify Centimeter to Ft and Inches for Invalid height
     */
    @Test
    fun testCmToFeetInvalid() {
        Assert.assertEquals(null, cmToFeet("unknown"))
        println("Invalid height.")
    }

    /**
     * Verify Centimeter to Ft and Inches for Empty height
     */
    @Test
    fun testCmToFeetEmpty() {
        Assert.assertEquals(null, cmToFeet(""))
        println("Empty height.")
    }
}