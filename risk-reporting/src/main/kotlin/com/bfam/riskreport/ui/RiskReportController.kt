package com.bfam.riskreport.ui

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class RiskReportController : Controller() {
    val statusProperty = SimpleStringProperty("")
    var status by statusProperty

    val api: Rest by inject()
    val user: UserModel by inject()

    init {
        api.baseURI = "https://api.github.com/"
    }

    fun login(username: String, password: String) {
        runLater { status = "" }
        api.setBasicAuth(username, password)
        val response = api.get("user")
        val json = response.one()
        runLater {
            if (response.ok()) {
                user.item = json.toModel()
                find(RiskReportView::class).replaceWith(WelcomeScreen::class, sizeToScene = true, centerOnScreen = true)
            } else {
                status = json.string("message") ?: "Login failed"
            }
        }
    }

    fun logout() {
        user.item = null
        primaryStage.uiComponent<UIComponent>()?.replaceWith(RiskReportView::class, sizeToScene = true, centerOnScreen = true)
    }

}