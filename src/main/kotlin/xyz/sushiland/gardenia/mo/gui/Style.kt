package xyz.sushiland.gardenia.mo.gui

import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.c
import tornadofx.cssclass
import tornadofx.px

class Style : Stylesheet() {

    companion object {
        val header by cssclass()
        val title_h by cssclass()
    }

    init {
        header {
            alignment = Pos.CENTER
        }

        title_h {
            textFill = c("#9370D8")
            fontSize = 50.px
            fontWeight = FontWeight.BOLD
        }
    }
}