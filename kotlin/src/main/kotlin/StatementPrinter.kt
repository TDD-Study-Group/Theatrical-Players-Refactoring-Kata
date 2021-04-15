import java.text.NumberFormat
import kotlin.math.floor
import kotlin.math.max

data class LineItem(val name: String, val amount: Int, val audience: Int, val volumeCredits: Int)

class StatementPrinter(val printingStrategy: (List<LineItem>, Invoice) -> String) {

    fun print(invoice: Invoice, plays: Map<String, Play>): String {
        return printingStrategy(calculateLineItems(invoice, plays), invoice)
    }

    private fun calculateLineItems(invoice: Invoice, plays: Map<String, Play>): List<LineItem> {
        val lineItems = mutableListOf<LineItem>()

        for ((playID, audience) in invoice.performances) {
            val play = plays[playID]
            play?.let {
                lineItems.add(LineItem(play.name, play.amount(audience), audience, play.volumeCredits(audience)))
            }
        }
        return lineItems
    }

    fun Play.amount(audience: Int): Int {
        var thisAmount: Int
        when (type) {
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
        return thisAmount
    }

}

fun printTextStatement(lineItems: List<LineItem>, customer: String, numberFormat: NumberFormat): String {
    val totalAmount = lineItems.sumBy(LineItem::amount)

    var result = "Statement for $customer\n"

    lineItems.forEach { item -> result += "  ${item.name}: ${numberFormat.format((item.amount / 100).toLong())} (${item.audience} seats)\n" }

    result += "Amount owed is ${numberFormat.format((totalAmount / 100).toLong())}\n"
    result += "You earned ${lineItems.sumBy(LineItem::volumeCredits)} credits\n"

    return result
}