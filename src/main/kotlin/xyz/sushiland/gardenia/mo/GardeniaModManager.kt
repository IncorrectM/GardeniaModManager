package xyz.sushiland.gardenia.mo

import com.beust.klaxon.Klaxon
import xyz.sushiland.gardenia.mo.exception.ModManagerNotVeridiedException
import xyz.sushiland.gardenia.mo.exception.NotADirectoryException
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

class GardeniaModManager(loadState: Boolean = true) {
    var verified = false
    val pluginList = ArrayList<Plugin>()
    lateinit var rootPath: String
    val nameList = ArrayList<String>()
    val uuidList = ArrayList<UUID>()
    lateinit var bieFile : File //BepInEx file
    lateinit var pgnFile : File //plugins file

    constructor(ver: Boolean, pluginList: ArrayList<Plugin>, rootPath: String) : this(false) {
        this.verified = ver
        this.rootPath = rootPath
        pluginList.forEach { this.pluginList.add(it) }
    }

    init {
        if (loadState){
            val load = File(DEFAULT_SETTING_DIRECTORY)
            if (load.exists()) {
                loadState(load)
                initPath()
                refresh()
            }
        }
    }

    companion object {
        const val BEP_IN_EX_NAME = "BepInEx"
        const val DEFAULT_SETTING_DIRECTORY = "./gmm_settings.json"
        val INFORMATION = mapOf<Information, String>(Information.EMAIL to "zzhefordu@qq.com")

    }

    fun getSavingModel() : GMMSaving {
        val array = Array<Plugin>(this.pluginList.size) { index -> Plugin("T_PLACE_HOLDER_$index", "P_PLACE_HOLDER_$index") }
        this.pluginList.forEachIndexed{index, plugin ->
            array[index] = plugin
        }
        return GMMSaving(this.rootPath, array, this.verified)
    }

    fun getPluginListSafe() : ArrayList<Plugin>{
        if (!verified) throw ModManagerNotVeridiedException("Verify the mod manager before using it.")
        return this.pluginList
    }

    fun setDirectory(rootPath: String) {
        verify(rootPath)
        verified = true
    }

    fun verify(rootPath: String) {
        verified = true
        this.rootPath = rootPath
        initPath()
        refresh()
        saveState()

    }

    fun registerPlugin(dir: String): State {
        if (!verified) throw ModManagerNotVeridiedException("Verify the mamager before using it.")
        var newFile = File(dir)
        if (newFile.exists()) {
            var newItem = Plugin(newFile.nameWithoutExtension, newFile.absolutePath)
            this.pluginList.add(newItem)
            this.nameList.add(newFile.nameWithoutExtension)
            this.uuidList.add(newItem.uuid)
        } else {
            println("\"${newFile.absolutePath} is not available.")
            return State.FILE_NOT_EXIST
        }
        saveState()
        return State.FILE_FOUND

    }

    fun registerPlugins(): State {
        if (!verified) throw ModManagerNotVeridiedException("Verify the mamager before using it.")
        println(pgnFile)
        pgnFile.walk().maxDepth(1).filter { it.isFile }.filter { it.extension == "dll" }
            .forEach {
                val file = it
                println(file)
                var exists = false
                pluginList.forEach { if (file.path == it.path) exists = true }
                if (!exists) {
                    this.registerPlugin(file.absolutePath)
                }

        }
        saveState()
        return State.SUC

    }

    fun remove(name: String): State {
        if (!verified) throw ModManagerNotVeridiedException("Verify the manager before using it.")
        return if (nameList.contains(name)) {
            pluginList.forEach { if (it.renamedTitle == name) {
                pluginList.remove(it)
                nameList.remove(it.renamedTitle)
                uuidList.remove(it.uuid)
                File(it.path).delete()
                }
            }
            saveState()
            State.SUC
        } else {
            State.DIR_NOT_REGISTERED
        }
    }

    fun removeByUUID(id: UUID) {
        pluginList.forEach {
            if (it.uuid == id) {
                pluginList.remove(it)
                nameList.remove(it.renamedTitle)
                uuidList.remove(it.uuid)
                File(it.path).delete()
                saveState()
                return
            }
        }

    }

    fun rename(primaryPlugin: Plugin, newTitle: String) {
        if (!verified) throw ModManagerNotVeridiedException("Verify the mamager before using it.")
        findPluginByUUID(primaryPlugin.uuid)?.apply {
            this.renamedTitle = newTitle
            this@GardeniaModManager.nameList.remove(primaryPlugin.renamedTitle)
            this@GardeniaModManager.nameList.add(this.renamedTitle)
        }
        println("#----------#")
        this.printAll()
        //namlist and pluginlistto deal with
        saveState()
    }

