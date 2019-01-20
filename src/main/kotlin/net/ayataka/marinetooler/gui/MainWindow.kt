package net.ayataka.marinetooler.gui

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.stage.Stage
import net.ayataka.eventapi.EventListener
import net.ayataka.eventapi.EventManager
import net.ayataka.marinetooler.ICE_LAKE
import net.ayataka.marinetooler.module.impl.*
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.ConnectEvent
import net.ayataka.marinetooler.pigg.event.DisconnectEvent
import net.ayataka.marinetooler.pigg.event.ReceivePacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.PacketDirection
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.recv.GetUserProfileResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.LoginChatResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.MoveEndPacket
import net.ayataka.marinetooler.pigg.network.packet.send.TravelBundlePacket
import net.ayataka.marinetooler.utils.toHexString
import org.controlsfx.control.StatusBar
import org.controlsfx.control.ToggleSwitch
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
    lateinit var command: TextField
    @FXML
    lateinit var instantDonate: CheckBox
    @FXML
    lateinit var gumiPoint: CheckBox
    @FXML
    lateinit var fishMacroButton: Button
    @FXML
    lateinit var autoUoUo: CheckBox
    @FXML
    lateinit var gumiAmount: Spinner<Int>

    // Packet table
    @FXML
    lateinit var recordButton: ToggleSwitch
    @FXML
    lateinit var recordSendCheckBox: CheckBox
    @FXML
    lateinit var recordRecvCheckBox: CheckBox
    @FXML
    lateinit var packetClearButton: Button
    @FXML
    lateinit var packetTable: TableView<PacketView>
    @FXML
    lateinit var serverCol: TableColumn<PacketView, String>
    @FXML
    lateinit var idCol: TableColumn<PacketView, String>
    @FXML
    lateinit var dataCol: TableColumn<PacketView, String>
    @FXML
    lateinit var preventAreaEntering: CheckBox

    // Old Area
    @FXML
    lateinit var areaList: ListView<String>
    @FXML
    lateinit var areaJumpButton: Button
    @FXML
    lateinit var areaSearchTextField: TextField

    init {
        EventManager.register(this)
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        ICE_LAKE.mainWindow = this

        tpX.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000)
        tpY.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000)
        tpZ.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000)

        gumiAmount.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100_000)
        gumiAmount.valueFactory.value = 100_000

        // TODO: Refactor
        command.setOnKeyPressed {
            if (it.code != KeyCode.ENTER)
                return@setOnKeyPressed

            val text = (it.source as TextField).text
            Command.doCommand(text)
            logBox.appendText("> $text\n")
            (it.source as TextField).text = ""
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
            gumiAmount.isDisable = !(it.source as CheckBox).isSelected
        }

        gumiAmount.valueFactory.valueProperty().addListener { _, _, new ->
            PuzzleZousyoku.amount = new
        }

        gumiAmount.focusedProperty().addListener { _, _, newValue ->
            if (!newValue) {
                gumiAmount.increment(0)
            }
        }

        fishMacroButton.setOnAction {
            if (FishMacro.task != null) {
                FishMacro.stop()
                (it.source as Button).text = "スタート"
                return@setOnAction
            }

            if (!FishMacro.ready) {
                ICE_LAKE.showMessage("釣り台に立ってから実行してください。")
                return@setOnAction
            }

            FishMacro.start()
            (it.source as Button).text = "ストップ"
        }

        packetClearButton.setOnAction {
            packetTable.items.clear()
        }

        preventAreaEntering.setOnAction {
            AreaEnterPrevent.enabled = (it.source as CheckBox).isSelected
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

        // packet table
        serverCol.cellValueFactory = PropertyValueFactory<PacketView, String>("server")
        idCol.cellValueFactory = PropertyValueFactory<PacketView, String>("id")
        dataCol.cellValueFactory = PropertyValueFactory<PacketView, String>("data")

        packetTable.setOnMousePressed {
            if (it.isPrimaryButtonDown && it.clickCount == 2) {
                openPacketDialog(packetTable.selectionModel.selectedItem)
            }
        }

        packetTable.setOnKeyPressed {
            if (it.code == KeyCode.ENTER) {
                openPacketDialog(packetTable.selectionModel.selectedItem)
            }
        }

        // old area
        areaJumpButton.setOnAction {
            val name = areaList.selectionModel.selectedItem
            val code = ICE_LAKE.oldAreas.keys.find { ICE_LAKE.oldAreas[it] == name } ?: return@setOnAction

            Pigg.send(TravelBundlePacket().apply {
                categoryCode = code.split(" ")[0]
                areaCode = code.split(" ")[1]
            })
        }

        areaSearchTextField.textProperty().addListener { _, _, newValue ->
            areaList.items.clear()
            areaList.items.addAll(ICE_LAKE.oldAreas.values.filter { newValue in it })
            areaList.items.sort()
        }

        // Enable default modules
        Command.enabled = true
        AutoGoodPigg.enabled = true
        NGBypass.enabled = true
        FishMacro.enabled = true
        FakeEquipment.enabled = true
        ActionModifier.enabled = true
        IceAreaConnector.enabled = true
        FurnitureGetter.enabled = true
        Analytics.enabled = true
    }

    fun openPacketDialog(data: PacketView) {
        val loader = FXMLLoader(javaClass.classLoader.getResource("PacketDialog.fxml"))
        val parent = loader.load<Parent>()

        loader.getController<PacketDialog>().load(data)

        Stage().run {
            title = "Packet Editor"
            scene = Scene(parent)
            isAlwaysOnTop = true
            show()
        }
    }

    fun log(msg: String) = Platform.runLater {
        logBox.appendText(msg + "\n")
        val lines = logBox.text.lines()
        if (lines.size > 1000) {
            logBox.deleteText(0, lines.first().length + 1)
        }
    }

    @EventListener
    fun onConnect(event: ConnectEvent) = Platform.runLater {
        statusBar.text = "  Connecting..."
    }

    @EventListener
    fun onDisconnect(event: DisconnectEvent) = Platform.runLater {
        statusBar.text = "  Disconnected."
    }

    @EventListener
    fun onRecvPacket(event: ReceivePacketEvent) = Platform.runLater {
        val packet = event.packet

        if (packet is LoginChatResultPacket) {
            statusBar.text = "  Connected."
        }

        if (targetSet.isSelected && packet is GetUserProfileResultPacket) {
            targetSet.isSelected = false

            ICE_LAKE.targetUser = packet.usercode
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
    }

    @EventListener
    fun onSendPacket(event: SendPacketEvent) = Platform.runLater {
        val packet = event.packet

        if (packet is MoveEndPacket) {
            areaPos.text = "座標         ：　X${packet.x} Y${packet.y} Z${packet.z}"
        }
    }

    fun recordPacket(direction: PacketDirection, server: String, id: String, data: ByteArray) = Platform.runLater {
        if (recordButton.isSelected) {
            if (direction == PacketDirection.SEND && recordSendCheckBox.isSelected) {
                packetTable.items.add(PacketView(server, id, data.toHexString(), direction.name))
            }
            if (direction == PacketDirection.RECEIVE && recordRecvCheckBox.isSelected) {
                packetTable.items.add(PacketView(server, id, data.toHexString(), direction.name))
            }
        }
    }
}

class PacketView(val server: String, val id: String, val data: String, val direction: String)