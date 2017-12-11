package org.badgrades.wordswithsalt.backend.persistence

import org.badgrades.wordswithsalt.backend.domain.SaltyWord

import scala.concurrent.Future

trait SaltyWordRepo {

  def writeWord(phrase: String, description: String): Future[SaltyWord]
  def getWordById(id: String): Future[SaltyWord]
  def getRandomWord: Future[SaltyWord]
  def getAllWords: Future[Seq[SaltyWord]]
}
