package net.ayataka.marinetooler.gui

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.network.PacketDirection
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.utils.fromHexToBytes
import net.ayataka.marinetooler.utils.toHexString
import java.net.URL
import java.nio.ByteBuffer
import java.util.*

class PacketDialog : Initializable {
    @FXML
    lateinit var rawTextArea: TextArea
    @FXML
    lateinit var textTextArea: TextArea
    @FXML
    lateinit var repeatRadioButton: RadioButton
    @FXML
    lateinit var amountRadioButton: RadioButton
    @FXML
    lateinit var amountSpinner: Spinner<Int>
    @FXML
    lateinit var timerDelaySpinner: Spinner<Int>
    @FXML
    lateinit var playButton: Button
    @FXML
    lateinit var progressLabel: Label

    private lateinit var direction: PacketDirection
    private lateinit var server: ServerType
    private var thread: Thread? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        amountRadioButton.isSelected = true
        amountSpinner.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999)
        timerDelaySpinner.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(50, 9999)
        progressLabel.text = ""

        rawTextArea.setOnKeyTyped {
            try {
                textTextArea.text = String(rawTextArea.text.fromHexToBytes())
            } catch (ex: Exception) {
            }
        }

        repeatRadioButton.setOnAction {
            amountRadioButton.isSelected = false
        }

        amountRadioButton.setOnAction {
            repeatRadioButton.isSelected = false
        }

        timerDelaySpinner.focusedProperty().addListener { _, _, newValue ->
            if (!newValue) {
                timerDelaySpinner.increment(0)
            }
        }

        amountSpinner.focusedProperty().addListener { _, _, newValue ->
            if (!newValue) {
                amountSpinner.increment(0)
            }
        }

        playButton.setOnAction {
            if (thread?.isAlive == true) {
                repeatRadioButton.isDisable = false
                amountRadioButton.isDisable = false
                rawTextArea.isDisable = false
                amountSpinner.isDisable = false
                timerDelaySpinner.isDisable = false
                playButton.text = "スタート"
                progressLabel.text = ""

                thread?.interrupt()
                thread = null
                return@setOnAction
            }

            val delay = timerDelaySpinner.value.toLong()
            val amount = if (repeatRadioButton.isSelected) Int.MAX_VALUE else amountSpinner.value
            val data = rawTextArea.text.fromHexToBytes()

            println("Delay: $delay")
            println("Amount: $amount")
            println("Data: ${data.toHexString()}")

            repeatRadioButton.isDisable = true
            amountRadioButton.isDisable = true
            rawTextArea.isDisable = true
            amountSpinner.isDisable = true
            timerDelaySpinner.isDisable = true
            playButton.text = "ストップ"
            progressLabel.text = "0 / ${if(amount == Int.MAX_VALUE) "∞" else amount}"

            thread = Thread {
                repeat(amount) {
                    if (direction == PacketDirection.RECEIVE) {
                        Pigg.proxies[server]?.receive(ByteBuffer.wrap(data))
                    }

                    if (direction == PacketDirection.SEND) {
                        Pigg.proxies[server]?.send(ByteBuffer.wrap(data))
                    }

                    Platform.runLater {
                        progressLabel.text = "$it / ${if (amount == Int.MAX_VALUE) "∞" else amount}"
                    }

                    Thread.sleep(delay)
                }

                Platform.runLater {
                    repeatRadioButton.isDisable = false
                    amountRadioButton.isDisable = false
                    rawTextArea.isDisable = false
                    amountSpinner.isDisable = false
                    timerDelaySpinner.isDisable = false
                    playButton.text = "スタート"
                    progressLabel.text = ""
                }
            }

            thread?.start()
        }
    }

    fun load(packet: PacketView) {
        direction = PacketDirection.valueOf(packet.direction)
        server = ServerType.valueOf(packet.server)

        rawTextArea.text = packet.data
        textTextArea.text = String(packet.data.fromHexToBytes())
    }
}