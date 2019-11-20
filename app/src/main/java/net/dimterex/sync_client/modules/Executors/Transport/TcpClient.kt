package net.dimterex.sync_client.modules.Executors.Transport

import java.net.InetSocketAddress
import java.net.Socket
import java.io.InputStream
import java.nio.ByteBuffer
import java.io.OutputStream
import java.nio.ByteOrder
import java.nio.charset.Charset
import kotlin.concurrent.thread
import kotlin.reflect.KFunction1

class TcpClient (
    val inetSocketAddress: InetSocketAddress,
    val messageReceavedFunc: KFunction1<String, Unit>?) {

    val BUFFER_SIZE: Int = 1024

    private val socket: Socket = Socket()
    private var _readedStream: InputStream? = null
    private var _writedStream: OutputStream? = null
    init {

        socket.connect(inetSocketAddress)
        _readedStream = socket.getInputStream()
        _writedStream = socket.getOutputStream()
        thread(start = true)
        {
            StartReadData()
        }
    }

    private fun StartReadData() {
        while (socket.isConnected)
        {
            val fileSizeBytes = ByteArray(4)
            var bytes = _readedStream!!.read(fileSizeBytes, 0, fileSizeBytes.size)

            val dataLength = toInt32_2(fileSizeBytes, 0) //ByteBuffer.wrap(fileSizeBytes).int

            println("Senden length = ${dataLength}")
            var bytesLeft = dataLength
            val data = ByteArray(dataLength)

            val buffersize = 1024
            var bytesRead = 0

            while (bytesLeft > 0) {
                var curDataSize = buffersize.coerceAtMost(bytesLeft)
            if (_readedStream!!.available() < curDataSize)
                curDataSize = _readedStream!!.available()//This save me

                bytes = _readedStream!!.read(data, bytesRead, curDataSize)
                bytesRead += curDataSize
                bytesLeft -= curDataSize
            }
            var rawString = data.toString(Charsets.UTF_8)

            messageReceavedFunc?.invoke(rawString)
        }
        StartReadData()
    }

    private fun SendData(rawString: String) {
        val data = rawString.toByteArray(Charsets.UTF_8)

        val dataLength = GetBytes(data.size)

        _writedStream!!.write(dataLength, 0, 4)

        var bytesSent = 0
        var bytesLeft = data.size

        while (bytesLeft > 0) {
            val curDataSize = BUFFER_SIZE.coerceAtMost(bytesLeft)
            _writedStream!!.write(data, bytesSent, curDataSize)
            bytesSent += curDataSize
            bytesLeft -= curDataSize
        }
    }

    fun GetBytes(value: Int): ByteArray {
        val buffer = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder())
        buffer.putInt(value)
        return buffer.array()
    }

    fun toInt32_2(bytes: ByteArray, index: Int): Int {
        return 0xff and bytes[index].toInt() shl 32 or (0xff and bytes[index + 1].toInt() shl 40) or (0xff and bytes[index + 2].toInt() shl 48) or (0xff and bytes[index + 3].toInt() shl 56)
    }

    fun close() {
        _readedStream?.close()
        _writedStream?.close()
        socket.close()
    }

    fun send(message: String) {
        SendData(message)
    }

}