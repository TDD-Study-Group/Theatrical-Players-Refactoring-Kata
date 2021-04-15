import java.text.NumberFormat
import kotlin.math.floor
import kotlin.math.max

data class LineItem(val name: String, val amount: Int, val audience: Int, val volumeCredits: Int)

class StatementPrinter(val printigStrat: (MutableList<LineItem>, Invoice) -> String) {

    fun print(invoice: Invoice, plays: Map<String, Play>): String {
        return printigStrat(calculateLineItems(invoice, plays), invoice)
    }

    private fun calculateLineItems(invoice: Invoice, plays: Map<String, Play>): MutableList<LineItem> {
        val lineItems = mutableListOf<LineItem>()

        for ((playID, audience) in invoice.performances) {
            val play = plays[playID]
            var thisAmount = 0
            var volumeCredits = 0

            when (play?.type) {
                "tragedy" -> {
                    thisAmount = 40000
                    if (audience > 30) {
                        thisAmount += 1000 * (audience - 30)
                    }
                    // add volume credits
                    volumeCredits += max(audience - 30, 0)
                }
                "comedy" -> {
                    thisAmount = 30000
                    if (audience > 20) {
                        thisAmount += 10000 + 500 * (audience - 20)
                    }
                    thisAmount += 300 * audience
                    // add volume credits
                    volumeCredits += max(audience - 30, 0)
                    // add extra credit for every ten comedy attendees
                    volumeCredits += floor((audience / 5).toDouble()).toInt()
                }
                else -> throw Error("unknown type: {play.type}")
            }


            lineItems.add(LineItem(play.name, thisAmount, audience, volumeCredits))
        }
        return lineItems
    }

}

private fun printTextStatement(lineItems: List<LineItem>, customer: String, numberFormat: NumberFormat): String {
    val totalAmount = lineItems.sumBy(LineItem::amount)

    var result = "Statement for $customer\n"

    lineItems.forEach { item -> result += "  ${item.name}: ${numberFormat.format((item.amount / 100).toLong())} (${item.audience} seats)\n" }

    result += "Amount owed is ${numberFormat.format((totalAmount / 100).toLong())}\n"
    result += "You earned ${lineItems.sumBy(LineItem::volumeCredits)} credits\n"

    return result
}