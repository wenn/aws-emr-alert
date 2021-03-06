package com.wen.emr.notifier

import org.apache.http.client.methods.{CloseableHttpResponse, HttpPost}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients

import com.google.gson.Gson
import com.wen.emr.event.RichTriggerEvent


class RequestException(m: String) extends Exception(m)

case class Token(id: String) {
  override def toString: String = id
}

case class Room(id: String)

case class Message(roomId: String, markdown: String)

/** Cisco Spark client notifier.
  *
  * @param token Spark authorization token
  * @param room  Room id to send a message to.
  */
case class SparkClient(token: Token, room: Room) {

  val EmptyString = ""

  /** Sends message to the spark room.
    *
    * @throws RequestException when not 20* request
    * @param event message to be sent
    * @return [[CloseableHttpResponse]] of Cisco Spark api request
    */
  def sendMessage(event: RichTriggerEvent): Unit =
    Some(HttpClients
      .createDefault
      .execute(postMessage(jsonMessage(build(event))))
      .getStatusLine)
      .map {status =>
        status.getStatusCode match {
          case code if code.toString.startsWith("20") => Unit
          case other => throw new RequestException(s"$other ${status.getReasonPhrase}")
        }
      }

  /** Cisco spark message api endpoint.
    *
    * @param json Json string to be sent.
    * @return [[HttpPost]]
    */
  def postMessage(json: String): HttpPost = {
    val post = new HttpPost("https://api.ciscospark.com/v1/messages")

    post.setHeader("Authorization", s"Bearer $token")
    post.setHeader("Content-Type", "application/json")
    post.setEntity(new StringEntity(json, "utf-8"))

    post
  }

  /** Json message.
    *
    * @param message message to be sent
    * @return Json string
    */
  def jsonMessage(message: String): String =
    (new Gson).toJson(Message(room.id, message))


  /** Build markdown text from [[RichTriggerEvent]]
    *
    * @param event Emr [[RichTriggerEvent]]
    * @return
    */
  def build(event: RichTriggerEvent) = {
    val message = if (!event.detail.message.isEmpty) s"\n- ${event.detail.message}"
    else EmptyString

    def webLink(event: RichTriggerEvent) = {
      val uri = "https://console.aws.amazon.com/elasticmapreduce/home" +
        s"?region=${event.region}#cluster-details:${event.detail.clusterId}"

      s"[${event.detail.clusterId}]($uri)"
    }

    s"**${event.detail.name}**: ${webLink(event)} " +
      s"- ${event.detail.state} *at ${event.time}*$message"
  }

}
