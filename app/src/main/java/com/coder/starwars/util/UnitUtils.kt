package com.coder.starwars.util

import com.coder.starwars.util.extensions.divide

/**
 * Utility for units conversion
 */
object UnitUtils {
    fun cmToFeet(heightCentimeters: String?): Pair<String, String>? {
        return heightCentimeters
            ?.toIntOrNull()
            ?.let { Pair(it.divide(30.48), it.divide(2.54)) }
    }
}