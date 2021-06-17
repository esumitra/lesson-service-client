package com.example.`lesson-service-client`

import sttp.tapir.Endpoint
import sttp.model.Uri
import sttp.client._
import sttp.tapir.client.sttp._


object TestUtils {
  
  def getRequestFromEndpoint[I, E, O](
    endpoint: Endpoint[I, E, O, Nothing],
    input: I,
    serverURL: Uri = Uri("localhost", 8080)
  ): Request[Either[E,O],Nothing] =
    endpoint
      .toSttpRequestUnsafe(serverURL)
      .apply(input)

}
