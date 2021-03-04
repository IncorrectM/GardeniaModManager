package xyz.sushiland.gardenia.mo.gui.view

import javafx.scene.Parent
import tornadofx.View
import tornadofx.borderpane

class MainView(): View("Gardenia Mod Manager") {
    override val root: Parent = borderpane {
        primaryStage.isResizable = false
        top<Header>(Header::class)
        center<CenterPart>(CenterPart::class)
        bottom<Bottom>(Bottom::class)
    }
}