package xyz.sushiland.gardenia.mo.gui.controller

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.stage.DirectoryChooser
import tornadofx.Controller
import tornadofx.SortedFilteredList
import tornadofx.alert
import xyz.sushiland.gardenia.mo.GardeniaModManager
import xyz.sushiland.gardenia.mo.Plugin
import xyz.sushiland.gardenia.mo.gui.models.PluginItem
import java.io.File
import java.util.*
import kotlin.system.exitProcess

class Store: Controller() {
    val modManager = GardeniaModManager()
    val items = SortedFilteredList<PluginItem>()

    init {
        if (!modManager.verified) {
            val alert = Alert(
                Alert.AlertType.CONFIRMATION, "Feel free :)"
            )
            alert.title = "Game directory"
            alert.headerText = "Click yes to choose your game directory."
            val result = alert.showAndWait()
            if (result.get() != ButtonType.OK) exitProcess(0)

            val chooser = DirectoryChooser()
            chooser.title = "Choose game directory."
            val stage = primaryStage
            stage.isResizable = false
            val chooseResult = chooser.showDialog(stage) ?: exitProcess(0)
            val dir = chooseResult.absolutePath
            println(dir)
//        modManager.loadState(File(dir))
            modManager.setDirectory(dir)
        }
        modManager.printAll()
        modManager.getPluginListSafe().forEach { this.addPlugin(it) }

    }

    fun addPlugin(newItem: Plugin) {
        items.add(PluginItem(newItem))
    }

    fun removeByUUID(id: UUID?) {
        id?.apply {
            modManager.removeByUUID(this)
            refresh()
        }

    }

    fun renamePluginItem(pgnItem: PluginItem?, newTitle: String) {
        if (pgnItem != null) modManager.renameByUUID(pgnItem.id, newTitle)
        refresh()
    }

    fun refreshModManger() {
        this.modManager.refresh()
        this.refresh()
    }

    private fun refresh() {
        this.items.clear()
        modManager.pluginList.forEach { this.addPlugin(it) }
    }

}