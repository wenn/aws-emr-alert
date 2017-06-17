package com.wen.emr.notifier

import com.google.gson.Gson
import org.apache.http.client.methods.{CloseableHttpResponse, HttpPost}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients


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


  /** Sends message to the spark room.
    *
    * @param message message to be sent
    * @return [[CloseableHttpResponse]] of Cisco Spark api request
    */
  def sendMessage(message: String): CloseableHttpResponse =
    HttpClients.createDefault.execute(postMessage(jsonMessage(message)))

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

}
