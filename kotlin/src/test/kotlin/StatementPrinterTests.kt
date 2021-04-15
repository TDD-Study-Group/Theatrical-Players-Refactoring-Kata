
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.approvaltests.Approvals.verify
import java.text.NumberFormat
import java.util.*

class StatementPrinterTests {

    @Test
    internal fun exampleStatement() {

        val plays = mapOf(
            "hamlet" to play("Hamlet", "tragedy"),
            "as-like" to play("As You Like It", "comedy"),
            "othello" to play("Othello", "tragedy")
        )

        val invoice = Invoice(
            "BigCo", listOf(
                Performance("hamlet", 55),
                Performance("as-like", 35),
                Performance("othello", 40)
            )
        )

        val statementPrinter = StatementPrinter { lineItems, invoice ->
            printTextStatement(
                lineItems,
                invoice.customer,
                NumberFormat.getCurrencyInstance(Locale.US)
            )
        }
        val result = statementPrinter.print(invoice, plays)

        verify(result)
    }

    @Test
    internal fun statementWithNewPlayTypes() {
        val plays = mapOf(
            "henry-v" to play("Henry V", "history"),
            "as-like" to play("As You Like It", "pastoral")
        )

        val invoice = Invoice(
            "BigCo", listOf(
                Performance("henry-v", 53),
                Performance("as-like", 55)
            )
        )

        val statementPrinter = StatementPrinter { lineItem, invoice ->
            printTextStatement(
                lineItem,
                invoice.customer,
                NumberFormat.getCurrencyInstance(Locale.US)
            )
        }
        assertThrows(Error::class.java) { statementPrinter.print(invoice, plays) }
    }
}
