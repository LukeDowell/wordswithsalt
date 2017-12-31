package org.badgrades.wordswithsalt.backend.persistence

import org.badgrades.wordswithsalt.backend.domain.SaltyWord

import scala.concurrent.{ExecutionContext, Future}

trait SaltyWordRepo {

  def writeWord(phrase: String, description: String)(implicit ec: ExecutionContext): Future[SaltyWord]
  def getWordById(id: String)(implicit ec: ExecutionContext): Future[SaltyWord]
  def getRandomWord(implicit ec: ExecutionContext): Future[SaltyWord]
  def getAllWords(implicit ec: ExecutionContext): Future[Seq[SaltyWord]]
}
