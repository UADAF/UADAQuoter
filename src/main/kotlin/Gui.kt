import Style.Companion.buttonArea
import Style.Companion.gear
import Style.Companion.quoteArea
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Parent
import javafx.scene.control.TextFormatter
import javafx.scene.image.Image
import javafx.scene.layout.Region
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import javafx.stage.Stage
import javafx.util.converter.IntegerStringConverter
import tornadofx.*

const val START_W = 800.0
const val START_H = 600.0

typealias PropertyGetter = (Region) -> DoubleProperty
private val oswaldFont: Font = loadFont("/Oswald-Regular.ttf", 24.0)!!
class MainScreen : View() {

    private val viewModel = object : ViewModel() {
        val pos = bind { SimpleIntegerProperty(1) }
    }

    private val gear: Image

    init {
        title = "Unified Anti-Divine Astral Quoter"
        gear = Image(javaClass.getResourceAsStream("/gear.png"))
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
        logo()
    }

    private fun Parent.logo() = hbox {
        fitToParentWidth()
        addClass(Style.logo)
        val g = imageview(gear) {
            addClass(Style.gear)
            fitWidth = 64.0
            fitHeight = 64.0
        }
        label("UADAQ") {
            minHeightProperty().bind(g.fitHeightProperty())
            minWidthProperty().bind((parent as Region).minWidthProperty().subtract(g.fitWidthProperty()))
            addClass(Style.title)
        }
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
        val logo by cssclass()
        val gear by cssclass()
        val title by cssclass()
    }

    init {

        root {
            backgroundColor = multi(c("#2C2F33"))
        }

        quoteArea {
            backgroundColor = multi(c("#2C2F33"))
        }
        buttonArea {
            backgroundColor = multi(c("#23272A"))
            maxWidth = 350.px
        }

        logo {
            padding = box(15.px, 0.px, 15.px, 15.px)
        }

        title {
            padding = box(0.px, 30.px, 0.px, 0.px)
            alignment = Pos.CENTER
            textFill = c("#9d9d9d")
            font = oswaldFont
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