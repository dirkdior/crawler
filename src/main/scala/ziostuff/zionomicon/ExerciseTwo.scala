package ziostuff.zionomicon

import zio.{ ZIO, _ }

import java.io._

object ExerciseTwo {

  //1
  def readFileZio1(file: String): ZIO[Any, Throwable, String] = ZIO
    .effect(scala.io.Source.fromFile(file))
    .flatMap(source => ZIO.effect((source.getLines.mkString, source)))
    .flatMap(res =>
      ZIO.effect {
        res._2.close
        res._1
      }
    )
  def readFileZio(file: String): ZIO[Any, Throwable, String]  = ZIO.effect {
    val source = scala.io.Source.fromFile(file)
    try source.getLines.mkString
    finally source.close()
  }

  //2
  def writeFileZio1(file: String, text: String): ZIO[Any, Throwable, Unit] = ZIO
    .effect(new PrintWriter(new File(file)))
    .flatMap(pw => ZIO.effect((pw.write(text), pw)))
    .flatMap(res => ZIO.effect(res._2.close()))
  def writeFileZio(file: String, text: String): ZIO[Any, Throwable, Unit]  = ZIO.effect {
    import java.io._
    val pw = new PrintWriter(new File(file))
    try pw.write(text)
    finally pw.close()
  }

  //3
  def copyFileZio(source: String, dest: String): ZIO[Any, Throwable, Unit] =
    readFileZio(source)
      .flatMap(contents => writeFileZio(dest, contents))

  //4
  object Four {
    def printLine(line: String): ZIO[Any, Throwable, Unit] = ZIO.effect(println(line))
    val readLine: ZIO[Any, Throwable, String]              = ZIO.effect(scala.io.StdIn.readLine())
    for {
      _ <- printLine("What is your name?")
      name <- readLine
      _ <- printLine(s"Hello, ${name}!")
    } yield ()
  }

  //5
  object Five {
    val random: ZIO[Any, Throwable, Int]                   = ZIO.effect(scala.util.Random.nextInt(3) + 1)
    def printLine(line: String): ZIO[Any, Throwable, Unit] = ZIO.effect(println(line))
    val readLine: ZIO[Any, Throwable, String]              = ZIO.effect(scala.io.StdIn.readLine())

    for {
      int <- random
      _ <- printLine("Guess a number from 1 to 3:")
      num <- readLine
      _ <- ZIO.when(num == int.toString)(printLine("You guessed right!"))
      _ <- ZIO.when(num != int.toString)(printLine(s"You guessed wrong, the number was $int!"))
    } yield ()
  }

  //6
  object Six {

    val random: ZIO[Any, Throwable, Int]      = ZIO.effect(scala.util.Random.nextInt(3) + 1)
    val readLine: ZIO[Any, Throwable, String] = ZIO.effect(scala.io.StdIn.readLine())

    random.zipWith(readLine)((rand, input) => s"$rand, $input")
  }

  //7
  object Seven {
    def printLine(line: String): ZIO[Any, Throwable, Unit] = ZIO.effect(println(line))

    val lActors      = List(printLine("Stalone"), printLine("Brad"), printLine("Smith"))
    val actorsEffect = ZIO.collectAll(lActors)
  }

  //8
  object Eight {
    def printLine(line: String): ZIO[Any, Throwable, Unit] = ZIO.effect(println(line))

    val a                                             = List(ZIO.effect(1), ZIO.effect(2), ZIO.effect(3))
    val printNumbers: ZIO[Any, Throwable, List[Unit]] =
      ZIO.foreach(a) { n =>
        printLine(n.toString)
      }
  }

  //9
  object Nine {
    val random: ZIO[Any, Throwable, Int]                   = ZIO.effect(scala.util.Random.nextInt(3) + 1)
    val readLine: ZIO[Any, Throwable, String]              = ZIO.effect(scala.io.StdIn.readLine())
    def printLine(line: String): ZIO[Any, Throwable, Unit] = ZIO.effect(println(line))

    val a                                      = List(ZIO.effect(1), ZIO.effect(2), ZIO.effect(3))
    val printNumbers: ZIO[Any, Throwable, Any] = random.orElse(printLine("Didn't work"))
  }
}

