package xyz.sushiland.gardenia.mo.gui.view

import javafx.scene.Parent
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import tornadofx.*
import xyz.sushiland.gardenia.mo.FileOperation
import xyz.sushiland.gardenia.mo.GardeniaModManager
import xyz.sushiland.gardenia.mo.gui.controller.Store
import kotlin.system.exitProcess

class Bottom : View() {
    val store: Store by inject()
    override val root: Parent = vbox {
        menubar {
            menu("File") {
                item("Install") {
                    action {

                        val chooser = FileChooser()
                        chooser.title = "Choose file path(.dll or .zip)."
                        val stage = primaryStage
                        stage.isResizable = false
                        val chooseResult = chooser.showOpenDialog(stage)
                        if (chooseResult != null){
                            val dir = chooseResult.absolutePath
                            store.installPlugin(dir)
                        } else {
                            println("No file chosen. Installation cancelled")
                        }
                    }
                }
                item("Uninstall") {
                    action {
                        alert(Alert.AlertType.INFORMATION, "Hey!", "The remove button is up there ↑")
                    }
                }
            }
            menu("View") {
                item("Refresh") {
                    action {
                        store.refreshModManger()
                    }
                }
                item("Wipe data") {
                    action {
                        val alert = Alert(
                            Alert.AlertType.CONFIRMATION, "Are you sure?"
                        )
                        alert.title = "Wipe data"
                        alert.headerText = "It's unrecoverable and need a restart of th manager.Your mods won't be wiped."
                        val result = alert.showAndWait()
                        if (result.get() == ButtonType.OK) store.modManager.wipe()
                    }
                }
            }
            menu("Help") {
                menu("Contact") {
                    item("Email") {
                        action {
                            alert(
                                Alert.AlertType.INFORMATION,
                                "${GardeniaModManager.INFORMATION[GardeniaModManager.Information.EMAIL]}",
                                "Feel free to contact me if you've met any questions."
                            )
                        }
                    }}
            }
        }
    }
}