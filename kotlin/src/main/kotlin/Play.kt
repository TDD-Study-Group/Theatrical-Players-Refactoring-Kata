import kotlin.math.floor
import kotlin.math.max

data class Play(val name: String, val type: String) {
    fun volumeCredits(audience: Int): Int {
        return when (type) {
            "tragedy" -> {
                max(audience - 30, 0)
            }
            "comedy" -> {
                max(audience - 30, 0) + floor((audience / 5).toDouble()).toInt()
            }
            else -> throw Error("unknown type: {play.type}")
        }
    }
}