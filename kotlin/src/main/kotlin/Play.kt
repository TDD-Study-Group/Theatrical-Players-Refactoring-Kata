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

    fun amount(audience: Int): Int {
        return when (type) {
            "tragedy" -> {
                val thisAmount = 40000
                if (audience > 30) {
                    thisAmount + 1000 * (audience - 30)
                } else {
                    thisAmount
                }
            }
            "comedy" -> {
                val thisAmount = 30000 + 300 * audience
                if (audience > 20) {
                    thisAmount + 10000 + 500 * (audience - 20)
                } else {
                    thisAmount
                }
            }
            else -> throw Error("unknown type: {play.type}")
        }

    }
}