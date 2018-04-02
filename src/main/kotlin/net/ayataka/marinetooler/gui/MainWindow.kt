package net.ayataka.marinetooler.gui

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.Clipboard
import javafx.scene.input.DataFormat
import javafx.scene.input.KeyCode
import net.ayataka.eventapi.EventListener
import net.ayataka.eventapi.EventManager
import net.ayataka.marinetooler.Tooler
import net.ayataka.marinetooler.module.impl.*
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.event.ConnectEvent
import net.ayataka.marinetooler.pigg.event.DisconnectEvent
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.GetUserProfileResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.LoginChatResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.ClickPiggShopItemPacket
import net.ayataka.marinetooler.pigg.network.packet.send.MoveEndPacket
import org.controlsfx.control.StatusBar
import java.net.URL
import java.util.*

class MainWindow : Initializable {
    @FXML
    lateinit var logBox: TextArea
    @FXML
    lateinit var statusBar: StatusBar
    @FXML
    lateinit var tpButton: Button
    @FXML
    lateinit var tpX: Spinner<Int>
    @FXML
    lateinit var tpY: Spinner<Int>
    @FXML
    lateinit var tpZ: Spinner<Int>
    @FXML
    lateinit var clickTPChk: CheckBox
    @FXML
    lateinit var skeetChk: CheckBox
    @FXML
    lateinit var moveGhost: CheckBox
    @FXML
    lateinit var targetSet: CheckBox
    @FXML
    lateinit var targetPic: ImageView
    @FXML
    lateinit var targetNick: Label
    @FXML
    lateinit var targetId: Label
    @FXML
    lateinit var targetUsercode: Label
    @FXML
    lateinit var targetDetails: Button
    @FXML
    lateinit var areaName: Label
    @FXML
    lateinit var areaCode: Label
    @FXML
    lateinit var areaSize: Label
    @FXML
    lateinit var areaPos: Label
    @FXML
    lateinit var areaDetails: Button
    @FXML
    lateinit var colorChat: CheckBox
    @FXML
    lateinit var colorChatColor: ColorPicker
    @FXML
    lateinit var chatGhost: CheckBox
    @FXML
    lateinit var actionGhost: CheckBox
    @FXML
    lateinit var ngBypass: CheckBox
    @FXML
    lateinit var autoGoodPigg: CheckBox
    @FXML
    lateinit var slotMacro: ToggleButton
    @FXML
    lateinit var skipTutorial: Button
    @FXML
    lateinit var freeGacha: Button
    @FXML
    lateinit var actionList: ListView<String>
    @FXML
    lateinit var runAction: MenuItem
    @FXML
    lateinit var copyAction: MenuItem
    @FXML
    lateinit var delAction: MenuItem
    @FXML
    lateinit var command: TextField
    @FXML
    lateinit var instantDonate: CheckBox
    @FXML
    lateinit var gumiPoint: CheckBox

