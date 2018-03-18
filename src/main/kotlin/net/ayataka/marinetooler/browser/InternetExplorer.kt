package net.ayataka.marinetooler.browser


class InternetExplorer {
    private var process: Process? = null

    init {
        Thread({
            val builder = ProcessBuilder("Player.exe", "start")
            process = builder.start()
            process?.waitFor()
            System.exit(0)
        }).start()
    }

    fun shutdown() {
        process?.destroy()
    }
}