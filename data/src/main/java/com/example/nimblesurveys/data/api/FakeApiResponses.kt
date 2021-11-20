package com.example.nimblesurveys.data.api

const val ID = 1
const val EMAIL = "test@abc.com"
const val PASSWORD = "password"
const val API_KEY = "apikey"
const val API_SECRET = "apisecret"
const val ACCESS_TOKEN = "accesstoken"
const val REFRESH_TOKEN = "refreshtoken"
const val TOKEN_TYPE = "Bearer"
const val EXPIRY = 7200L
const val CREATED_AT = 1597169495L
const val AVATAR_URL = "avatarurl"

const val LOGIN_RESPONSE_SUCCESS = """
{
  "data": {
    "id": $ID,
    "type": "token",
    "attributes": {
      "access_token": "$ACCESS_TOKEN",
      "token_type": "$TOKEN_TYPE",
      "expires_in": $EXPIRY,
      "refresh_token": "$REFRESH_TOKEN",
      "created_at": $CREATED_AT
    }
  }
}
"""

const val LOGIN_RESPONSE_FAILED = """
{
  "errors": [
    {
      "source": "Doorkeeper::OAuth::Error",
      "detail": "Client authentication failed due to unknown client, no client authentication included, or unsupported authentication method.",
      "code": "invalid_client"
    }
  ]
}
"""

const val USER_RESPONSE_SUCCESS = """
{
  "data": {
    "id": $ID,
    "type": "user",
    "attributes": {
      "email": "$EMAIL",
      "avatar_url": "$AVATAR_URL"
    }
  }
}
"""

const val RESPONSE_FAILED_EMPTY = "{}"