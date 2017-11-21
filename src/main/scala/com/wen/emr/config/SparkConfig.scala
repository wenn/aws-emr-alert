package com.wen.emr.config

import com.amazonaws.services.s3.{AmazonS3ClientBuilder, AmazonS3URI}
import com.amazonaws.util.IOUtils
import com.typesafe.config.ConfigException.Missing
import com.typesafe.config.{Config, ConfigFactory}
import com.wen.emr.notifier.{Room, Token}

/** Loads config for Cisco Spark form s3 */
trait AppConfig {
  val S3ConfigPath = "S3_CONFIG_PATH"

  lazy val config = configFromS3
  lazy val token = Token(config.getString("spark.token"))
  lazy val room = Room(config.getString("spark.roomId"))
  lazy val clusterNameRegex = config.getString("spark.cluster.name.regex")

  lazy val allowClusterStatuses = ClusterStatuses.parseFromKey("spark.cluster.status")
  lazy val allowStepStatuses = StepStatuses.parseFromKey("spark.cluster.step.status")

  /** Load config from S3
    *
    * @return String of config content
    */
  def configFromS3: Config =
    s3ConfigFilePath match {
      case None => throw new RuntimeException(s"$S3ConfigPath environment variable not defined.")
      case Some(configPath) => {
        val s3ConfigFile = new AmazonS3URI(configPath)
        val s3 = AmazonS3ClientBuilder.defaultClient

        val inputStream = s3
          .getObject(s3ConfigFile.getBucket, s3ConfigFile.getKey)
          .getObjectContent

        ConfigFactory.parseString(IOUtils.toString(inputStream))
      }
    }

  /** S3 config path
    *
    * @return string value of s3 config path
    */
  def s3ConfigFilePath: Option[String] = Option(System.getenv(S3ConfigPath))

  private trait Statuses {

    /** Parse statuses from key.
      *
      * @param configKey config key to parse from.
      * @return [[Seq]] of statuses.
      */
    def parseFromKey(configKey: String): Seq[String] = onMissing(defaults) {
      parseFromValue(config.getString(configKey))
    }

    /** Parse statuses from value.
      *
      * @param statuses config values of statuses to parse from.
      * @return [[Seq]] of statuses.
      */
    def parseFromValue(statuses: String): Seq[String] = statuses
      .split(',')
      .map {s =>
        val status = s.trim

        if (!valid(status))
          throw new IllegalArgumentException(s"Not a valid status=$status")

        status
      }

    private[config] val statuses: Seq[String]
    private[config] val defaults: Seq[String]

    private def valid(status: String): Boolean = statuses.contains(status)

    private def onMissing[T](default: T)
                            (fn: => T) = {
      try {
        fn
      } catch {
        case e: Missing => default
      }
    }
  }

  private object ClusterStatuses extends Statuses {
    val STARTING = "STARTING"
    val WAITING = "WAITING"
    val RUNNING = "RUNNING"
    val TERMINATED = "TERMINATED"
    val TERMINATED_WITH_ERRORS = "TERMINATED_WITH_ERRORS"

    override private[config] val defaults = Seq(STARTING, TERMINATED,
      TERMINATED_WITH_ERRORS)

    override private[config] val statuses = Seq(STARTING, WAITING, RUNNING, TERMINATED,
      TERMINATED_WITH_ERRORS)
  }

  private object StepStatuses extends Statuses {
    val CANCEL_PENDING = "CANCEL_PENDING"
    val CANCELLED = "CANCELLED"
    val COMPLETED = "COMPLETED"
    val FAILED = "FAILED"
    val PENDING = "PENDING"
    val RUNNING = "RUNNING"

    override private[config] val defaults = Seq(FAILED)

    override private[config] val statuses = Seq(CANCEL_PENDING, CANCELLED, COMPLETED,
      FAILED, PENDING, RUNNING)
  }

}

object SparkConfig extends AppConfig

