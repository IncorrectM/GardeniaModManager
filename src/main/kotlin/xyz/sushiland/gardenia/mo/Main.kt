package xyz.sushiland.gardenia.mo

import tornadofx.*
import xyz.sushiland.gardenia.mo.gui.Style
import xyz.sushiland.gardenia.mo.gui.view.MainView

fun main(args : Array<String>) {
    launch<MyApp>(args)
}

class MyApp: App(MainView::class, Style::class) {
    init {
        reloadStylesheetsOnFocus()
    }
}
