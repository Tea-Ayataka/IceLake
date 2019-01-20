package net.ayataka.marinetooler.pigg.swf

import com.flagstone.transform.DoABC
import com.flagstone.transform.Movie
import com.flagstone.transform.MovieHeader
import net.ayataka.marinetooler.utils.fromHexToBytes
import net.ayataka.marinetooler.utils.toHexString
import java.io.ByteArrayOutputStream

object SWFInjector {
    fun inject(data: ByteArray): ByteArray {
        val movie = Movie()
        movie.decodeFromStream(data.inputStream())

        movie.objects.forEach {
            if (it is DoABC) {
                it.data = it.data.toHexString().replace("02 32 37 03 31 33 33 03 32 31 33 02 36 34", "03 31 32 37 03 30 30 30 02 30 30 02 30 31").fromHexToBytes()
            }
        }

        val output = ByteArrayOutputStream()
        movie.encodeToStream(output)

        return output.toByteArray()
    }
}