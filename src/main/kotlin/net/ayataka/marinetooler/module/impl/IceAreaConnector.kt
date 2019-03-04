package net.ayataka.marinetooler.module.impl

import net.ayataka.marinetooler.module.Module

object IceAreaConnector : Module() {
    /*
    var areaData = BaseAreaData()

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        if ((packet is NotifyUserRoomEnteredPacket || packet is GetNoticeBoardMessageOfAreaPacket) && isConnectingToIceArea()) {
            event.canceled = true
        }

        if (packet is LoginChatPacket && isConnectingToIceArea()) {
            packet.data = ByteBuilder()
                    .apply {
                        areaData.defineAvatars
                                .find { it.data.userCode == CurrentUser.usercode }
                                ?.data
                                ?.writeTo(this)
                    }
                    .build()
                    .array()
        }
    }

    @EventListener
    fun onRecvPacket(event: ReceivePacketEvent) {
        val packet = event.packet

        if (packet is BaseAreaData) {
            println(packet.placeFurnitures)
            println(packet.defineFurnitures)
        }

        if(packet is BaseAreaData && packet.areaData.areaCode == "114514"){
            println("buriburi")

            Pigg.send(ListUserFurniture())
        }

        if(packet is ListUserFurnitureResultPacket) {
            println(packet.placedFurnitures)
            println(packet.roomData)
            println(packet.max)

            println(packet.furnitures[2])
            println(FurnitureGetter.furnitures.last())

            packet.apply {
                furnitures.add(FurnitureGetter.furnitures.last().apply { time = furnitures[2].time } )
E
                furnitures.remove(furnitures[2])

                furnitures.forEach {
                    it.quantity = 999
                }
            }
        }
    }
    */
}