    init {
        EventManager.register(this)
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        Tooler.mainWindow = this

        tpX.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000)
        tpY.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000)
        tpZ.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000)

        //actionList.items.addAll(File("action").readLines())

        // TODO: Refactor
        command.setOnKeyPressed {
            if (it.code != KeyCode.ENTER)
                return@setOnKeyPressed

            val text = (it.source as TextField).text
            Command.doCommand(text)
            logBox.appendText("> $text\n")
            (it.source as TextField).text = ""
        }

        runAction.setOnAction {
            CurrentUser.playAction(actionList.selectionModel.selectedItems.first())
        }

        copyAction.setOnAction {
            Clipboard.getSystemClipboard().setContent(mapOf(Pair(DataFormat.PLAIN_TEXT, actionList.selectionModel.selectedItems.first())))
        }

        delAction.setOnAction {
            actionList.items.remove(actionList.selectionModel.selectedItems.first())
        }

        skipTutorial.setOnAction {
            SkipTutorial.skip()
        }

        autoGoodPigg.setOnAction {
            AutoGoodPigg.enabled = (it.source as CheckBox).isSelected
        }

        slotMacro.setOnAction {
            SlotMacro.enabled = (it.source as ToggleButton).isSelected
        }

        ngBypass.setOnAction {
            NGBypass.enabled = (it.source as CheckBox).isSelected
        }

        moveGhost.setOnAction {
            MoveGhost.enabled = (it.source as CheckBox).isSelected
        }

        chatGhost.setOnAction {
            ChatGhost.enabled = (it.source as CheckBox).isSelected
        }

        chatGhost.setOnAction {
            ChatGhost.enabled = (it.source as CheckBox).isSelected
        }

        actionGhost.setOnAction {
            ActionGhost.enabled = (it.source as CheckBox).isSelected
        }

        colorChat.setOnAction {
            ColorChat.enabled = (it.source as CheckBox).isSelected
        }

        colorChatColor.setOnAction {
            ColorChat.color = (it.source as ColorPicker).value
        }

        clickTPChk.setOnAction {
            ClickTP.enabled = (it.source as CheckBox).isSelected
        }

        skeetChk.setOnAction {
            Skeet.enabled = (it.source as CheckBox).isSelected
        }

        freeGacha.setOnAction {
            MuryouGatyaZenkaihou.enabled = true
        }

        instantDonate.setOnAction {
            InstantDonator.enabled = (it.source as CheckBox).isSelected
        }

        gumiPoint.setOnAction {
            PuzzleZousyoku.enabled = (it.source as CheckBox).isSelected
        }

        // Instant teleport
        tpX.valueFactory.valueProperty().addListener { _, _, new ->
            CurrentUser.teleport(new, tpY.value, tpZ.value, 0)
        }
        tpY.valueFactory.valueProperty().addListener { _, _, new ->
            CurrentUser.teleport(tpX.value, new, tpZ.value, 0)
        }
        tpZ.valueFactory.valueProperty().addListener { _, _, new ->
            CurrentUser.teleport(tpX.value, tpY.value, new, 0)
        }

        // Enable default modules
        Command.enabled = true
        AutoGoodPigg.enabled = true
        NGBypass.enabled = true
    }

    fun log(msg: String) {
        Platform.runLater({
            logBox.appendText(msg + "\n")
        })
    }

    @EventListener
    fun onConnect(event: ConnectEvent) {
        Platform.runLater({
            statusBar.text = "  Connecting..."
        })
    }

    @EventListener
    fun onDisconnect(event: DisconnectEvent) {
        Platform.runLater({
            statusBar.text = "  Disconnected."
        })
    }

    @EventListener
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet

        Platform.runLater({
            if (packet is LoginChatResultPacket) {
                statusBar.text = "  Connected."
            }

            if (targetSet.isSelected && packet is GetUserProfileResultPacket) {
                targetSet.isSelected = false

                Tooler.targetUser = packet.usercode
                targetNick.text = "ニックネーム  ：　${packet.nickName}"
                targetUsercode.text = "ユーザーコード：　${packet.usercode}"
                if (packet.amebaId.isEmpty()) {
                    targetId.text = "アメーバID    ：　<未設定>"
                    targetPic.image = Image("default-pigg.png")
                } else {
                    targetId.text = "アメーバID    ：　${packet.amebaId}"
                    targetPic.image = Image("http://origin.contents.pigg.ameba.jp/api/84/user/${packet.amebaId}/image?type=png")
                }
            }

            if (packet is BaseAreaData) {
                areaName.text = "名前         ：　${packet.areaData.areaName}"
                areaSize.text = "サイズ        ：　${packet.areaData.sizeX} x ${packet.areaData.sizeY}"
                areaCode.text = "エリアコード ：　${packet.areaData.categoryCode}/${packet.areaData.areaCode}"
            }

            // Collect action
            if (packet is ActionResultPacket && !packet.actionCode.contains("\u0000")) {
                /*  val file = File("action")
                  var actions = file.readText()
                  if (!actions.contains(packet.actionCode)) {
                      actions += packet.actionCode + "\n"
                  }
                  file.writeText(actions)*/

                if (!actionList.items.contains(packet.actionCode)) {
                    actionList.items.add(packet.actionCode)
                }
            }
        })
    }

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        Platform.runLater({
            if (packet is MoveEndPacket) {
                areaPos.text = "座標         ：　X${packet.x} Y${packet.y} Z${packet.z}"
            }

            if (packet is ClickPiggShopItemPacket && packet.itemType == "action") {
                if (!actionList.items.contains(packet.itemCode)) {
                    actionList.items.add(packet.itemCode)
                }
            }
        })
    }
}
