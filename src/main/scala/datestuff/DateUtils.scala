package datestuff

import java.time._
import java.time.temporal.ChronoUnit

object DateUtils extends App {

  val NOW: Instant = ZonedDateTime.now(ZoneId.of("Africa/Lagos")).toInstant

  val midnight: LocalTime               = LocalTime.MIDNIGHT
  val today: LocalDate                  = LocalDate.now(ZoneId.of("Africa/Lagos"))
  val todayMidnight: LocalDateTime      = LocalDateTime.of(today, midnight)
  val todayMidnightZoned: ZonedDateTime = ZonedDateTime.of(todayMidnight, ZoneId.of("Africa/Lagos"))
//  val tomorrowMorning: LocalDateTime = todayMidnight.plusDays(1).plusHours(8)
  val tomorrowMorning: Instant          = todayMidnightZoned.plusDays(1).plusHours(8).toInstant
  val tomorrowEvening: LocalDateTime    = todayMidnight.plusDays(1).plusHours(20)
  val tomorrow_8AM: Instant             = todayMidnightZoned.plusDays(1).plusHours(8).toInstant
  val tomorrow_730AM: Instant           = todayMidnightZoned.plusDays(1).plusHours(7).plusMinutes(30).toInstant
  val tomorrow_830AM: Instant           = todayMidnightZoned.plusDays(1).plusHours(8).plusMinutes(30).toInstant

  def getSignedHoursBetween(closer_time: Instant, distant_time: Instant): Long   =
    ChronoUnit.HOURS.between(closer_time, distant_time)
  def getSignedMinutesBetween(closer_time: Instant, distant_time: Instant): Long =
    ChronoUnit.MINUTES.between(closer_time, distant_time)

  println(
    s"MIDNIGHT $midnight \nTODAY $today \nTODAY MIDNIGHT $todayMidnight \nTOMORROW MORNING $tomorrowMorning \nTOMORROW EVENING $tomorrowEvening"
  )

  println(NOW)

  println(todayMidnightZoned)

  println(
    getSignedHoursBetween(tomorrow_8AM, tomorrow_830AM)
  )
  println(
    getSignedHoursBetween(tomorrow_8AM, tomorrow_730AM)
  )

}
