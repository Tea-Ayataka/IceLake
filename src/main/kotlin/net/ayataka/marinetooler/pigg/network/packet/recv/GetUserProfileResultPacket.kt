package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.club.ProfileClubData
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineAvatar
import net.ayataka.marinetooler.pigg.network.packet.data.user.AvatarData
import net.ayataka.marinetooler.pigg.network.packet.data.user.FavoriteState

class GetUserProfileResultPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_USER_PROFILE_RESULT.id

    var usercode = ""
    var amebaId = ""
    var asUserId = ""
    var nickName = ""
    var description = ""
    var goodCount = 0
    var friendCount = 0
    var friend = false
    var ignore = false
    var sameArea = false
    var hasGivenGoodToday = false
    var isOnline = false
    var hasFriendshipRequest = false
    var sentFriendshipRequest = false
    var friendShipMessage = ""
    var isAllowedRoom: Byte = 0
    var isAllowedFriend = false
    var isAllowedMail = false
    var allowOpenFriendList: Byte = 0
    var isBan = false
    var clubJoinable = false
    var missionCount = 0
    var isAdmin = false
    var isEvent = false
    var eventTitle = ""
    var isAndroid = false
    var zone: Byte = 0
    var isAllowGift = false
    var hasPiggLife = false
    var hasPiggIsland = false
    var hasPiggCafe = false
    var hasPiggWorld = false
    var isPiggLifeAvailable = false
    var isPiggIslandAvailable = false
    var isPiggCafeAvailable = false
    var isPiggWorldAvailable = false
    var isGroupMessageEnabled = false
    var oneMessage: String? = null
    var beginnerRemainingCount = 0
    var joinedContest = false
    var contestCode: String? = null
    var petType = ""
    var petColor = 0
    var isDiaryReadEnable = false
    var isNewDiaryPage = false
    var totalDiaryPage = 0
    var myFavoriteCount = 0
    var receiveFavoriteCount = 0
    var favoriteState = FavoriteState.STATE_FAVORITE_NONE
    var isAllowAddFavorite = false
    var allowOpenFavoriteList = false
    var isBlock = false
    var defineAvatar = DefineAvatar()
    var myBestRankingTitle = ""
    var myBest1 = ""
    var myBest2 = ""
    var myBest3 = ""
    var isSendedMyBest = false

    var clubs = mutableListOf<ProfileClubData>()

    override fun readFrom(buffer: ByteBuilder) {
        usercode = buffer.readString()
        amebaId = buffer.readString()
        asUserId = buffer.readString()
        nickName = buffer.readString()
        description = buffer.readString()
        goodCount = buffer.readInt()
        friendCount = buffer.readInt()
        friend = buffer.readBoolean()
        ignore = buffer.readBoolean()
        sameArea = buffer.readBoolean()
        hasGivenGoodToday = buffer.readBoolean()
        isOnline = buffer.readBoolean()
        hasFriendshipRequest = buffer.readBoolean()
        sentFriendshipRequest = buffer.readBoolean()
        friendShipMessage = buffer.readString()
        isAllowedRoom = buffer.readByte()
        isAllowedFriend = buffer.readBoolean()
        isAllowedMail = buffer.readBoolean()
        allowOpenFriendList = buffer.readByte()
        isBan = buffer.readBoolean()

        (0 until buffer.readInt()).forEach {
            val profileClubData = ProfileClubData().apply { readFrom(buffer) }

            clubs.add(profileClubData)
        }

        clubJoinable = buffer.readBoolean()
        missionCount = buffer.readInt()
        isAdmin = buffer.readBoolean()
        isEvent = buffer.readBoolean()
        eventTitle = buffer.readString()
        isAndroid = buffer.readBoolean()
        zone = buffer.readByte()
        isAllowGift = buffer.readBoolean()
        hasPiggLife = buffer.readBoolean()
        hasPiggIsland = buffer.readBoolean()
        hasPiggCafe = buffer.readBoolean()
        hasPiggWorld = buffer.readBoolean()
        isPiggLifeAvailable = buffer.readBoolean()
        isPiggIslandAvailable = buffer.readBoolean()
        isPiggCafeAvailable = buffer.readBoolean()
        isPiggWorldAvailable = buffer.readBoolean()
        isGroupMessageEnabled = buffer.readBoolean()

        oneMessage = buffer.readString()

        beginnerRemainingCount = buffer.readInt()
        joinedContest = buffer.readBoolean()

        if(joinedContest){
            contestCode = buffer.readString()
        }

        petType = buffer.readString()
        petColor = buffer.readInt()
        isDiaryReadEnable = buffer.readBoolean()
        isNewDiaryPage = buffer.readBoolean()
        totalDiaryPage = buffer.readInt()
        myFavoriteCount = buffer.readInt()
        receiveFavoriteCount = buffer.readInt()

        favoriteState = FavoriteState.getState(buffer.readByte())

        isAllowAddFavorite = buffer.readBoolean()
        allowOpenFavoriteList = buffer.readBoolean()
        isBlock = buffer.readBoolean()

        defineAvatar = DefineAvatar(AvatarData().apply { readFrom(buffer) })

        myBestRankingTitle = buffer.readString()
        myBest1 = buffer.readString()
        myBest2 = buffer.readString()
        myBest3 = buffer.readString()
        isSendedMyBest = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(usercode, amebaId, asUserId, nickName, description)
                .writeInt(goodCount, friendCount)
                .writeBoolean(friend, ignore, sameArea, hasGivenGoodToday, isOnline, hasFriendshipRequest, sentFriendshipRequest)
                .writeString(friendShipMessage)
                .writeByte(isAllowedRoom)
                .writeBoolean(isAllowedFriend, isAllowedMail)
                .writeByte(allowOpenFriendList)
                .writeBoolean(isBan)
                .writeInt(clubs.size)

        clubs.forEach {
            it.writeTo(buffer)
        }

        buffer.writeBoolean(clubJoinable)
                .writeInt(missionCount)
                .writeBoolean(isAdmin, isEvent)
                .writeString(eventTitle)
                .writeBoolean(isAndroid)
                .writeByte(zone)
                .writeBoolean(isAllowGift, hasPiggLife, hasPiggIsland, hasPiggCafe, hasPiggWorld, isPiggLifeAvailable, isPiggIslandAvailable, isPiggCafeAvailable, isPiggWorldAvailable, isGroupMessageEnabled)

        oneMessage?.let { buffer.writeString(it) }

        buffer.writeInt(beginnerRemainingCount)
                .writeBoolean(joinedContest)

        contestCode?.let { buffer.writeString(it) }
        buffer.writeString(petType)
                .writeInt(petColor)
                .writeBoolean(isDiaryReadEnable, isNewDiaryPage)
                .writeInt(totalDiaryPage, myFavoriteCount, receiveFavoriteCount)
                .writeByte(favoriteState.state)
                .writeBoolean(isAllowAddFavorite, allowOpenFavoriteList, isBlock)

        defineAvatar.data.writeTo(buffer)

        buffer.writeString(myBestRankingTitle)
                .writeString(myBest1)
                .writeString(myBest2)
                .writeString(myBest3)
                .writeBoolean(isSendedMyBest)

        return buffer
    }
}