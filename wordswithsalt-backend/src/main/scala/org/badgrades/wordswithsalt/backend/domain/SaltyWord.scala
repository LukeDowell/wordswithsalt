package org.badgrades.wordswithsalt.backend.domain

import scala.beans.BeanProperty

case class SaltyWord(
                      id: String,
                      phrase: String,
                      description: String
                    ) {
  def toBean: SaltyWordBean = {
    val saltyWordBean = new SaltyWordBean()
    saltyWordBean.id = id
    saltyWordBean.phrase = phrase
    saltyWordBean.description = description
    saltyWordBean
  }
}

// Required for Firebase
class SaltyWordBean() {
  @BeanProperty var id: String = _
  @BeanProperty var phrase: String = _
  @BeanProperty var description: String = _
  def toCase: SaltyWord = SaltyWord(id, phrase, description)
}
