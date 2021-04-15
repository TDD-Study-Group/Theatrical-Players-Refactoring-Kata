import java.text.NumberFormat

data class LineItem(val name: String, val amount: Int, val audience: Int, val volumeCredits: Int)

class StatementPrinter(val printingStrategy: (List<LineItem>, Invoice) -> String) {

    fun print(invoice: Invoice, plays: Map<String, Play>): String {
        return printingStrategy(calculateLineItems(invoice, plays), invoice)
    }

    private fun calculateLineItems(invoice: Invoice, plays: Map<String, Play>): List<LineItem> {
        val lineItems = mutableListOf<LineItem>()

        invoice.performances.forEach { (playID, audience) ->
            plays[playID]?.let { play ->
                lineItems.add(LineItem(play.name, play.amount(audience), audience, play.volumeCredits(audience)))
            }
        }
        return lineItems
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