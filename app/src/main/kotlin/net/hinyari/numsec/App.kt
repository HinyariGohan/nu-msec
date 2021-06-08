package net.hinyari.numsec

import org.apache.commons.net.ntp.NTPUDPClient
import java.net.InetAddress
import java.text.SimpleDateFormat
import java.util.*

class App {
    val greeting: String
        get() {
            return "Hello World!"
        }

    val NTP_SERVER = "ntp.nict.jp"
}

fun main() {
    val app = App();
    val formatter = SimpleDateFormat("HH:mm:ss.SSS");
    val client = NTPUDPClient();

    try {
        client.open();
        val host = InetAddress.getByName(app.NTP_SERVER);
        val info = client.getTime(host)

        info.computeDetails()
        val exactTime = Date(System.currentTimeMillis() + info.offset)
        println("正確な時刻 " + formatter.format(exactTime));

        val packet = info.message

        println("[t1] クライアントがパケットを送信した時刻: ${formatter.format(packet.originateTimeStamp.date)}")
        println("[t2] NTPサーバがパケットを受信した時刻: ${formatter.format(packet.receiveTimeStamp.date)}")
        println("[t3] NTPサーバがパケットを送信した時刻: ${formatter.format(packet.transmitTimeStamp.date)}")
        println("[t4] クライアントがパケットを受信した時刻: ${formatter.format(Date(info.returnTime))}")

        println()
        println("往来にかかった時間: ${info.delay} ms")
        println("クライアントの時間差: ${info.offset} ms")
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        client.close()
    }
}
