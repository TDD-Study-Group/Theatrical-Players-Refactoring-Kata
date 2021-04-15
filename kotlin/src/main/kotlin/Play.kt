import kotlin.math.floor
import kotlin.math.max

fun play(name: String, type: String): Play {
    return when (type) {
        "tragedy" -> TragedyPlay(name)
        "comedy" -> ComedyPlay(name)
        else -> InvalidPlay(name, type)
    }
}

class InvalidPlay(name: String, override val type: String) : Play(name, type) {
    override fun volumeCredits(audience: Int): Int {
        throw Error("unknown type: $type")
    }

    override fun amount(audience: Int): Int {
        throw Error("unknown type: $type")
    }
}

data class ComedyPlay(override val name: String) : Play(name, "comedy") {
    override fun volumeCredits(audience: Int): Int {
        return max(audience - 30, 0) + floor((audience / 5).toDouble()).toInt()
    }

    override fun amount(audience: Int): Int {
        val thisAmount = 30000 + 300 * audience
        return if (audience > 20) {
            thisAmount + 10000 + 500 * (audience - 20)
        } else {
            thisAmount
        }
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

sealed class Play(open val name: String, open val type: String) {
    abstract fun volumeCredits(audience: Int): Int

    abstract fun amount(audience: Int): Int
}
