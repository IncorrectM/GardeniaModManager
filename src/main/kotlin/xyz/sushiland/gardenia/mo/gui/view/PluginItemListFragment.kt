package xyz.sushiland.gardenia.mo.gui.view

import javafx.scene.Parent
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.TextInputDialog
import tornadofx.*
import xyz.sushiland.gardenia.mo.gui.controller.Store
import xyz.sushiland.gardenia.mo.gui.models.PluginItem
import xyz.sushiland.gardenia.mo.gui.models.PluginItemModel

class PluginItemListFragment : ListCellFragment<PluginItem>() {
    val store : Store by inject()
    val plugin = PluginItemModel(itemProperty)
    override val root: Parent = vbox {
        hbox {
            form {
                fieldset {
                    field("Title") {
                        label(plugin.title) {
                            style {
                                fontSize = 20.px
                            }
                        }
                    }
                    separator()
                    field("Path") {
                        label(plugin.path) {
                            style {
                                fontSize = 20.px
                            }
                        }
                    }
                } //fieldset
                hbox {
                    button("Remove") {
                        action {
                            println("Delete clicked.")
                            val alert = Alert(
                                Alert.AlertType.CONFIRMATION, "Are you sure to do this? " +
                                        "It's kind like not recoverable."
                            )
                            alert.title = "Confirm"
                            alert.headerText = null
                            val result = alert.showAndWait()
                            if (result.get() == ButtonType.OK) store.removeByUUID(plugin.id.value) else println("Deleting cancelled.")
                        }
                    }
                    button("Rename") {
                        action {
                            val input = TextInputDialog()
                            input.title = "Renaming"
                            input.headerText = "Rename the plugin(mod)."
                            input.contentText = "New title:"
                            val result = input.showAndWait()
                            result.ifPresent {
                                store.renamePluginItem(plugin.item, input.editor.text)
                            }
                        }
                    }
                }
            } //form
        } //hbox
    }
}