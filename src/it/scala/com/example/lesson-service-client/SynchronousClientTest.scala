package com.example.`lesson-service-client`

import org.scalatest.{WordSpec, BeforeAndAfterAll, Matchers}
import sttp.client._
import sttp.tapir.client.sttp._
import com.example.lessonservice.apispec._

class SynchronousClientTest extends WordSpec
  with Matchers
  with BeforeAndAfterAll {

    "Synchronous client" should {
      implicit val backend: SttpBackend[Identity, Nothing, NothingT] = HttpURLConnectionBackend()
      val ServerURL = "http://localhost:8080"

      val scoreLessonRequestDTO = ScoreLessonRequestDTO(
        "testUser",
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

        val response: Either[String,ScoreResponseDTO] = clientRequest.send().body
        response shouldBe('left)
        response.left.get should include("Invalid")
      }

      "return valid response for successful quiz completion" in {
        val authToken = "1234"

        val clientRequest = Specification
          .scoreEndpoint
          .toSttpRequestUnsafe(uri"$ServerURL")
          .apply((authToken, scoreLessonRequestDTO))

        val response: Either[String,ScoreResponseDTO] = clientRequest.send().body
        response shouldBe('right)
        response.map { scoreDTO => 
          println(s"score response = $scoreDTO")
          scoreDTO.lessonId shouldEqual(scoreLessonRequestDTO.lessonId)
        }
      }
    }
  
}
