package _shared

import java.security.MessageDigest
import java.time.{ Instant, LocalDate, LocalDateTime, LocalTime, ZoneId, ZonedDateTime }
import java.util.UUID
import scala.util.Random

object GeneratorUtils {

  def generateRandomId: String = "CRAWid_" + HashingUtils.md5Hash(UUID.randomUUID.toString + ":" + DateUtils.NOW)

  def generateSixDigitNum: Int = {
    Random.nextInt(999999)
    //    String.format("%06d", number)
  }
}

object HashingUtils {
  def sha256Hash(value: String): String =
    MessageDigest.getInstance("SHA-256").digest(value.getBytes).map("%02x".format(_)).mkString

  def md5Hash(value: String): String = MessageDigest.getInstance("MD5").digest(value.getBytes).map("%02x".format(_)).mkString
}

object DateUtils {

  private val zoneId: ZoneId              = ZoneId.of("Africa/Lagos")
  private def nextMidnight: ZonedDateTime =
    ZonedDateTime.of(LocalDateTime.of(LocalDate.now(zoneId), LocalTime.MIDNIGHT), zoneId).plusDays(1)
  private def thisMidnight: ZonedDateTime =
    ZonedDateTime.of(LocalDateTime.of(LocalDate.now(zoneId), LocalTime.MIDNIGHT), zoneId)

  //IMPORTANT: Write test cases for these specific times
  def NOW: Instant = ZonedDateTime.now(zoneId).toInstant
}
