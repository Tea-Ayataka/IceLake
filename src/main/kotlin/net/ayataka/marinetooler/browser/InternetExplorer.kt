package net.ayataka.marinetooler.browser


class InternetExplorer {
    private var process: Process? = null

    init {
        Thread({
            val builder = ProcessBuilder("Player.exe", "start")
            this.process = builder.start()
            this.process?.waitFor()
            System.exit(0)
        }).start()
    }

    fun shutdown() {
        this.process?.destroy()
    }
}