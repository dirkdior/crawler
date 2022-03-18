package encoding

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveEnumerationCodec

object CircleJSONSample extends App {

  case class FAQs(
    segments: List[Segment]
  )
  case class Segment(
    header: String,
    topics: List[Topic]
  )
  case class Topic(
    header: String,
    content: String
  )

  val faqs = FAQs(
    segments = List(
      Segment(
        header = "Transactions",
        List(
          Topic(header = "How do I handle a failed transaction", content = "Some paragraph here..."),
          Topic(header = "How do I view my transaction history", content = "Some paragraph here...")
        )
      ),
      Segment(
        header = "Insurance",
        List(
          Topic(header = "Raising a claim", content               = "Some paragraph here..."),
          Topic(header = "What does the insurance cover", content = "Some paragraph here...")
        )
      )
    )
  )

  val json = faqs.asJson.noSpaces

  println(json)

}

object MoreCirceExamples extends App {

  println(
    0 >= 0.0
  )

  import Resources._
  import JsonSupport._
//  println(
//    serialize(
//      CurrentState(
//        data          = Empty,
//        event         = IncomingText("Something"),
//        eventExecuted = false
//      )
//    )
//  )
//  println(
//    CurrentState(
//      data          = SurnameInput("Borg"),
//      event         = IncomingPayment,
//      eventExecuted = false
//    )
//  )
  println(
    serialize(
      CurrentState(
        state         = MedicationReminder,
        data          = RegistrationComplete(
          surname     = "DD",
          firstname   = "DD",
          email       = "DD",
          dob         = "DD",
          phoneNumber = "DD"
        ),
        event         = InvoiceUploadedByAdmin,
        eventExecuted = false
      )
    )
  )
}

object Resources {
  sealed trait HandlerEvent
  case class IncomingText(text: String) extends HandlerEvent
  case class IncomingImage(url: String) extends HandlerEvent
  object IncomingPayment extends HandlerEvent

  sealed trait AdminInitiatedEvent extends HandlerEvent
  object InvoiceUploadedByAdmin extends AdminInitiatedEvent

  sealed trait MedicationReminderEvent extends HandlerEvent
  object BeginReminders extends MedicationReminderEvent
  object ScheduleNextReminders extends MedicationReminderEvent
  object MorningMedicationReminder extends MedicationReminderEvent
  case class AfternoonMedicationReminder(scheduleNextReminders: Boolean) extends MedicationReminderEvent
  object NightMedicationReminder extends MedicationReminderEvent
  case class MedicationTakenConfirmation(period: UsagePeriod.Value) extends MedicationReminderEvent
//  case class MedicationTakenConfirmation(period: String) extends MedicationReminderEvent

  sealed trait PromptEvent extends HandlerEvent
  object RetryReminderScheduling extends PromptEvent

  sealed trait HandlerData
  sealed trait RegistrationData extends HandlerData
  sealed trait PrescriptionUploadData extends HandlerData
  sealed trait SubscriptionData extends HandlerData
  sealed trait ReminderData extends HandlerData {
    val regDetails: RegistrationComplete
  }

  object Empty extends RegistrationData
  object RegistrationPrompt extends RegistrationData
  object ContinueRegistration extends RegistrationData
  case class SurnameInput(surname: String) extends RegistrationData
  case class FirstnameInput(surname: String, firstname: String) extends RegistrationData
  case class EmailInput(surname: String, firstname: String, email: String) extends HandlerData with RegistrationData

  case class RegistrationComplete(
    surname: String,
    firstname: String,
    email: String,
    dob: String,
    phoneNumber: String
  ) extends PrescriptionUploadData
  case class UserPrescription(prescription: Prescription, registrationData: RegistrationComplete) extends PrescriptionUploadData

  case class StartSubscription(regDetails: RegistrationComplete) extends SubscriptionData

  case class ActiveReminder(regDetails: RegistrationComplete) extends ReminderData

//  case class OngoingReminder(
//    medList: List[PrescriptionEntry],
//    medReadableStr: String,
//    regDetails: RegistrationComplete,
//    usagePeriod: String
//  ) extends ReminderData
  case class OngoingReminder(
    medList: List[PrescriptionEntry],
    medReadableStr: String,
    regDetails: RegistrationComplete,
    usagePeriod: UsagePeriod.Value
  ) extends ReminderData

  sealed trait HandlerState
  case object Routing extends HandlerState
  object Registration extends HandlerState
  object PrescriptionUpload extends HandlerState
  object PreSubscription extends HandlerState
  object Subscription extends HandlerState
  object MedicationReminder extends HandlerState

  case class Prescription(
    text: Option[String],
    imageList: List[String]
  ) {
    require(
      requirement = text.exists(_.trim.nonEmpty) || imageList.exists(_.trim.nonEmpty),
      message     = "The Prescription must contain either a text or an image url!"
    )
  }

  case class CurrentState(
    state: HandlerState,
    data: HandlerData,
    event: HandlerEvent,
    eventExecuted: Boolean
  )

  object UsagePeriod extends Enumeration {
    type UsagePeriod = Value

    val MORNING   = Value("MORNING")
    val AFTERNOON = Value("AFTERNOON")
    val NIGHT     = Value("NIGHT")
  }

  case class PrescriptionEntry(
    id: Int,
    userPhoneId: PhoneNumberType,
    medicineName: String,
    additionalInfo: Option[String],
    dosage: Int,
    dosageUnit: DosageUnit.Value,
    dailyUsageFrequency: Int
  ) {
    def readableFormat = s"$medicineName, Dosage: $dosage $dosageUnit"
  }
//  case class PrescriptionEntry(
//    id: Int,
//    userPhoneId: PhoneNumberType,
//    medicineName: String,
//    additionalInfo: Option[String],
//    dosage: Int,
//    dosageUnit: String,
//    dailyUsageFrequency: Int
//  ) {
//    def readableFormat = s"$medicineName, Dosage: $dosage $dosageUnit"
//  }

  object DosageUnit extends Enumeration {
    type DosageUnit = Value

    val ml      = Value("ml")
    val tablets = Value("tablets")

    def get(str: String): Option[DosageUnit.Value] = str.toLowerCase match {
      case "ml"      => Some(DosageUnit.ml)
      case "tablets" => Some(DosageUnit.tablets)
      case _         => None
    }
  }

  sealed trait PhoneNumberType {
    val number: String
  }

  case class RawPhoneNumber(number: String) extends PhoneNumberType

  case class PhoneNumber(number: String) extends PhoneNumberType {
    override def toString: String = number
  }
}

object JsonSupport extends JsonSupportT {
  import Resources._
  def serialize(d: CurrentState): String   = d.asJson.noSpaces
  def deserialize(s: String): CurrentState = decode[CurrentState](s) match {
    case Right(deserialized) =>
      deserialized
    case Left(error)         =>
      println("Unable to deserialize JsonString [{}] to CurrentState, error: {}", s, error)
      throw new IllegalArgumentException
  }
}

trait JsonSupportT {
  import Resources._

  implicit val usagePeriodDecoder: Decoder[UsagePeriod.Value] = Decoder.decodeEnumeration(UsagePeriod)
  implicit val usagePeriodEncoder: Encoder[UsagePeriod.Value] = Encoder.encodeEnumeration(UsagePeriod)

  implicit val dosageUnitDecoder: Decoder[DosageUnit.Value] = Decoder.decodeEnumeration(DosageUnit)
  implicit val dosageUnitEncoder: Encoder[DosageUnit.Value] = Encoder.encodeEnumeration(DosageUnit)

}
