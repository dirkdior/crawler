package onepipe

import java.security.MessageDigest
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.spec.{ IvParameterSpec, SecretKeySpec }
import org.apache.commons.codec.binary.Base64

import scala.util.Try

object OnePipe extends App {

  println(
    encrypt(
      strToBeEncrypted = "5531886652142950;564;09/32;3310",
      SecretKey        = "1HRxpffRuyAIEt98"
    )
  )

  println(
    encrypt(
      strToBeEncrypted = "12345678",
      SecretKey        = "1HRxpffRuyAIEt98"
    )
  )

  import com.github.t3hnar.bcrypt._
  import org.apache.commons.codec.binary.Base64
  def hashPassword(
    plainString: String
  ): Try[String] = plainString.bcryptSafe(12)
  println(
    hashPassword(s"Card User;4040404040404040;123;12/25;1234").get
  )

  def stuff[T](a: String => T): Unit = {
    println(a("bbb"))
  }

//  stuff[String]("my")

  def encrypt(strToBeEncrypted: String, SecretKey: String): Option[String] = {
    try {
      val md               = MessageDigest.getInstance("md5")
      val digestOfPassword = md.digest(SecretKey.getBytes("UTF-16LE"))
      val keyBytes         = Arrays.copyOf(digestOfPassword, 24)
      var j                = 0
      var k                = 16
      while ({
        j < 8
      }) keyBytes({
        k += 1;
        k - 1
      }) = keyBytes({
        j += 1;
        j - 1
      })
      val secretKey      = new SecretKeySpec(keyBytes, "DESede")
      val iv             = new IvParameterSpec(new Array[Byte](8))
      val cipher         = Cipher.getInstance("DESede/CBC/PKCS5Padding")
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv)
      val plainTextBytes = strToBeEncrypted.getBytes("UTF-16LE")
      val cipherText     = cipher.doFinal(plainTextBytes)
      val base64Bytes    = Base64.encodeBase64(cipherText)
      val encrypted      = new String(base64Bytes)
      Some(encrypted)
    }
    catch {
      case e: Exception =>
        println("one pipe has failed with [{}]", e)
        e.printStackTrace()
        None
    }

  }
}
