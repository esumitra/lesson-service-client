package com.example.`lesson-service-client`

import org.scalatest.{WordSpec, BeforeAndAfterAll, Matchers}
import sttp.tapir.server.stub._
import sttp.client.testing.SttpBackendStub
import sttp.client.monad.IdMonad
import sttp.client._
import sttp.tapir.client.sttp._

import com.example.lessonservice.apispec._

class LessonApiSpecTest extends WordSpec
  with Matchers
  with BeforeAndAfterAll {

  val ServerURL = uri"http://localhost:8080"
  val AuthToken = "testToken"

  "Lesson Service API" should {
    "Score endpoint" should {

      val passingScore = ScoreResponseDTO("testUser", "testLesson", 80)
      val failingScore = ScoreResponseDTO("testUser", "testLesson", 50)
      val scoreLessonRequest = ScoreLessonRequestDTO(
        "testUser",
        "testLesson",
        List(
          QuizResponseDTO("question1", "response1234"),
          QuizResponseDTO("question2", "response4234"),
        )
      )

        
      "return fail status when failing score received" in {
        implicit val backend = SttpBackendStub
          .apply(IdMonad)
          .whenRequestMatches(Specification.scoreEndpoint)
          .thenSuccess(failingScore)

        val scoreInput = (AuthToken, scoreLessonRequest)
        val clientRequest = Specification
          .scoreEndpoint
          .toSttpRequestUnsafe(ServerURL)
          .apply(scoreInput)
        val response: Either[String,ScoreResponseDTO] = clientRequest.send().body
        response shouldBe('right)
        response.map { dto =>
          dto.pass shouldBe(false)
        }
      }

      "return pass status when passing score received" in {
        implicit val backend = SttpBackendStub
          .apply(IdMonad)
          .whenRequestMatches(Specification.scoreEndpoint)
          .thenSuccess(passingScore)

        val scoreInput = (AuthToken, scoreLessonRequest)
        val clientRequest = Specification
          .scoreEndpoint
          .toSttpRequestUnsafe(ServerURL)
          .apply(scoreInput)
        val response: Either[String,ScoreResponseDTO] = clientRequest.send().body
        response shouldBe('right)
        response.map { dto =>
          dto.pass shouldBe(true)
        }
      }
    }
  }
}
