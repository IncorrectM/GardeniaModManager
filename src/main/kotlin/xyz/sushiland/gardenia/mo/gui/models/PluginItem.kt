package xyz.sushiland.gardenia.mo.gui.models

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.ItemViewModel
import xyz.sushiland.gardenia.mo.Plugin
import java.util.*

class PluginItem(plugin: Plugin) {
    val id: UUID  = plugin.uuid

    val titleProperty = SimpleStringProperty(plugin.renamedTitle)
    val idProperty = SimpleObjectProperty(plugin.uuid)
    val pathProperty = SimpleStringProperty(plugin.path)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as PluginItem
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}

class PluginItemModel(property: ObjectProperty<PluginItem>): ItemViewModel<PluginItem>(itemProperty = property) {
    val title = bind(autocommit = true) {
        item?.titleProperty
    }
    val path = bind(autocommit = true) {
        item?.pathProperty
    }
    val id = bind(autocommit = true) {
        item?.idProperty
    }
}