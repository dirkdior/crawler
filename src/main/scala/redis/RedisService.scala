package redis

import com.redis._

object RedisService extends App {

  val r = new RedisClient("134.213.56.44", 6379)

  println(
    r.get("keys")
  )

}
