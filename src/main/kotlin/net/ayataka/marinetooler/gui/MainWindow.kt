package net.ayataka.marinetooler.gui

import com.darkmagician6.eventapi.EventManager
import com.darkmagician6.eventapi.EventTarget
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.Clipboard
import javafx.scene.input.DataFormat
import javafx.scene.input.KeyCode
import net.ayataka.marinetooler.Tooler
import net.ayataka.marinetooler.module.impl.*
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.event.ConnectEvent
import net.ayataka.marinetooler.pigg.event.DisconnectEvent
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.data.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.GetUserProfileResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.LoginChatResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.ClickPiggShopItemPacket
import net.ayataka.marinetooler.pigg.network.packet.send.MoveEndPacket
import org.controlsfx.control.StatusBar
import java.io.File
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

    init {
        EventManager.register(this)
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        Tooler.mainWindow = this

        this.tpX.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000)
        this.tpY.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000)
        this.tpZ.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000)

        //this.actionList.items.addAll(File("action").readLines())

        // TODO: Refactor
        this.command.setOnKeyPressed {
            if(it.code != KeyCode.ENTER)
                return@setOnKeyPressed

            val text = (it.source as TextField).text
            Command.doCommand(text)
            this.logBox.appendText("> $text\n")
            (it.source as TextField).text = ""
        }

        this.runAction.setOnAction {
            CurrentUser.playAction(this.actionList.selectionModel.selectedItems.first())
        }

        this.copyAction.setOnAction {
            Clipboard.getSystemClipboard().setContent(mapOf(Pair(DataFormat.PLAIN_TEXT, this.actionList.selectionModel.selectedItems.first())))
        }

        this.delAction.setOnAction {
            this.actionList.items.remove(this.actionList.selectionModel.selectedItems.first())
        }

        this.skipTutorial.setOnAction {
            SkipTutorial.skip()
        }

        this.autoGoodPigg.setOnAction {
            AutoGoodPigg.enabled = (it.source as CheckBox).isSelected
        }

        this.slotMacro.setOnAction {
            SlotMacro.enabled = (it.source as ToggleButton).isSelected
        }

        this.ngBypass.setOnAction {
            NGBypass.enabled = (it.source as CheckBox).isSelected
        }

        this.moveGhost.setOnAction {
            MoveGhost.enabled = (it.source as CheckBox).isSelected
        }

        this.chatGhost.setOnAction {
            ChatGhost.enabled = (it.source as CheckBox).isSelected
        }

        this.chatGhost.setOnAction {
            ChatGhost.enabled = (it.source as CheckBox).isSelected
        }

        this.actionGhost.setOnAction {
            ActionGhost.enabled = (it.source as CheckBox).isSelected
        }

        this.colorChat.setOnAction {
            ColorChat.enabled = (it.source as CheckBox).isSelected
        }

        this.colorChatColor.setOnAction {
            ColorChat.color = (it.source as ColorPicker).value
        }

        this.clickTPChk.setOnAction {
            ClickTP.enabled = (it.source as CheckBox).isSelected
        }

        this.skeetChk.setOnAction {
            Skeet.enabled = (it.source as CheckBox).isSelected
        }

        this.freeGacha.setOnAction {
            MuryouGatyaZenkaihou.enabled = true
        }

        // Instant teleport
        this.tpX.valueFactory.valueProperty().addListener { _, _, new ->
            CurrentUser.teleport(new.toShort(), this.tpY.value.toShort(), this.tpZ.value.toShort(), 0)
        }
        this.tpY.valueFactory.valueProperty().addListener { _, _, new ->
            CurrentUser.teleport(this.tpX.value.toShort(), new.toShort(), this.tpZ.value.toShort(), 0)
        }
        this.tpZ.valueFactory.valueProperty().addListener { _, _, new ->
            CurrentUser.teleport(this.tpX.value.toShort(), this.tpY.value.toShort(), new.toShort(), 0)
        }

        // Enable default modules
        Command.enabled = true
        AutoGoodPigg.enabled = true
        NGBypass.enabled = true
    }

    fun log(msg: String) {
        Platform.runLater({
            this.logBox.appendText(msg + "\n")
        })
    }

    @EventTarget
    fun onConnect(event: ConnectEvent){
        Platform.runLater({
            this.statusBar.text = "  Connecting..."
        })
    }

    @EventTarget
    fun onDisconnect(event: DisconnectEvent){
        Platform.runLater({
            this.statusBar.text = "  Disconnected."
        })
    }

    @EventTarget
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet

        Platform.runLater({
            if (packet is LoginChatResultPacket) {
                this.statusBar.text = "  Connected."
            }

            if (this.targetSet.isSelected && packet is GetUserProfileResultPacket) {
                this.targetSet.isSelected = false

                Tooler.targetUser = packet.usercode
                this.targetNick.text = "ニックネーム  ：　${packet.nickName}"
                this.targetUsercode.text = "ユーザーコード：　${packet.usercode}"
                if (packet.amebaId.isEmpty()) {
                    this.targetId.text = "アメーバID    ：　<未設定>"
                    this.targetPic.image = Image("default-pigg.png")
                } else {
                    this.targetId.text = "アメーバID    ：　${packet.amebaId}"
                    this.targetPic.image = Image("http://origin.contents.pigg.ameba.jp/api/84/user/${packet.amebaId}/image?type=png")
                }
            }

            if (packet is BaseAreaData) {
                this.areaName.text = "名前         ：　${packet.areaName}"
                this.areaSize.text = "サイズ        ：　${packet.sizeX} x ${packet.sizeY}"
                this.areaCode.text = "エリアコード ：　${packet.categoryCode}/${packet.areaCode}"
            }

            // Collect action
            if(packet is ActionResultPacket && !packet.actionCode.contains("\u0000")) {
              /*  val file = File("action")
                var actions = file.readText()
                if (!actions.contains(packet.actionCode)) {
                    actions += packet.actionCode + "\n"
                }
                file.writeText(actions)*/

                if(!this.actionList.items.contains(packet.actionCode)) {
                    this.actionList.items.add(packet.actionCode)
                }
            }
        })
    }

    @EventTarget
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        Platform.runLater({
            if (packet is MoveEndPacket) {
                this.areaPos.text = "座標         ：　X${packet.x} Y${packet.y} Z${packet.z}"
            }

            if(packet is ClickPiggShopItemPacket && packet.itemType == "action") {
                if(!this.actionList.items.contains(packet.itemCode)) {
                    this.actionList.items.add(packet.itemCode)
                }
            }
        })
    }
}
