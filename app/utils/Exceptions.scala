package utils

case class RequestParsingException(msg: String) extends Exception(msg)

case class NoDataFoundException(msg: String) extends Exception(msg)