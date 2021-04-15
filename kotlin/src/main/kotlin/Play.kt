import kotlin.math.floor
import kotlin.math.max

fun play(name: String, type: String): Play {
    return when (type){
        "tragedy" -> TragedyPlay(name)
        else -> Play(name, type)
    }
}

data class TragedyPlay(override val name: String) : Play(name, "tragedy") {
    override fun volumeCredits(audience: Int): Int {
        return max(audience - 30, 0)
    }

    override fun amount(audience: Int): Int {
        val thisAmount = 40000
        return if (audience > 30) {
            thisAmount + 1000 * (audience - 30)
        } else {
            thisAmount
        }
    }
}

open class Play(open val name: String, private val type: String) {
    open fun volumeCredits(audience: Int): Int {
        return when (type) {
            "comedy" -> {
                max(audience - 30, 0) + floor((audience / 5).toDouble()).toInt()
            }
            else -> throw Error("unknown type: $type")
        }
    }

    open fun amount(audience: Int): Int {
        return when (type) {
            "comedy" -> {
                val thisAmount = 30000 + 300 * audience
                if (audience > 20) {
                    thisAmount + 10000 + 500 * (audience - 20)
                } else {
                    thisAmount
                }
            }
            else -> throw Error("unknown type: $type")
        }
    }
}