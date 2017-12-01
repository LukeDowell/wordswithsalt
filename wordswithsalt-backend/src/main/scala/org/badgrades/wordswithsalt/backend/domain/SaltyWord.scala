package org.badgrades.wordswithsalt.backend.domain

import scala.beans.BeanProperty

case class SaltyWord(
                      phrase: String,
                      description: String
                    ) {
  def toBean: SaltyWordBean = {
    val saltyWordBean = new SaltyWordBean()
    saltyWordBean.phrase = phrase
    saltyWordBean.description = description
    saltyWordBean
  }
}

class SaltyWordBean() {
  @BeanProperty var id: String = _
  @BeanProperty var phrase: String = _
  @BeanProperty var description: String = _
  def toCase: SaltyWord = SaltyWord(phrase, description)
}
