import org.slf4j.Logger
import java.util.concurrent.atomic.AtomicLong

class ProgressReporter(private val logger: Logger, val step: Int = 10000) {
    val counter = AtomicLong()

    fun progress() {
        val count = counter.incrementAndGet()
        if (count % step == 0L)
            logger.info("Progress so far: ${String.format("%,d", count)}")
    }

}