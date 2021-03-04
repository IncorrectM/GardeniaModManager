package xyz.sushiland.gardenia.mo.gui.view

import javafx.scene.Parent
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.TextInputDialog
import tornadofx.*
import xyz.sushiland.gardenia.mo.gui.controller.Store

class CenterPart : View() {
    val store: Store by inject()
    override val root: Parent = hbox {
        listview(store.items) {
            prefWidth = 1080.0
            prefHeight = 600.0
            isEditable = false
            cellFragment(PluginItemListFragment::class)
        }
    }
}