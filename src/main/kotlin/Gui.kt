import Style.Companion.buttonArea
import Style.Companion.quoteArea
import javafx.beans.property.DoubleProperty
import javafx.beans.property.Property
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.CacheHint
import javafx.scene.Parent
import javafx.scene.control.TextFormatter
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.paint.Paint
import javafx.stage.Stage
import javafx.util.converter.IntegerStringConverter
import tornadofx.*

const val START_W = 800.0
const val START_H = 600.0

typealias PropertyGetter = (Region) -> DoubleProperty

class MainScreen : View() {

    private val viewModel = object : ViewModel() {
        val pos = bind { SimpleIntegerProperty(1) }
    }

    init {
        title = "Unified Anti-Divine Astral Quoter"

    }

    private val DIGIT_PATTERN = "\\d*".toRegex()

    val posFormmater = TextFormatter<Int>(IntegerStringConverter(), 1) {
        if (it.text.matches(DIGIT_PATTERN)) it else null
    }


    override val root = hbox {
        minWidthProperty().bind(primaryStage.widthProperty())
        minHeightProperty().bind(primaryStage.heightProperty())
        addClass(Style.root)
        buttonArea()
        quoteArea()
    }


    private fun Parent.quoteArea() = vbox {
        val ba = parent.childrenUnmodifiable.first { it.hasClass(buttonArea) } as Region
        prefWidthProperty().bind((parent as Region).minWidthProperty().subtract(ba.widthProperty()))
        fitToParentHeight()
        addClass(quoteArea)
    }

    private fun Parent.buttonArea() = vbox {
        prefWidthProperty().bind((parent as Region).minWidthProperty() * 0.3)
        fitToParentHeight()
        addClass(buttonArea)
    }

    private fun Region.setChildrenWidths(vararg widths: Int, prop: PropertyGetter = Region::minWidthProperty, childProp: PropertyGetter = prop, parentProp: PropertyGetter = prop) {
        val p = parentProp(this)
        for ((w, c) in widths.zip(childrenUnmodifiable)) {
            if(c is Region) {
                childProp(c).bind(p * (w / 100.0))
            }
        }
    }

}

class Style : Stylesheet() {


    companion object {
        val root by cssclass()
        val quoteArea by cssclass()
        val buttonArea by cssclass()
    }

    init {

        root {
            backgroundColor = multi(c("pink"))
        }

        quoteArea {
            backgroundColor = multi(c("#2C2F33"))
        }
        buttonArea {
            backgroundColor = multi(c("#23272A"))
            maxWidth = 350.px
        }
    }
}

class Gui : App(MainScreen::class, Style::class) {

    override fun start(stage: Stage) {
        super.start(stage.apply {
            width = START_W
            height = START_H

        })
    }

}