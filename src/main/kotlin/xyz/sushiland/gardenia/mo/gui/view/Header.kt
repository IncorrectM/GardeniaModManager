package xyz.sushiland.gardenia.mo.gui.view

import javafx.scene.Parent
import tornadofx.*
import xyz.sushiland.gardenia.mo.gui.Style
import xyz.sushiland.gardenia.mo.gui.controller.Store

class Header: View() {
    val store by inject<Store>()
    override val root: Parent = vbox {
        addClass(Style.header)
        label("Gardenia") {
            addClass(Style.title_h)

        }
    }
}