    fun renameByUUID(id: UUID, newTitle: String) {
        findPluginByUUID(id)?.apply {
            this@GardeniaModManager.nameList.remove(this.renamedTitle)
            this.renamedTitle = newTitle
            this@GardeniaModManager.nameList.add(this.renamedTitle)
        }
        println("#----------#")
        this.printAll()
        saveState()
    }

    fun findPluginByUUID(id: UUID): Plugin? {
        this.pluginList.forEach { if (it.uuid == id) return it }
        return null
    }

    fun addPlugin(plugin: Plugin) {
        this.pluginList.add(plugin)
        this.nameList.add(plugin.renamedTitle)
        this.uuidList.add(plugin.uuid)
    }

    fun printAll() {
        if (!verified) throw ModManagerNotVeridiedException("Verify the mamager before using it.")
        this.pluginList.forEach {
            println("Name: ${it.renamedTitle}, Path: ${it.path}")
        }
    }

    fun refresh() {
        registerPlugins()
        deletePluginsNotExists()
    }

    fun wipe() {
        val saveFile = File(DEFAULT_SETTING_DIRECTORY)
        if (saveFile.exists()) {
            println("Deleting saving file.")
            saveFile.delete()
        }
        exitProcess(0)
    }

    private fun saveState() {
        val save = File(DEFAULT_SETTING_DIRECTORY)
        if (!save.exists()) save.createNewFile()
        save.writeText(Klaxon().toJsonString(this.getSavingModel()))
        //todo: complete this

    }

    fun loadState(file: File) {
        val text = file.readText()
        println(text)
        if (text == "") {
            file.delete()
            return
        }

        val loadedModManager = Klaxon().parse<GMMSaving>(text)
        loadedModManager?.apply {
            unpack(loadedModManager)
            this@GardeniaModManager.verified = true
        }
        //todo: complete this

    }

    private fun deletePluginsNotExists() {
        var toRemove = ArrayList<Plugin>()
        pluginList.forEach {
            if (!File(it.path).exists()) {
                toRemove.add(it)
                nameList.remove(it.renamedTitle)
                uuidList.remove(it.uuid)
            }
        }
        pluginList.removeAll(toRemove)
    }

    private fun initPath() {
        println("Root: $rootPath")
        var bieexits = false
        val rootFile = File(this.rootPath)
        if (rootFile.isFile) throw NotADirectoryException("Directory needed.")
        rootFile.walk().maxDepth(1).forEach { if (it.name == BEP_IN_EX_NAME) bieexits = true }
        if (!bieexits) {
            println("BepInEx not found in \"${rootFile.path}\".")
            exitProcess(-1)
        }
        bieFile = File(if (rootFile.absolutePath.endsWith('/')) "${rootFile.absolutePath}$BEP_IN_EX_NAME"
        else "${rootFile.absolutePath}/$BEP_IN_EX_NAME")
//        println("bie $bieFile")
        pgnFile = File(if (bieFile.absolutePath.endsWith('/')) "${bieFile.absolutePath}/plugins"
        else "${bieFile.absolutePath}/plugins")
//        println("pgn ${pgnFile}")
        if (!pgnFile.exists()) pgnFile.mkdir()

    }

    private fun unpack(saving: GMMSaving) {
        this.verified = saving.verified
        this.rootPath = saving.rootPath
        saving.pluginList.forEach {
            println(it)
            this.addPlugin(it)
        }
    }

    enum class State {
        SUC,
        FILE_FOUND,
        FILE_NOT_EXIST,
        DIR_NOT_REGISTERED,
    }

    enum class Information {
        CONTACT,EMAIL
    }

}

data class Plugin(val registeredTitle: String, val path: String, var renamedTitle: String = registeredTitle){
    val uuid = UUID.randomUUID()
}

data class GMMSaving(val rootPath: String, val pluginList: Array<Plugin>, val verified: Boolean) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GMMSaving

        if (rootPath != other.rootPath) return false
        if (!pluginList.contentEquals(other.pluginList)) return false
        if (verified != other.verified) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rootPath.hashCode()
        result = 31 * result + pluginList.contentHashCode()
        result = 31 * result + verified.hashCode()
        return result
    }

}