//10
import zio.{ App => ZIOApp }
object Cat extends ZIOApp {
  def run(commandLineArguments: List[String]) = {
    val showFilesContents = ZIO.foreach(commandLineArguments) { cla =>
      readFileZio(cla).flatMap(contents => printLine(contents))
    }
    showFilesContents.exitCode
  }

  def printLine(line: String): ZIO[Any, Throwable, Unit]     = ZIO.effect(println(line))
  def readFileZio(file: String): ZIO[Any, Throwable, String] = ZIO.effect {
    val source = scala.io.Source.fromFile(file)
    try source.getLines.mkString
    finally source.close()
  }
}

//11
object Eleven {
  def eitherToZIO[E, A](either: Either[E, A]): ZIO[Any, E, A] =
    either.fold(l => ZIO.fail(l), r => ZIO.succeed(r))
}

//12
object Twelve {
  def listToZIO[A](list: List[A]): ZIO[Any, None.type, A] = ZIO.succeed(list.head)
}

//13
object Thirteen {
  def currentTime(): Long                          = System.currentTimeMillis()
  lazy val currentTimeZIO: ZIO[Any, Nothing, Long] = ZIO.effectTotal(currentTime())
}

//14
object Fourteen {
  def getCacheValue(
    key: String,
    onSuccess: String => Unit,
    onFailure: Throwable => Unit
  ): Unit                                                        =
    ???
  def getCacheValueZio(key: String): ZIO[Any, Throwable, String] =
    ZIO.effectAsync { callback =>
      getCacheValue(
        key = key,
        success => callback(ZIO.succeed(success)),
        error => callback(ZIO.fail(error))
      )
    }
}

//15
object Fifteen {
  trait User
  def saveUserRecord(
    user: User,
    onSuccess: () => Unit,
    onFailure: Throwable => Unit
  ): Unit                                                      = ???
  def saveUserRecordZio(user: User): ZIO[Any, Throwable, Unit] =
    ZIO.effectAsync { callback =>
      saveUserRecord(
        user = user,
        () => callback(ZIO.succeed()),
        error => callback(ZIO.fail(error))
      )
    }
}

//16
object Sixteen {
  import scala.concurrent.{ ExecutionContext, Future }

  trait Query
  trait Result

  def doQuery(query: Query)(
    implicit ec: ExecutionContext
  ): Future[Result] = ???

  def doQueryZio(query: Query): ZIO[Any, Throwable, Result] =
    Task.fromFuture(implicit ec => doQuery(query))
}

//17
object HelloHuman extends ZIOApp {
  def run(args: List[String]): URIO[console.Console, ExitCode] = {
//    console.getStrLn.flatMap(name => console.putStrLn(s"Greetings, $name")).exitCode
    val solution = for {
      name <- console.getStrLn
      _ <- console.putStrLn(s"Greetings, $name")
    } yield ()
    solution.exitCode
  }
}

//18
import scala.util.Try
object NumberGuessing extends ZIOApp {
  def run(args: List[String]) = {
//    console
//      .putStrLn("Guess a randomly chosen number between 1 and 3")
//      .flatMap(_ =>
//        console.getStrLn.flatMap(num =>
//          random
//            .nextIntBetween(1, 4)
//            .flatMap(rand =>
//              ZIO
//                .fromTry(Try(num.toInt))
//                .flatMap(num =>
//                  ZIO
//                    .when(num == rand)(console.putStrLn("You were right!"))
//                    .flatMap(_ => ZIO.when(num != rand)(console.putStrLn(s"You were so wrong! The guess was $rand")))
//                )
//            )
//        )
//      )
//      .exitCode

    val solution = for {
      _ <- console.putStrLn("Guess a randomly chosen number between 1 and 3")
      rawNum <- console.getStrLn
      rand <- random.nextIntBetween(1, 4)
      num <- ZIO.fromTry(Try(rawNum.toInt))
      _ <- ZIO.when(num == rand)(console.putStrLn("You were right!"))
      _ <- ZIO.when(num != rand)(console.putStrLn(s"You were so wrong! The guess was $rand"))
    } yield ()
    solution.exitCode
  }
}

//19
