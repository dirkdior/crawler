package datestuff

import java.time._

object DateUtils extends App {

  val NOW: Instant = ZonedDateTime.now(ZoneId.of("Africa/Lagos")).toInstant

  val midnight: LocalTime               = LocalTime.MIDNIGHT
  val today: LocalDate                  = LocalDate.now(ZoneId.of("Africa/Lagos"))
  val todayMidnight: LocalDateTime      = LocalDateTime.of(today, midnight)
  val todayMidnightZoned: ZonedDateTime = ZonedDateTime.of(todayMidnight, ZoneId.of("Africa/Lagos"))
//  val tomorrowMorning: LocalDateTime = todayMidnight.plusDays(1).plusHours(8)
  val tomorrowMorning: Instant          = todayMidnightZoned.plusDays(1).plusHours(8).toInstant
  val tomorrowEvening: LocalDateTime    = todayMidnight.plusDays(1).plusHours(20)

  println(
    s"MIDNIGHT $midnight \nTODAY $today \nTODAY MIDNIGHT $todayMidnight \nTOMORROW MORNING $tomorrowMorning \nTOMORROW EVENING $tomorrowEvening"
  )

  println(NOW)

  println(todayMidnightZoned)

}
