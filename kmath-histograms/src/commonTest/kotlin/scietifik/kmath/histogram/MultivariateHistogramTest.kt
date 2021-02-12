package scietifik.kmath.histogram

import kscience.kmath.histogram.RealHistogramSpace
import kscience.kmath.histogram.fill
import kscience.kmath.histogram.put
import kscience.kmath.real.RealVector
import kscience.kmath.real.invoke
import kotlin.random.Random
import kotlin.test.*

internal class MultivariateHistogramTest {
    @Test
    fun testSinglePutHistogram() {
        val hSpace = RealHistogramSpace.fromRanges(
            (-1.0..1.0),
            (-1.0..1.0)
        )
        val histogram = hSpace.produce {
            put(0.55, 0.55)
        }
        val bin = histogram.bins.find { it.value.toInt() > 0 } ?: fail()
        assertTrue { bin.contains(RealVector(0.55, 0.55)) }
        assertTrue { bin.contains(RealVector(0.6, 0.5)) }
        assertFalse { bin.contains(RealVector(-0.55, 0.55)) }
    }

    @Test
    fun testSequentialPut() {
        val hSpace = RealHistogramSpace.fromRanges(
            (-1.0..1.0),
            (-1.0..1.0),
            (-1.0..1.0)
        )
        val random = Random(1234)

        fun nextDouble() = random.nextDouble(-1.0, 1.0)

        val n = 10000
        val histogram = hSpace.produce {
            fill {
                repeat(n) {
                    yield(RealVector(nextDouble(), nextDouble(), nextDouble()))
                }
            }
        }
        assertEquals(n, histogram.bins.sumBy { it.value.toInt() })
    }
}