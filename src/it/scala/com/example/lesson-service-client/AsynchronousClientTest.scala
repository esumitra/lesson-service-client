package com.example.`lesson-service-client`

import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import sttp.client._
import sttp.tapir.client.sttp._
import com.example.lessonservice.apispec._
import scala.concurrent.{Future}
import sttp.client.asynchttpclient.future.AsyncHttpClientFutureBackend
import TestUtils.{getRequestFromEndpoint}
import cats.data.{EitherT, Nested}

class AsynchronousClientTest extends AsyncWordSpec 
  with Matchers 
  with BeforeAndAfterAll {
  
    "Asynchronous client" should {
      // implicit val backend: SttpBackend[Identity, Nothing, NothingT] = HttpURLConnectionBackend()
      implicit val sttpBackend = AsyncHttpClientFutureBackend()

      val ServerURL = "http://localhost:8080"

      val userId = "testUser"
      val scoreLessonRequestDTO = ScoreLessonRequestDTO(
        userId,
        "testLesson",
        List(
          QuizResponseDTO("question1", "response1234"),
          QuizResponseDTO("question2", "response4234"),
        )
      )

      "return 4xx error for unauthenticated client" in {
        val authToken = "xxxx"

        val clientRequest = Specification
          .scoreEndpoint
          .toSttpRequestUnsafe(uri"$ServerURL")
          .apply((authToken, scoreLessonRequestDTO))

        val responseFuture: Future[Response[Either[String,ScoreResponseDTO]]] = clientRequest.send()
        responseFuture.map { response =>
          println(s"response: ${response.body}")
          response.body shouldBe('left)
        }
      }

      "return non-zero list of next lessons in (score and get-next-lesson) workflow" in {
        val authToken = "1234"

        val responseFuture: Future[Either[String,UpcomingLessonsResponseDTO]] =
          for {
            scoreResponse <- getRequestFromEndpoint(Specification.scoreEndpoint, (authToken, scoreLessonRequestDTO)).send()
            if (scoreResponse.body.isRight)
            uid = scoreResponse.body.right.get.userId
            upcomingLessonsResponse <- getRequestFromEndpoint(Specification.upcomingLessonsEndpoint, (authToken, uid)).send()
          } yield upcomingLessonsResponse.body

        responseFuture.map { response => 
          println(s"workflow response: $response")
          response shouldBe('right)
          response.right.get.lessons.size should be > 0
        }
      }

    }

  }
