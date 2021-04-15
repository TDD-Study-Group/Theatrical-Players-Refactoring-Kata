import java.text.NumberFormat
import java.util.Locale
import kotlin.math.floor
import kotlin.math.max

class StatementPrinter {

    private val numberFormat: NumberFormat
        get() {
            return NumberFormat.getCurrencyInstance(Locale.US)
        }


    data class LineItem(val name: String, val amount: Int, val audience: Int)

    fun print(invoice: Invoice, plays: Map<String, Play>): String {
        var volumeCredits = 0
        val lineItems = mutableListOf<LineItem>()

        for ((playID, audience) in invoice.performances) {
            val play = plays[playID]
            var thisAmount = 0

            when (play?.type) {
                "tragedy" -> {
                    thisAmount = 40000
                    if (audience > 30) {
                        thisAmount += 1000 * (audience - 30)
                    }
                }
                "comedy" -> {
                    thisAmount = 30000
                    if (audience > 20) {
                        thisAmount += 10000 + 500 * (audience - 20)
                    }
                    thisAmount += 300 * audience
                }
                else -> throw Error("unknown type: {play.type}")
            }

            // add volume credits
            volumeCredits += max(audience - 30, 0)
            // add extra credit for every ten comedy attendees
            if ("comedy" == play.type) volumeCredits += floor((audience / 5).toDouble()).toInt()

            lineItems.add(LineItem(play.name, thisAmount, audience))
        }

        return printTextStatement(lineItems, invoice.customer, volumeCredits)
    }

    private fun printTextStatement(
        lineItems: MutableList<LineItem>,
        customer: String,
        volumeCredits: Int
    ): String {
        val totalAmount = lineItems.sumBy(LineItem::amount)

        var result = "Statement for $customer\n"

        lineItems.forEach { item -> result += "  ${item.name}: ${numberFormat.format((item.amount / 100).toLong())} (${item.audience} seats)\n" }

        result += "Amount owed is ${numberFormat.format((totalAmount / 100).toLong())}\n"
        result += "You earned $volumeCredits credits\n"

        return result
    }

}
